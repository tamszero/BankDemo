<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>로그인</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="로그인"/>
<c:set var="backUrl" value="${pageContext.request.contextPath}/"/>
<%@ include file="../fragments/topbar.jspf" %>

<c:if test="${not empty message}">
    <div class="bk-flash bk-flash-ok"><span class="bk-flash-dot"></span>${message}</div>
</c:if>

<form:form modelAttribute="loginRequest" action="${pageContext.request.contextPath}/members/login" method="post" cssClass="bk-form">
    <input type="hidden" name="redirectURL" value="${redirectURL}"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <h2>아이디와 비밀번호를<br>입력해주세요</h2>

    <form:errors path="" cssClass="bk-form-err" element="div"/>

    <div class="bk-field">
        <label for="loginId">아이디</label>
        <form:input id="loginId" path="userId" placeholder="아이디" cssClass="bk-input"/>
        <form:errors path="userId" cssClass="bk-err" element="span"/>
    </div>

    <div class="bk-field">
        <label for="loginPw">비밀번호</label>
        <form:password id="loginPw" path="password" placeholder="비밀번호" cssClass="bk-input"/>
        <form:errors path="password" cssClass="bk-err" element="span"/>
    </div>

    <button type="submit" class="bk-btn bk-btn-primary" style="margin-top:4px">로그인
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg></button>

    <div style="display:flex;align-items:center;justify-content:space-between;border-top:2px solid var(--color-divider);padding-top:16px;margin-top:8px">
        <span style="font-size:14px;color:var(--color-neutral-700)">계정이 없으신가요?</span>
        <a class="bk-link-btn" style="font:800 14px var(--font-heading)" href="${pageContext.request.contextPath}/members/join">회원가입</a>
    </div>
</form:form>

</div>
</div>
</body>
</html>
