<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
</head>
<body>
<h1>회원가입</h1>

<form:form modelAttribute="memberJoinRequest" action="/members/join" method="post">

    <form:errors path="" cssStyle="color:red"/>

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

    <div>
        <label>비밀번호 확인</label>
        <form:password path="passwordConfirm"/>
        <form:errors path="passwordConfirm" cssStyle="color:red"/>
    </div>

    <div>
        <label>이름</label>
        <form:input path="userName"/>
        <form:errors path="userName" cssStyle="color:red"/>
    </div>

    <div>
        <label>이메일</label>
        <form:input path="email"/>
        <form:errors path="email" cssStyle="color:red"/>
    </div>

    <button type="submit">가입하기</button>
</form:form>

<a href="/members/login">이미 계정이 있으신가요?</a>
</body>
</html>