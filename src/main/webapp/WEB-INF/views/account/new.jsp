<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>계좌 개설</title>
</head>
<body>
<h1>계좌 개설</h1>

<form:form modelAttribute="accountCreateRequest" action="/accounts/new" method="post">

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <form:errors path="" cssStyle="color:red"/>

    <div>
        <label>계좌 비밀번호 (숫자 4자리)</label>
        <form:password path="password"/>
        <form:errors path="password" cssStyle="color:red"/>
    </div>

    <div>
        <label>비밀번호 확인</label>
        <form:password path="passwordConfirm"/>
        <form:errors path="passwordConfirm" cssStyle="color:red"/>
    </div>

    <button type="submit">개설하기</button>
</form:form>

<a href="/accounts">목록으로</a>
</body>
</html>