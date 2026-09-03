# 동시 출금(락) 테스트용 스크립트
# Postman은 요청을 순차적으로 보내는 싱글스레드 러너라 "동시에 두 요청이 도착"하는 상황을
# 재현하기 어렵습니다. 이 스크립트는 PowerShell로 로그인 세션(JSESSIONID)과 CSRF 토큰을
# 딱 한 번 얻은 뒤, 그 세션을 공유하는 출금 요청 2개를 백그라운드 Job으로 "동시에" 쏩니다.
#
# 사용법:
#   .\postman\concurrent-withdraw-test.ps1
#   .\postman\concurrent-withdraw-test.ps1 -AccountId 1 -Amount 700 -Password "1234"
#
# 기대 결과
#   - 락(FOR UPDATE)이 걸려있는 현재 코드: 둘 중 하나만 "출금이 완료되었습니다", 나머지는
#     "잔액이 부족합니다"(잔액이 두 번째 요청 시점엔 이미 줄어있으므로) -> 최종 잔액이 음수로 안 감.
#   - findByIdForUpdate 대신 findById(락 없는 조회)로 바꿔서 같은 스크립트를 돌리면, 운이 나쁘면
#     두 요청이 같은 balance를 읽어 둘 다 성공 처리되어 잔액이 음수(오버드래프트)가 될 수 있음.
#     (타이밍에 따라 매번 재현되지는 않을 수 있어서, 여러 번 반복 실행 권장)

param(
    [string]$BaseUrl = "http://localhost:8080",
    [string]$UserId = "gildong",
    [string]$LoginPassword = "1234",
    [int]$AccountId = 1,
    [int]$Amount = 700,
    [string]$Password = "1234"
)

$ErrorActionPreference = "Stop"

# 1) 로그인 페이지에서 CSRF 토큰 얻기 + 세션 생성
$loginPage = Invoke-WebRequest -Uri "$BaseUrl/members/login" -SessionVariable session
if ($loginPage.Content -match 'name="_csrf" value="([^"]+)"') {
    $csrf = $Matches[1]
} else {
    throw "로그인 페이지에서 _csrf 토큰을 찾지 못했습니다."
}
Write-Host "CSRF token: $csrf"

# 2) 로그인 (같은 $session 쿠키 컨테이너를 계속 재사용)
$loginBody = @{
    userId      = $UserId
    password    = $LoginPassword
    redirectURL = "/"
    _csrf       = $csrf
}
Invoke-WebRequest -Uri "$BaseUrl/members/login" -Method POST -Body $loginBody -WebSession $session -MaximumRedirection 5 | Out-Null
Write-Host "로그인 완료 (session cookie 확보)"

# 3) 같은 세션/쿠키를 각 Job에 넘겨서 거의 동시에 출금 요청 2개 발사
$withdrawJob = {
    param($BaseUrl, $session, $csrf, $AccountId, $Amount, $Password, $tag)
    $body = @{
        accountId = $AccountId
        amount    = $Amount
        password  = $Password
        _csrf     = $csrf
    }
    try {
        $res = Invoke-WebRequest -Uri "$BaseUrl/transactions/withdraw" -Method POST -Body $body -WebSession $session -MaximumRedirection 5
        if ($res.Content -match "출금이 완료되었습니다") { $result = "SUCCESS" }
        elseif ($res.Content -match "잔액이 부족합니다") { $result = "INSUFFICIENT_BALANCE" }
        else { $result = "OTHER($($res.StatusCode))" }
    } catch {
        $result = "ERROR: $($_.Exception.Message)"
    }
    "[$tag] $result"
}

$job1 = Start-Job -ScriptBlock $withdrawJob -ArgumentList $BaseUrl, $session, $csrf, $AccountId, $Amount, $Password, "요청1"
$job2 = Start-Job -ScriptBlock $withdrawJob -ArgumentList $BaseUrl, $session, $csrf, $AccountId, $Amount, $Password, "요청2"

Wait-Job $job1, $job2 | Out-Null
Receive-Job $job1
Receive-Job $job2
Remove-Job $job1, $job2

# 4) 최종 잔액 확인 (같은 세션으로 계좌 상세 페이지 조회)
$detail = Invoke-WebRequest -Uri "$BaseUrl/accounts/$AccountId" -WebSession $session
if ($detail.Content -match '잔액:\s*([\d,]+)원') {
    Write-Host "최종 잔액: $($Matches[1])원"
} else {
    Write-Host "잔액 파싱 실패 - 응답 본문을 직접 확인하세요."
}
