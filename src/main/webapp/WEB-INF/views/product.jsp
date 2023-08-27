<%@ include file="./common/header.jspf" %>
<style>
.error {
    color: #ff0000;
    font-size: 12px;
}
.errorblock {
     color: #000;
     background-color: #ffEEEE;
     border: 3px solid #ff0000;
     padding: 8px;
     margin: 16px;
}
.ui-autocomplete-loading {
    background: white url("http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.2/themes/smoothness/images/ui-anim_basic_16x16.gif") right center no-repeat;
}
</style>
<body>
<form:form name="challan" action="${pageContext.request.contextPath}/challan" method="POST" modelAttribute="productCommand">
<div class="container-fluid">
    <%@ include file="./common/navigation.jspf" %>
    <c:if test="${not empty actionResponse.errors}">
        <div class="row mb-1 alert alert-danger" style="margin: 1%">
            <ul>
                <c:forEach items="${actionResponse.errors}" var="errorMessage">
                    <li>${errorMessage}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <c:if test="${actionResponse.responseType == 'SUCCESS'}">
        <div class="row mb-1 alert alert-success" style="margin: 1%">
            challan ${challanCommand.challan.getchallanNo()} Save Successfully!!
        </div>
    </c:if>
    <div class="row mb-1">
        <div class="col-md-12 fs-1 fw-bold" style="text-align:center;">Yarn</div>
    </div>
    <table class="table table-striped table-bordered">
    <tr>
        <td>
            <form:hidden path="product.id"/>
            <form:input path="product.name" placeholder="Enter Product Name" onkeyup="autoSearchProduct(event, this,-1)" class="form-control ui-autocomplete-input" autocomplete="off"/>
        </td>
        <td>
            <input type="submit" value="search" name="searchProduct" class="btn btn-primary"/>
        </td>
    </tr>
    </table>

        <table id="productTable" class="table table-striped table-bordered">
            <thead>
                <tr>
                  <th>Yarn Quality</th>
                  <th>HSN</th>
                  <th>Status</th>
                  <th></th>
                </tr>
            </thead>
            <tbody id="productTBody">
            <tr>
            <td>
                <form:hidden path="product.id"/>
                <form:input path="product.name" required="true" class="form-control"/>
                <form:errors path="product.name" cssClass="error"/>
            </td>
            <td>
                <form:input path="product.hsn" class="numbersOnly form-control" required="true"/>
                <form:errors path="product.hsn" cssClass="error"/>
            </td>
            <td>
                <form:select path="product.active" class="form-select" required="true">
                    <form:option value="" label="--Select--"/>
                    <form:option value="true" label="Yes"/>
                    <form:option value="false" label="No"/>
                </form:select>
                <form:errors path="product.active" cssClass="error"/>
            </td>
            <td></td>
            </tr>
            </tbody>
            <tfoot>
                <tr>
                    <td><button type="button" class="btn btn-primary" onclick="resetRow()">Reset</button></td>
                    <td colspan="2"><button type="submit" name="saveChallans" class="btn btn-success">Submit</button></td>
                    <td></td>
                </tr>
            </tfoot>
        </table>
</div>
</form:form>
<%@ include file="./common/footer.jspf" %>