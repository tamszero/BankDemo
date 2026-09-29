<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>계좌 상세</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="계좌 상세"/>
<c:set var="backUrl" value="${pageContext.request.contextPath}/accounts"/>
<%@ include file="../fragments/topbar.jspf" %>

<c:if test="${not empty message}">
    <div class="bk-flash bk-flash-ok"><span class="bk-flash-dot"></span>${message}</div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="bk-flash bk-flash-err"><span class="bk-flash-dot"></span>${errorMessage}</div>
</c:if>

<div style="padding:24px 20px 22px;display:flex;flex-direction:column;gap:6px">
    <span style="font:600 14px var(--font-body)">입출금 계좌</span>
    <span style="font-size:14px;color:var(--color-neutral-700);font-variant-numeric:tabular-nums">${fn:substring(account.accountNumber,0,3)}-${fn:substring(account.accountNumber,3,6)}-${fn:substring(account.accountNumber,6,12)}</span>
    <span style="font:800 38px/1.1 var(--font-heading);letter-spacing:-0.03em;margin-top:14px;font-variant-numeric:tabular-nums"><fmt:formatNumber value="${account.balance}" pattern="#,###"/>원</span>
    <span style="font-size:13px;color:var(--color-neutral-700)">개설일 ${account.createdAtText}</span>
</div>

<div class="bk-actions3">
    <a class="bk-action" style="height:56px;flex-direction:row;align-items:center;justify-content:flex-start;gap:8px" href="${pageContext.request.contextPath}/transactions/deposit">입금</a>
    <a class="bk-action" style="height:56px;flex-direction:row;align-items:center;justify-content:flex-start;gap:8px" href="${pageContext.request.contextPath}/transactions/withdraw">출금</a>
    <a class="bk-action" style="height:56px;flex-direction:row;align-items:center;justify-content:center;background:var(--color-accent);color:var(--color-bg)" href="${pageContext.request.contextPath}/transactions/transfer">이체</a>
</div>

<div class="bk-section-head">
    <span class="bk-section-title">거래내역</span>
    <a class="bk-link-btn" href="${pageContext.request.contextPath}/accounts/${account.id}/histories">전체 보기</a>
</div>

<div style="padding:22px 20px 0">
    <span style="font-size:14px;color:var(--color-neutral-700)">계좌 해지를 원하시면 아래 비밀번호를 입력해주세요.</span>
</div>
<form action="${pageContext.request.contextPath}/accounts/${account.id}/close" method="post" class="bk-form bk-form-tight" style="padding-top:12px">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class="bk-field">
        <label for="closePw">계좌 비밀번호</label>
        <input type="password" id="closePw" name="password" class="bk-input bk-input-pin" placeholder="숫자 4자리">
    </div>
    <button type="submit" class="bk-btn-ghost" style="align-self:flex-start">계좌 해지</button>
</form>

</div>
</div>
</body>
</html>
