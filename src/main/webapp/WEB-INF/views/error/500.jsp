<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>오류</title>
    <%@ include file="../fragments/head.jspf" %>
</head>
<body>
<div class="bk-app">
<div class="bk-simple">
    <h1>일시적인 오류가<br>발생했습니다</h1>
    <p style="margin:0;font-size:14px;color:var(--color-neutral-700)">${message}</p>
    <a class="bk-btn bk-btn-primary" style="width:auto;padding:0 20px" href="${pageContext.request.contextPath}/">홈으로</a>
</div>
</div>
</body>
</html>
