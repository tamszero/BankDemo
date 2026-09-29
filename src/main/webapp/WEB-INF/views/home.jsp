<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>DEMO BANK</title>
    <%@ include file="fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">

<c:if test="${empty loginMember}">
    <div class="bk-hero">
        <div class="bk-hero-top">
            <span class="bk-hero-brand">데모 뱅크 어플리케이션</span>
            <span class="bk-hero-year">2026</span>
        </div>
        <h1>Real<br>Bank</h1>
    </div>
    <div class="bk-hero-grid">
        <div><span class="no">01</span><span class="label">계좌 개설</span></div>
        <div><span class="no">02</span><span class="label">입금 · 출금</span></div>
        <div><span class="no">03</span><span class="label">이체</span></div>
    </div>
    <p class="bk-hero-copy">계좌 개설부터 이체까지, 필요한 기능만 담았습니다.</p>
    <div class="bk-btn-row" style="margin-top:auto;padding:0 20px 44px">
        <a class="bk-btn bk-btn-primary" href="${pageContext.request.contextPath}/members/login">로그인
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg></a>
        <a class="bk-btn bk-btn-secondary" href="${pageContext.request.contextPath}/members/join">회원가입
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg></a>
    </div>
</c:if>

<c:if test="${not empty loginMember}">
    <div class="bk-homebar">
        <span class="bk-homebar-brand">DEMO BANK</span>
        <a class="bk-homebar-user" href="${pageContext.request.contextPath}/members/mypage">${loginMember.userName}님
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><circle cx="12" cy="8" r="4"></circle><path d="M4 21a8 8 0 0 1 16 0"></path></svg></a>
    </div>

    <div class="bk-balance">
        <span class="bk-balance-label">총 잔액</span>
        <span class="bk-balance-amount"><fmt:formatNumber value="${totalBalance}" pattern="#,###"/>원</span>
        <jsp:useBean id="now" class="java.util.Date"/>
        <span class="bk-balance-meta">계좌 ${fn:length(accounts)}개 · <fmt:formatDate value="${now}" pattern="yyyy.MM.dd"/> 기준</span>
    </div>

    <div class="bk-actions3">
        <a class="bk-action" href="${pageContext.request.contextPath}/transactions/deposit">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M17 7 7 17"></path><path d="M17 17H7V7"></path></svg>입금</a>
        <a class="bk-action" href="${pageContext.request.contextPath}/transactions/withdraw">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M7 7h10v10"></path><path d="M7 17 17 7"></path></svg>출금</a>
        <a class="bk-action" href="${pageContext.request.contextPath}/transactions/transfer">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M8 3 4 7l4 4"></path><path d="M4 7h16"></path><path d="m16 21 4-4-4-4"></path><path d="M20 17H4"></path></svg>이체</a>
    </div>

    <div class="bk-section-head">
        <span class="bk-section-title">내 계좌</span>
        <a class="bk-link-btn" href="${pageContext.request.contextPath}/accounts">전체 보기</a>
    </div>

    <c:choose>
        <c:when test="${empty accounts}">
            <div style="padding:20px;display:flex;flex-direction:column;gap:12px;align-items:flex-start;border-bottom:1px solid var(--color-divider)">
                <span style="font-size:14px;color:var(--color-neutral-700)">아직 계좌가 없습니다.</span>
                <a class="bk-btn bk-btn-primary" style="width:auto;height:44px;font-size:14px" href="${pageContext.request.contextPath}/accounts/new">계좌 개설하기</a>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="acc" items="${accounts}" varStatus="st">
                <a class="bk-row" href="${pageContext.request.contextPath}/accounts/${acc.id}">
                    <span class="bk-row-name">
                        <span class="title">입출금 <fmt:formatNumber value="${st.count}" pattern="00"/></span>
                        <span class="sub">${fn:substring(acc.accountNumber,0,3)}-${fn:substring(acc.accountNumber,3,6)}-${fn:substring(acc.accountNumber,6,12)}</span>
                    </span>
                    <span class="bk-row-amount"><fmt:formatNumber value="${acc.balance}" pattern="#,###"/>원</span>
                    <svg class="bk-chev" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="m9 18 6-6-6-6"></path></svg>
                </a>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</c:if>

</div>

<c:if test="${not empty loginMember}">
    <c:set var="activeTab" value="home"/>
    <%@ include file="fragments/tabbar.jspf" %>
</c:if>

</div>
</body>
</html>
