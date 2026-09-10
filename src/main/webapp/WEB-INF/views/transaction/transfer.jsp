<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>계좌 이체</title></head>
<body>
<h1>계좌 이체</h1>

<form:form modelAttribute="transferRequest" action="/transactions/transfer/confirm" method="post">
    <form:errors path="" cssStyle="color:red"/>

    <div>
        <label>출금 계좌</label>
        <form:select path="fromAccountId">
            <form:option value="" label="-- 선택 --"/>
            <c:forEach var="acc" items="${accounts}">
                <form:option value="${acc.id}">
                    ${acc.accountNumber} (<fmt:formatNumber value="${acc.balance}" pattern="#,###"/>원)
                </form:option>
            </c:forEach>
        </form:select>
        <form:errors path="fromAccountId" cssStyle="color:red"/>
    </div>

    <div>
        <label>받는 분 계좌번호</label>
        <form:input path="toAccountNumber"/>
        <form:errors path="toAccountNumber" cssStyle="color:red"/>
    </div>

    <div>
        <label>이체 금액</label>
        <form:input path="amount" type="number"/>
        <form:errors path="amount" cssStyle="color:red"/>
    </div>

    <div>
        <label>계좌 비밀번호</label>
        <form:password path="password"/>
        <form:errors path="password" cssStyle="color:red"/>
    </div>

    <button type="submit">다음</button>
</form:form>
</body>
</html>