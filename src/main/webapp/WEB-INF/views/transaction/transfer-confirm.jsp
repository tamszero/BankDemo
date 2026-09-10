<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>이체 확인</title></head>
<body>
<h1>이체 내용을 확인해주세요</h1>

<table>
    <tr>
        <th>받는 분</th>
        <td><strong>${target.ownerName}</strong></td>
    </tr>
    <tr>
        <th>계좌번호</th>
        <td>${target.accountNumber}</td>
    </tr>
    <tr>
        <th>이체 금액</th>
        <td><strong><fmt:formatNumber value="${transferRequest.amount}" pattern="#,###"/>원</strong></td>
    </tr>
</table>

<p>이체 후에는 취소할 수 없습니다. 받는 분과 금액을 다시 확인해주세요.</p>

<form action="/transactions/transfer" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="hidden" name="fromAccountId"   value="${transferRequest.fromAccountId}">
    <input type="hidden" name="toAccountNumber" value="${transferRequest.toAccountNumber}">
    <input type="hidden" name="amount"          value="${transferRequest.amount}">
    <input type="hidden" name="password"        value="${transferRequest.password}">
    <input type="hidden" name="token"           value="${token}">

    <button type="submit">이체하기</button>
    <a href="/transactions/transfer">취소</a>
</form>
</body>
</html>