<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 계좌</title>
</head>
<body>
<h1>내 계좌 목록</h1>

<c:if test="${not empty message}">
    <p style="color:green">${message}</p>
</c:if>

<c:choose>
    <c:when test="${empty accounts}">
        <p>보유한 계좌가 없습니다.</p>
        <form action="/accounts/new" method="get">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">계좌 개설</button>
        </form>
    </c:when>
    <c:otherwise>
        <table>
            <c:forEach var="acc" items="${accounts}">
                <tr>
                    <td><a href="/accounts/${acc.id}">${acc.accountNumber}</a></td>
                    <td><fmt:formatNumber value="${acc.balance}" pattern="#,###"/>원</td>
                </tr>
            </c:forEach>
        </table>

        <form action="/accounts/new" method="get">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">계좌 개설</button>
        </form>
        <form action="/transactions/deposit" method="get">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">입금하기</button>
        </form>
        <form action="/transactions/withdraw" method="get">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">출금하기</button>
        </form>
        <form action="/transactions/transfer" method="get">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">이체하기</button>
        </form>

    </c:otherwise>
</c:choose>
<a href="/">홈으로</a>


</body>
</html>