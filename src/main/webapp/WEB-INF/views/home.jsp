<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>홈</title>
</head>
<body>
<h1>홈</h1>

<c:if test="${empty loginMember}">
    <p>비로그인 상태입니다.</p>
    <a href="/members/login">로그인</a>
    <a href="/members/join">회원가입</a>
</c:if>

<c:if test="${not empty loginMember}">
    <p>${loginMember.userName}님 환영합니다. (${loginMember.userId})</p>

    <form action="/members/logout" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit">로그아웃</button>
    </form>

    <form action="/accounts" method="get">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit">내 계좌</button>
        </form>
</c:if>



</body>
</html>