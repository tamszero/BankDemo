<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>계좌 개설</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="계좌 개설"/>
<c:set var="backUrl" value="${pageContext.request.contextPath}/accounts"/>
<%@ include file="../fragments/topbar.jspf" %>

<form:form modelAttribute="accountCreateRequest" action="${pageContext.request.contextPath}/accounts/new" method="post" cssClass="bk-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <h2>입출금 계좌를<br>개설합니다</h2>

    <div class="bk-divider2">
        <div class="bk-kv"><span class="k">계좌번호</span><span>110으로 시작하는 12자리, 자동 발급</span></div>
        <div class="bk-kv"><span class="k">예금주</span><span>${sessionScope.loginMember.userName}</span></div>
        <div class="bk-kv"><span class="k">초기 잔액</span><span>0원</span></div>
    </div>

    <form:errors path="" cssClass="bk-form-err" element="div"/>

    <div class="bk-field">
        <label for="newPw">계좌 비밀번호</label>
        <form:password id="newPw" path="password" placeholder="숫자 4자리" cssClass="bk-input bk-input-pin"/>
        <form:errors path="password" cssClass="bk-err" element="span"/>
    </div>

    <div class="bk-field">
        <label for="newPw2">비밀번호 확인</label>
        <form:password id="newPw2" path="passwordConfirm" placeholder="숫자 4자리" cssClass="bk-input bk-input-pin"/>
        <form:errors path="passwordConfirm" cssClass="bk-err" element="span"/>
    </div>

    <button type="submit" class="bk-btn bk-btn-primary" style="margin-top:4px">개설하기
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg></button>
</form:form>

</div>
</div>
</body>
</html>
