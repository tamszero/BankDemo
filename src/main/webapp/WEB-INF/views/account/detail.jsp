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

<c:if test="${not empty message}">
    <p style="color:green">${message}</p>
</c:if>

<a href="/accounts">목록으로</a>
</body>
</html>