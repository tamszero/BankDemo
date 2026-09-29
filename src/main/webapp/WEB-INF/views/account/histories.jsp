<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>거래내역</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="거래내역"/>
<c:set var="backUrl" value="${pageContext.request.contextPath}/accounts/${account.id}"/>
<%@ include file="../fragments/topbar.jspf" %>

<div style="padding:18px 20px 16px;display:flex;justify-content:space-between;align-items:baseline">
    <span style="display:flex;flex-direction:column;gap:2px">
        <span style="font:600 14px var(--font-body)">입출금 계좌</span>
        <span style="font-size:13px;color:var(--color-neutral-700);font-variant-numeric:tabular-nums">${fn:substring(account.accountNumber,0,3)}-${fn:substring(account.accountNumber,3,6)}-${fn:substring(account.accountNumber,6,12)}</span>
    </span>
    <span style="font:800 20px var(--font-heading);font-variant-numeric:tabular-nums"><fmt:formatNumber value="${account.balance}" pattern="#,###"/>원</span>
</div>

<c:if test="${not empty histories}">
<div class="bk-seg" id="histSeg">
    <button type="button" class="active" data-filter="all">전체</button>
    <button type="button" data-filter="in">입금</button>
    <button type="button" data-filter="out">출금</button>
</div>
</c:if>

<c:choose>
    <c:when test="${empty histories}">
        <div class="bk-empty" style="border-top:2px solid var(--color-divider)">해당하는 거래내역이 없습니다.</div>
    </c:when>
    <c:otherwise>
        <c:set var="prevDate" value=""/>
        <c:forEach var="h" items="${histories}">
            <c:set var="curDate" value="${fn:substring(h.createdAtText,0,10)}"/>
            <c:if test="${curDate != prevDate}">
                <div class="bk-tx-date-head">${curDate}</div>
                <c:set var="prevDate" value="${curDate}"/>
            </c:if>

            <c:set var="isOut" value="${h.withdrawAccountId == account.id}"/>
            <c:set var="dir" value="${isOut ? 'out' : 'in'}"/>
            <c:choose>
                <c:when test="${isOut}">
                    <c:set var="kind" value="${h.txType == 'TRANSFER' ? '이체' : '출금'}"/>
                    <c:set var="counter" value="${not empty h.depositAccountNumber ? h.depositAccountHolder : '현금 출금'}"/>
                    <c:set var="bal" value="${h.withdrawBalance}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="kind" value="${h.txType == 'TRANSFER' ? '이체' : '입금'}"/>
                    <c:set var="counter" value="${not empty h.withdrawAccountNumber ? h.withdrawAccountHolder : '현금 입금'}"/>
                    <c:set var="bal" value="${h.depositBalance}"/>
                </c:otherwise>
            </c:choose>

            <div class="bk-tx-row" data-dir="${dir}">
                <span class="bk-tx-time">${fn:substring(h.createdAtText,11,16)}</span>
                <span class="bk-tx-info">
                    <span class="title">${counter}</span>
                    <span class="sub">${kind}</span>
                </span>
                <span style="display:flex;flex-direction:column;align-items:flex-end;gap:2px">
                    <span class="bk-tx-amt ${dir}">${isOut ? '−' : '+'}<fmt:formatNumber value="${h.amount}" pattern="#,###"/>원</span>
                    <span class="bk-tx-bal"><fmt:formatNumber value="${bal}" pattern="#,###"/>원</span>
                </span>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

</div>
</div>

<c:if test="${not empty histories}">
<script>
(function () {
    var seg = document.getElementById('histSeg');
    if (!seg) return;
    seg.addEventListener('click', function (e) {
        var btn = e.target.closest('button[data-filter]');
        if (!btn) return;
        seg.querySelectorAll('button').forEach(function (b) { b.classList.toggle('active', b === btn); });
        var filter = btn.getAttribute('data-filter');
        document.querySelectorAll('.bk-tx-row').forEach(function (row) {
            row.style.display = (filter === 'all' || row.getAttribute('data-dir') === filter) ? '' : 'none';
        });
    });
})();
</script>
</c:if>
</body>
</html>
