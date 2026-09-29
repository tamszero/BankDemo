<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>입금</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="입금"/>
<c:set var="backUrl" value="${pageContext.request.contextPath}/accounts"/>
<%@ include file="../fragments/topbar.jspf" %>

<form:form modelAttribute="depositRequest" action="${pageContext.request.contextPath}/transactions/deposit" method="post">
    <form:errors path="" cssClass="bk-form-err" element="div" cssStyle="margin:18px 20px 0"/>

    <div style="padding:22px 20px 10px" class="bk-amount-label">입금 계좌</div>
    <div class="bk-divider2">
        <c:choose>
            <c:when test="${empty accounts}">
                <div class="bk-empty">사용 가능한 계좌가 없습니다.</div>
            </c:when>
            <c:otherwise>
                <c:forEach var="acc" items="${accounts}" varStatus="st">
                    <label class="bk-pick">
                        <form:radiobutton path="accountId" value="${acc.id}"/>
                        <span class="bk-pick-dot"></span>
                        <span class="bk-row-name">
                            <span class="title">입출금 <fmt:formatNumber value="${st.count}" pattern="00"/></span>
                            <span class="sub">${fn:substring(acc.accountNumber,0,3)}-${fn:substring(acc.accountNumber,3,6)}-${fn:substring(acc.accountNumber,6,12)}</span>
                        </span>
                        <span class="bk-row-amount"><fmt:formatNumber value="${acc.balance}" pattern="#,###"/>원</span>
                    </label>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <form:errors path="accountId" cssClass="bk-err" element="span" cssStyle="padding:8px 20px 0;display:block"/>

    <div style="padding:26px 20px 0;display:flex;flex-direction:column;gap:10px">
        <span class="bk-amount-label">입금 금액</span>
        <div class="bk-amount-row">
            <form:input path="amount" type="number" id="txAmount" placeholder="0"/>
            <span class="bk-amount-won">원</span>
        </div>
        <form:errors path="amount" cssClass="bk-err" element="span"/>
        <div class="bk-chips">
            <button type="button" class="bk-chip" data-add="10000">+1만</button>
            <button type="button" class="bk-chip" data-add="50000">+5만</button>
            <button type="button" class="bk-chip" data-add="100000">+10만</button>
        </div>
    </div>

    <div class="bk-field" style="padding:26px 20px 0">
        <label for="txPw">계좌 비밀번호</label>
        <form:password id="txPw" path="password" placeholder="숫자 4자리" cssClass="bk-input bk-input-pin"/>
        <form:errors path="password" cssClass="bk-err" element="span"/>
    </div>

    <div style="padding:24px 20px 44px">
        <button type="submit" class="bk-btn bk-btn-primary">입금하기
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg></button>
    </div>
</form:form>

</div>
</div>
<script>
document.querySelectorAll('.bk-chip[data-add]').forEach(function (chip) {
    chip.addEventListener('click', function () {
        var input = document.getElementById('txAmount');
        var cur = parseInt(input.value || '0', 10) || 0;
        input.value = cur + parseInt(chip.getAttribute('data-add'), 10);
    });
});
</script>
</body>
</html>
