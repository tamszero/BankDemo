# Real Bank - 계좌 이체 중심 데모 뱅킹 서비스

Spring Boot + Mybatis 기반 은행 서비스입니다.
**원장(ledger)**기반 설계 적용, 
비관적 락을 통한 동시 거래 갱신 손실 차단 구현

## 화면
<details>
<summary>화면 보기 (클릭)</summary>
| 홈 | 이체 확인 |
|---|---|
| <img width="750" height="1334" alt="home" src="https://github.com/user-attachments/assets/0acab297-9577-46ee-8a01-1e4374f10175" /> | <img width="750" height="1334" alt="transfer confirm" src="https://github.com/user-attachments/assets/992a886e-77d6-4597-863f-5aa3b2ef7c37" /> |
| 거래내역 | 에러 페이지 |
|---|---|
| <img width="750" height="1334" alt="history" src="https://github.com/user-attachments/assets/8ddc9e64-b743-43bf-a8f2-bb995789c6be" /> | <img width="750" height="1334" alt="error" src="https://github.com/user-attachments/assets/34874c76-f37f-4064-91c9-e266762b93f6" /> |
</details>

## 주요 기능
- 로그인 및 회원가입
- 계좌 개설 / 거래내역 / 잔액확인
- 입금 / 출금 / 이체

## 실행
git clone ...
./gradle bootrun

http://localhost:8080 - H2 인메모리 DB로 즉시 실행가능
테스트 계정: gildong / 1234 
            dooly / 1234
            mai / 1234

## 동시성 제어 검증 

## 보안 고려사항

## 한계 및 개선 방향
- **멱등성** — 세션 기반이라 동일 브라우저 내 중복만 차단됩니다.
  분산 환경에서는 요청 키를 DB나 Redis에 저장하는 방식이 필요합니다.
- **이체 확인 페이지** — 계좌 비밀번호를 hidden으로 전달해 HTML 소스에 노출됩니다.
  확인 단계에서 재입력받는 방식이 안전합니다.
- **거래내역 조회** — `w_account_id OR d_account_id` 조건이 인덱스를 충분히 활용하지
  못합니다. `UNION ALL` 분리를 고려할 수 있습니다.
- **상태값** — `ACTIVE`/`CLOSED`를 문자열로 관리해 타입 안정성이 없습니다. enum 전환 필요.
