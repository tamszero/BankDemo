<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>계좌 상세</title>
</head>
<body>
<h1>계좌 상세</h1>

<div>계좌번호: ${account.accountNumber}</div>
<div>잔액: <fmt:formatNumber value="${account.balance}" pattern="#,###"/>원</div>
<div>개설일: ${account.createdAtText}</div>

<c:if test="${not empty errorMessage}">
    <p style="color:red">${errorMessage}</p>
</c:if>

<c:if test="${not empty message}">
    <p style="color:green">${message}</p>
</c:if>

<h3>계좌 해지</h3>
<p>해지할 계좌의 비밀번호를 입력해주세요</p>
<form action="/accounts/${account.id}/close" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="password" name="password"/>
    <button type="submit">해지</button>
</form>

<a href="/accounts">목록으로</a>
<a href="/accounts/${account.id}/histories">거래내역</a>
</body>
</html>