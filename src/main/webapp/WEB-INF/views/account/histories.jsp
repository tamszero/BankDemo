<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>거래내역</title>
    <style>
        table { border-collapse: collapse; width: 100%; max-width: 700px; }
        th, td { border: 1px solid #ddd; padding: 8px 12px; text-align: right; }
        th { background: #f5f5f5; text-align: center; }
        td.type { text-align: center; }
        td.date { text-align: center; white-space: nowrap; }
        .deposit { color: #1a7f37; }   /* 입금: 초록 */
        .withdraw { color: #d1242f; } /* 출금: 빨강 */
        .empty { color: #888; padding: 24px 0; }
    </style>
</head>
<body>
<h1>거래내역</h1>
 
<div>
    <strong>${account.accountNumber}</strong>
    (현재 잔액: <fmt:formatNumber value="${account.balance}" pattern="#,###"/>원)
</div>
 
<br/>
 
<c:choose>
    <c:when test="${empty histories}">
        <p class="empty">거래내역이 없습니다.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>거래일시</th>
                <th>구분</th>
                <th>금액</th>
                <th>거래 후 잔액</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="h" items="${histories}">
                <tr>
                    <td class="date">${h.createdAtText}</td>
 
                    <%-- 이 계좌 입장에서 출금 쪽이면 "출금", 입금 쪽이면 "입금" --%>
                    <c:choose>
                        <c:when test="${h.withdrawAccountId == account.id}">
                            <td class="type withdraw">출금</td>
                            <td class="withdraw">-<fmt:formatNumber value="${h.amount}" pattern="#,###"/>원</td>
                            <td><fmt:formatNumber value="${h.withdrawBalance}" pattern="#,###"/>원</td>
                        </c:when>
                        <c:when test="${h.depositAccountId == account.id}">
                            <td class="type deposit">입금</td>
                            <td class="deposit">+<fmt:formatNumber value="${h.amount}" pattern="#,###"/>원</td>
                            <td><fmt:formatNumber value="${h.depositBalance}" pattern="#,###"/>원</td>
                        </c:when>
                    </c:choose>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
 
<br/>
<a href="/accounts/${account.id}">계좌 상세로 돌아가기</a>
</body>
</html>
 


history_1.jsp 다운로드됨 Explorer에서 표시