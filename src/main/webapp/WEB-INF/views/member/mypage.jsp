<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>마이페이지</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-main">
<c:set var="pageTitle" value="마이페이지"/>
<%@ include file="../fragments/topbar.jspf" %>

<c:if test="${not empty message}">
    <div class="bk-flash bk-flash-ok"><span class="bk-flash-dot"></span>${message}</div>
</c:if>

<div style="padding:26px 20px 22px;display:flex;flex-direction:column;gap:4px">
    <span style="font:800 32px/1.1 var(--font-heading);letter-spacing:-0.02em">${member.userName}</span>
    <span style="font-size:14px;color:var(--color-neutral-700)">${member.email}</span>
</div>

<div class="bk-divider2">
    <div class="bk-kv"><span class="k">아이디</span><span class="v-strong">${member.userId}</span></div>
    <div class="bk-kv"><span class="k">이름</span><span class="v-strong">${member.userName}</span></div>
    <div class="bk-kv" style="border-bottom:2px solid var(--color-divider)"><span class="k">이메일</span><span class="v-strong">${member.email}</span></div>
</div>

<div style="padding:22px 20px 8px;font:800 18px var(--font-heading)">보안</div>

<div style="padding:18px 20px 20px;display:flex;flex-direction:column;gap:14px;background:var(--color-neutral-100);border-top:1px solid var(--color-divider);border-bottom:1px solid var(--color-divider)">
    <span style="font:600 15px var(--font-body)">비밀번호 변경</span>

    <form:form modelAttribute="PasswordChangeRequest" action="${pageContext.request.contextPath}/members/password" method="post" cssClass="bk-form-tight" style="display:flex;flex-direction:column;gap:14px">
        <form:errors path="" cssClass="bk-form-err" element="div"/>

        <div class="bk-field">
            <label for="cpCur">현재 비밀번호</label>
            <form:password id="cpCur" path="currentPassword" cssClass="bk-input" style="height:48px"/>
            <form:errors path="currentPassword" cssClass="bk-err" element="span"/>
        </div>

        <div class="bk-field">
            <label for="cpNew">새 비밀번호</label>
            <form:password id="cpNew" path="newPassword" placeholder="8~20자" cssClass="bk-input" style="height:48px"/>
            <form:errors path="newPassword" cssClass="bk-err" element="span"/>
        </div>

        <div class="bk-field">
            <label for="cpNew2">새 비밀번호 확인</label>
            <form:password id="cpNew2" path="newPasswordConfirm" cssClass="bk-input" style="height:48px"/>
            <form:errors path="newPasswordConfirm" cssClass="bk-err" element="span"/>
        </div>

        <button type="submit" class="bk-btn bk-btn-primary" style="height:48px;font-size:15px">변경하기</button>
    </form:form>
</div>

<div style="padding:28px 20px 0">
    <form action="${pageContext.request.contextPath}/members/logout" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="bk-btn bk-btn-secondary" style="height:52px;font-size:15px">로그아웃
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="square"><path d="M9 21H4V3h5"></path><path d="m16 17 5-5-5-5"></path><path d="M21 12H9"></path></svg></button>
    </form>
</div>

</div>
<c:set var="activeTab" value="mypage"/>
<%@ include file="../fragments/tabbar.jspf" %>
</div>
</body>
</html>
