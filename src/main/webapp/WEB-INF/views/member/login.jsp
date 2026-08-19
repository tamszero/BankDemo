<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
</head>
<body>
<h1>로그인</h1>

<form:form modelAttribute="loginRequest" action="/members/login" method="post">

    <input type="hidden" name="redirectURL" value="${redirectURL}"/>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <form:errors path="*" cssStyle="color:red"/>

    <div>
        <label>아이디</label>
        <form:input path="userId"/>
        <form:errors path="userId" cssStyle="color:red"/>
    </div>

    <div>
        <label>비밀번호</label>
        <form:password path="password"/>
        <form:errors path="password" cssStyle="color:red"/>
    </div>

    <button type="submit">로그인</button>
</form:form>

<a href="/members/join">계정이 없으신가요?</a>
</body>
</html>
