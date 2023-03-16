<%@ include file="./common/header.jspf" %>
<style>
</style>
<body>
<form:form name="employees" action="${pageContext.request.contextPath}/employees" method="POST" modelAttribute="employeeCommand">
<div class="container-fluid">
    <%@ include file="./common/navigation.jspf" %>
    <c:if test="${actionResponse.responseType == 'SUCCESS'}">
        <div class="row mb-1 alert alert-success" style="margin: 1%">
            <span>${successMessage}</span>
        </div>
    </c:if>
    <c:if test="${not empty deleted}">
        <div class="row mb-1 alert alert-danger" style="margin: 1%">
            <span>${deleted}</span>
        </div>
    </c:if>
    <div class="row mb-1">
        <div class="col-md-1 ">
            <form:label path="employee.firstName" >First Name</form:label>
        </div>
        <div class="col-md-2 ">
            <form:label path="employee.lastName" >Last Name</form:label>
        </div>
        <div class="col-md-2 ">
            <form:label path="employee.designation" >Designation</form:label>
        </div>
        <div class="col-md-2 ">
            <form:label path="employee.salary" >Salary</form:label>
        </div>
        <div class="col-md-2 ">
            <form:label path="employee.accountNo" >Account No</form:label>
        </div>
        <div class="col-md-2 ">
            <form:label path="employee.ifscCode" >IFSC Code</form:label>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-1 ">
            <form:hidden path="employee.id"/>
            <form:input path="employee.firstName" required="true"/>
            <form:errors path="employee.firstName" cssClass="error" />
        </div>
        <div class="col-md-2 ">
            <form:input path="employee.lastName" required="true"/>
            <form:errors path="employee.lastName" cssClass="error" />
        </div>
        <div class="col-md-2 ">
            <form:select path="employee.designation" required="true">
                <form:option value="" label="Select"/>
                <form:options items="${designations}" itemValue="id" itemLabel="designation" />
            </form:select>
            <form:errors path="employee.designation" cssClass="error"/>
        </div>
        <div class="col-md-2 ">
            <form:input path="employee.salary" required="true"/>
            <form:errors path="employee.salary" cssClass="error" />
        </div>
        <div class="col-md-2 ">
            <form:input path="employee.bankDetail[0].bankName" required="true"/>
            <form:errors path="employee.bankDetail[0].bankName" cssClass="error" />
        </div>
        <div class="col-md-2 ">
            <form:input path="employee.bankDetail[0].branch" required="true"/>
            <form:errors path="employee.bankDetail[0].branch" cssClass="error" />
        </div>
        <div class="col-md-2 ">
            <form:input path="employee.bankDetail[0].accountNo" required="true"/>
            <form:errors path="employee.bankDetail[0].accountNo" cssClass="error" />
        </div>
        <div class="col-md-2 ">
            <form:input path="employee.bankDetail[0].ifscCode" required="true"/>
            <form:errors path="employee.bankDetail[0].ifscCode" cssClass="error" />
        </div>
        <div class="col-md-1 ">
            <input type="Submit" class="btn btn-primary" />
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-3">
            <form:textarea path="employee.address.address" class="col-md-12" placeholder="Enter Address" required="true"/>
            <form:errors path="employee.address.address" cssClass="error" />
        </div>
        <div class="col-md-2">
            <form:input path="employee.address.pinCode" placeholder="Pincode" required="true"/>
            <form:errors path="employee.address.pinCode" cssClass="error" />
        </div>
        <div class="col-md-2">
            <form:select path="employee.address.state.name" placeholder="State Name" required="true">
                <form:option value="">Select State</form:option>
                <form:options items="${states}" itemValue="id" itemLabel="name" />
            </form:select>
            <form:errors path="employee.address.state.name" cssClass="error" />
        </div>

    </div>

    <!-- employee tables -->
    <table id="invoiceTable" class="table table-striped">
            <thead>
                <tr>
                    <th>S.No</th>
                    <th>Date</th>
                    <th>Name</th>
                    <th>Designation</th>
                    <th>Salary</th>
                    <th>Acc No - Ifsc</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                <c:when test="${not empty employees}">
                    <c:forEach items="${employees}" var="employeeList" varStatus="index">
                    <tr>
                        <td>${index.index + 1}</td>
                        <td>
                            <fmt:formatDate pattern="dd/MM/yyyy" value="${employeeList.insertDt}" var="invDateFormatted"/>
                            <c:out value="${invDateFormatted}">NA</c:out>
                        </td>
                        <td>${employeeList.firstName} ${employeeList.lastName}</td>
                        <td>${employeeList.designation.designation}</td>
                        <td>${employeeList.salary}</td>
                        <td>${employeeList.accountNo} - ${employeeList.ifscCode}</td>
                        <td><input type="button" value="-" onclick="deleteByEmployeeId('${employeeList.id}')" class="btn btn-sm btn-danger" /></td>
                    </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <td colspan="7">
                        No Data Available
                    </td>
                </c:otherwise>
                </c:choose>
            </tbody>
        </table>

</div>
</form:form>
<script>
function deleteByEmployeeId(){
//TODO: write ajax to delete
}
</script>
<%@ include file="./common/footer.jspf" %>