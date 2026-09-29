<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>이체 확인</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="이체 확인"/>
<c:set var="backUrl" value="${pageContext.request.contextPath}/transactions/transfer"/>
<%@ include file="../fragments/topbar.jspf" %>

<div class="bk-status" style="gap:28px">
    <h2 style="font-size:28px;line-height:1.25"><span style="color:var(--color-accent)">${target.ownerName}</span>님에게<br><fmt:formatNumber value="${transferRequest.amount}" pattern="#,###"/>원을<br>보낼까요?</h2>

    <div class="bk-divider2">
        <div class="bk-kv wide"><span class="k">받는 분</span><span class="v-strong">${target.ownerName}</span></div>
        <div class="bk-kv wide" style="border-bottom:2px solid var(--color-divider)"><span class="k">받는 계좌</span><span class="v-strong">${target.accountNumber}</span></div>
    </div>

    <form action="${pageContext.request.contextPath}/transactions/transfer" method="post" class="bk-btn-row" style="margin-top:auto">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="fromAccountId" value="${transferRequest.fromAccountId}">
        <input type="hidden" name="toAccountNumber" value="${transferRequest.toAccountNumber}">
        <input type="hidden" name="amount" value="${transferRequest.amount}">
        <input type="hidden" name="password" value="${transferRequest.password}">
        <input type="hidden" name="token" value="${token}">
        <button type="submit" class="bk-btn bk-btn-primary">이체하기
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg></button>
    </form>
    <a class="bk-btn bk-btn-secondary" href="${pageContext.request.contextPath}/transactions/transfer">수정하기</a>
</div>

</div>
</div>
</body>
</html>
