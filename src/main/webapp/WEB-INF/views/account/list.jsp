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

<c:choose>
    <c:when test="${empty accounts}">
        <p>보유한 계좌가 없습니다.</p>
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
    </c:otherwise>
</c:choose>
<a href="/">홈으로</a>
<a href="/accounts/new">계좌 개설</a>

</body>
</html>