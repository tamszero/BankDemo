<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>내 계좌</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="내 계좌"/>
<%@ include file="../fragments/topbar.jspf" %>

<c:if test="${not empty message}">
    <div class="bk-flash bk-flash-ok"><span class="bk-flash-dot"></span>${message}</div>
</c:if>

<c:set var="total" value="${0}"/>
<c:forEach var="acc" items="${accounts}"><c:set var="total" value="${total + acc.balance}"/></c:forEach>

<div style="padding:24px 20px 18px;display:flex;align-items:flex-end;justify-content:space-between">
    <div style="display:flex;flex-direction:column;gap:4px">
        <span class="bk-balance-label">총 잔액</span>
        <span style="font:800 30px/1.1 var(--font-heading);letter-spacing:-0.02em;font-variant-numeric:tabular-nums"><fmt:formatNumber value="${total}" pattern="#,###"/>원</span>
    </div>
    <span style="font-size:13px;color:var(--color-neutral-700)">${fn:length(accounts)}개 계좌</span>
</div>

<c:choose>
    <c:when test="${empty accounts}">
        <div class="bk-empty" style="border-top:2px solid var(--color-divider);border-bottom:1px solid var(--color-divider)">아직 계좌가 없습니다.</div>
    </c:when>
    <c:otherwise>
        <div class="bk-divider2">
            <c:forEach var="acc" items="${accounts}" varStatus="st">
                <a class="bk-acc-row" href="${pageContext.request.contextPath}/accounts/${acc.id}">
                    <span class="bk-acc-row-top">
                        <span style="font:600 15px var(--font-body)">입출금 <fmt:formatNumber value="${st.count}" pattern="00"/></span>
                        <svg class="bk-chev" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="m9 18 6-6-6-6"></path></svg>
                    </span>
                    <span class="bk-acc-row-bottom">
                        <span class="num">${fn:substring(acc.accountNumber,0,3)}-${fn:substring(acc.accountNumber,3,6)}-${fn:substring(acc.accountNumber,6,12)}</span>
                        <span class="bal"><fmt:formatNumber value="${acc.balance}" pattern="#,###"/>원</span>
                    </span>
                </a>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<div style="padding:20px">
    <a class="bk-btn bk-btn-secondary" href="${pageContext.request.contextPath}/accounts/new">새 계좌 개설
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="var(--color-accent)" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="M12 5v14"></path></svg></a>
</div>

</div>
<c:set var="activeTab" value="accounts"/>
<%@ include file="../fragments/tabbar.jspf" %>
</div>
</body>
</html>
