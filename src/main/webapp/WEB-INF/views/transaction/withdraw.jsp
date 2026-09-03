<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>출금</title></head>
<body>
<h1>출금</h1>

<form:form modelAttribute="withdrawRequest" action="/transactions/withdraw" method="post">
    <form:errors path="" cssStyle="color:red"/>

    <div>
        <label>출금 계좌</label>
        <form:select path="accountId">
            <form:option value="" label="-- 선택 --"/>
            <c:forEach var="acc" items="${accounts}">
                <form:option value="${acc.id}">
                    ${acc.accountNumber} (<fmt:formatNumber value="${acc.balance}" pattern="#,###"/>원)
                </form:option>
            </c:forEach>
        </form:select>
        <form:errors path="accountId" cssStyle="color:red"/>
    </div>

    <div>
        <label>출금 금액</label>
        <form:input path="amount" type="number"/>
        <form:errors path="amount" cssStyle="color:red"/>
    </div>

    <div>
        <label>계좌 비밀번호</label>
        <form:password path="password"/>
        <form:errors path="password" cssStyle="color:red"/>
    </div>

    <button type="submit">출금하기</button>
</form:form>
</body>
</html>