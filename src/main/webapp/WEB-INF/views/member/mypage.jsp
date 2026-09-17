<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>마이페이지</title></head>
<body>
<h1>마이페이지</h1>

<table>
    <tr><th>아이디</th><td>${member.userId}</td></tr>
    <tr><th>이름</th><td>${member.userName}</td></tr>
    <tr><th>이메일</th><td>${member.email}</td></tr>
</table>

<h2>비밀번호 변경</h2>

<form:form modelAttribute="PasswordChangeRequest" action="/members/password" method="post">
    <form:errors path="" cssStyle="color:red"/>

    <div>
        <label>현재 비밀번호</label>
        <form:password path="currentPassword"/>
        <form:errors path="currentPassword" cssStyle="color:red"/>
    </div>

    <div>
        <label>새 비밀번호</label>
        <form:password path="newPassword"/>
        <form:errors path="newPassword" cssStyle="color:red"/>
    </div>

    <div>
        <label>새 비밀번호 확인</label>
        <form:password path="newPasswordConfirm"/>
        <form:errors path="newPasswordConfirm" cssStyle="color:red"/>
    </div>

    <button type="submit">변경하기</button>
</form:form>

<a href="/">홈으로</a>
</body>
</html>