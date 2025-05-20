<%@ include file="./common/header.jspf" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<head>
    <title>Upload Bank Statement</title>
</head>
<body>
    <h2>Upload Bank Statement (CSV)</h2>
    <form method="post" action="${pageContext.request.contextPath}/api/ledger/upload" enctype="multipart/form-data">
        <input type="file" name="file" accept=".csv" required />
        <sec:csrfInput />

        <label for="startDt" class="col-md-3">Start date:</label>
        <input name="startDt" id="startDt" class="col-md-3" required="true"/>

        <label for="endDt" class="col-md-3">End date:</label>
        <input name="endDt" id="endDt" class="col-md-3" required="true"/>


        <button type="submit">Upload</button>
    </form>
</body>
<%@ include file="./common/footer.jspf" %>
