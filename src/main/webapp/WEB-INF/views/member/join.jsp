<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>회원가입</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="회원가입"/>
<c:set var="backUrl" value="${pageContext.request.contextPath}/"/>
<%@ include file="../fragments/topbar.jspf" %>

<form:form modelAttribute="memberJoinRequest" action="${pageContext.request.contextPath}/members/join" method="post" cssClass="bk-form bk-form-tight">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <h2 style="margin-bottom:4px">회원 정보를<br>입력해주세요</h2>

    <form:errors path="" cssClass="bk-form-err" element="div"/>

    <div class="bk-field">
        <label for="jId">아이디</label>
        <form:input id="jId" path="userId" placeholder="영문·숫자 4~20자" cssClass="bk-input"/>
        <form:errors path="userId" cssClass="bk-err" element="span"/>
    </div>

    <div class="bk-field">
        <label for="jPw">비밀번호</label>
        <form:password id="jPw" path="password" placeholder="8~20자" cssClass="bk-input"/>
        <form:errors path="password" cssClass="bk-err" element="span"/>
    </div>

    <div class="bk-field">
        <label for="jPw2">비밀번호 확인</label>
        <form:password id="jPw2" path="passwordConfirm" placeholder="비밀번호를 한 번 더 입력" cssClass="bk-input"/>
        <form:errors path="passwordConfirm" cssClass="bk-err" element="span"/>
    </div>

    <div class="bk-field">
        <label for="jName">이름</label>
        <form:input id="jName" path="userName" placeholder="실명" cssClass="bk-input"/>
        <form:errors path="userName" cssClass="bk-err" element="span"/>
    </div>

    <div class="bk-field">
        <label for="jEmail">이메일</label>
        <form:input id="jEmail" path="email" placeholder="name@example.com" cssClass="bk-input"/>
        <form:errors path="email" cssClass="bk-err" element="span"/>
    </div>

    <button type="submit" class="bk-btn bk-btn-primary" style="margin-top:6px">가입하기
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M5 12h14"></path><path d="m12 5 7 7-7 7"></path></svg></button>
</form:form>

</div>
</div>
</body>
</html>
