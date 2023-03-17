<%@ include file="./common/header.jspf" %>
<style>
</style>
<body>
<div class="container-fluid">
<form:form name="employees" action="${pageContext.request.contextPath}/employees" method="POST" modelAttribute="employeeCommand">

<%@ include file="./common/navigation.jspf" %>
  <div class="row">
    <div class="col text-center mb-1 fw-bold">Employee Details</div>
  </div>
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

    <div class="row">
    <div class="col">
      <form:hidden path="employee.id"/>
      <form:label path="employee.firstName" >First Name</form:label>
      <form:input path="employee.firstName" class="form-control" placeholder="First name" required="true"/>
      <form:errors path="employee.firstName" cssClass="error" />
    </div>
    <div class="col">
      <form:label path="employee.lastName" >Last Name</form:label>
      <form:input path="employee.lastName" class="form-control" placeholder="Last name" required="true"/>
      <form:errors path="employee.lastName" cssClass="error" />
    </div>
    <div class="col">
      <form:label path="employee.panCardNo" >Pan No.</form:label>
      <form:input path="employee.panCardNo" class="form-control" placeholder="Pan No" required="true"/>
      <form:errors path="employee.panCardNo" cssClass="error" />
    </div>
    <div class="col">
      <form:label path="employee.salary" >Salary</form:label>
      <form:input path="employee.salary" class="form-control" placeholder="Salary" required="true"/>
      <form:errors path="employee.salary" cssClass="error" />
    </div>
    <div class="col">
      <form:label path="employee.designation" >Designation</form:label>
      <form:select path="employee.designation" class="form-control" required="true">
          <form:option value="" label="--Select--"/>
          <form:options items="${designations}" itemValue="id" itemLabel="designation" />
      </form:select>
      <form:errors path="employee.designation" cssClass="error"/>
    </div>
    <div class="col">
      <form:label path="employee.status" >Status</form:label>
      <form:select path="employee.status" class="form-control" placeholder="Status" required="true">
          <form:option value="Active">Active</form:option>
          <form:option value="InActive">InActive</form:option>
      </form:select>
      <form:errors path="employee.status" cssClass="error" />
    </div>
    </div>
    <div class="row">
    <div class="col-md-6">
      <form:label path="employee.address.address">Address</form:label>
      <form:textarea path="employee.address.address" class="form-control" placeholder="Enter Address" required="true"/>
      <form:errors path="employee.address.address" cssClass="error" />
    </div>
    <div class="col-md-2">
      <form:label path="employee.address.pinCode">Pincode</form:label>
      <form:input path="employee.address.pinCode" class="form-control" placeholder="Pincode" required="true"/>
      <form:errors path="employee.address.pinCode" cssClass="error" />
    </div>
    <div class="col-md-4">
      <form:label path="employee.address.state.id" >State</form:label>
      <form:select path="employee.address.state.id" class="form-control" placeholder="State" required="true">
          <form:option value="" label="--Select--"/>
          <form:options items="${states}" itemValue="id" itemLabel="name" />
      </form:select>
      <form:errors path="employee.address.state.id" cssClass="error" />
    </div>
  </div>
<table class="table" id="bankDetailTable">
<thead>
        <tr>
          <th>Sr.No</th>
          <th>Bank Name</th>
          <th>Branch</th>
          <th>Account No</th>
          <th>IFSC</th>
          <th></th>
        </tr>
    </thead>
    <tbody id="bankDetailTbody">
<c:choose>
        <c:when test="${empty employee.bankDetails}">
        <tr>
            <td>1</td>
            <td>
                <form:input path="employee.bankDetails[0].bankName" class="form-control" placeholder="Enter Bank Name" required="true"/>
                <form:errors path="employee.bankDetails[0].bankName" cssClass="error" />
            </td>
            <td>
                <form:input path="employee.bankDetails[0].branch" class="form-control"  placeholder="Enter Bank Branch" required="true"/>
                <form:errors path="employee.bankDetails[0].branch" cssClass="error" />
            </td>
            <td>
                <form:input path="employee.bankDetails[0].accountNo" class="form-control"  placeholder="Enter Bank Account No" required="true"/>
                <form:errors path="employee.bankDetails[0].accountNo" cssClass="error" />
            </td>
            <td>
                <form:input path="employee.bankDetails[0].ifsc" class="form-control"  placeholder="Enter Bank IFSC" required="true"/>
                <form:errors path="employee.bankDetails[0].ifsc" cssClass="error" />
            </td>
            <td>
                <input type="button" value="+" onclick="bankDetailAddRow(this)" class="btn btn-sm btn-primary" />
            </td>
        </tr>
        </c:when>
        <c:otherwise>
            <c:forEach items="${employee.bankDetails}" var="bankDetails" varStatus="index">
            <tr>
            <td>
                <form:input path="employee.bankDetails[${index.index}].bankName" class="col-md-2 " placeholder="Enter Bank Name" required="true"/>
                <form:errors path="employee.bankDetails[0].bankName" cssClass="error" />
            </td>
            <td>
                <form:input path="employee.bankDetails[${index.index}].branch" class="col-md-2 " placeholder="Enter Bank Branch" required="true"/>
                <form:errors path="employee.bankDetails[${index.index}].branch" cssClass="error" />
            </td>
            <td>
                <form:input path="employee.bankDetails[${index.index}].accountNo" class="col-md-2 " placeholder="Enter Bank Account No" required="true"/>
                <form:errors path="employee.bankDetails[${index.index}].accountNo" cssClass="error" />
            </td>
            <td>
                <form:input path="employee.bankDetails[${index.index}].ifsc" class="col-md-2 " placeholder="Enter Bank IFSC" required="true"/>
                <form:errors path="employee.bankDetails[${index.index}].ifsc" cssClass="error" />
            </td>
            <td>
                <c:if test="${index.index == employee.bankDetails.size()-1}">
                    <input type="button" value="+" id="bankDetailAdd_${index.index}" class="btn btn-sm btn-primary" onclick="bankDetailAddRow(this)"/>
                    <input  type="button" value="-" id="bankDetailDel_${index.index}" class="btn btn-sm btn-danger rounded" onclick="bankDetailDelRow(this)" style="margin-left: 18%;width: 60%;"/>
                </c:if>
            </td>
            </tr>
            </c:forEach>
        </c:otherwise>
        </c:choose>
   </tbody>
</table>
<input type="Submit" class="btn btn-primary" />
</form:form>
</div>
<script>
function deleteByEmployeeId(){
//TODO: write ajax to delete
}

function bankDetailAddRow(element) {

let index = $("#bankDetailTbody > tr").length-1
const bankName = '#employee\\.bankDetails'+index+'\\.bankName'
const branch = '#employee\\.bankDetails'+index+'\\.branch'
const accountNo = '#employee\\.bankDetails'+index+'\\.accountNo'
const ifsc = '#employee\\.bankDetails'+index+'\\.ifsc'

if ($(bankName).val() == '') {
    alert('Please enter Bank Name')
    $(bankName).focus()
    return;
}
if ($(branch).val() == '') {
    alert('Please enter Branch Name')
    $(branch).focus()
    return;
}
if ($(accountNo).val() == '') {
    alert('Please enter Account No')
    $(accountNo).focus()
    return;
}
if ($(ifsc).val() == '') {
    alert('Please enter IFSC')
    $(ifsc).focus()
    return;
}

let bankDetailRowHtml = `<tr>
                             <td>`+(index+2)+`</td>
                             <td>
                                 <input id="employee.bankDetails`+(index+1)+`.bankName" name="employee.bankDetails[`+(index+1)+`].bankName" placeholder="Enter Bank Name" class="form-control" required="true" type="text" value="">
                             </td>
                             <td>
                                 <input id="employee.bankDetails`+(index+1)+`.branch" name="employee.bankDetails[`+(index+1)+`].branch" placeholder="Enter Bank Branch" class="form-control" required="true" type="text" value="">
                             </td>
                             <td>
                                 <input id="employee.bankDetails`+(index+1)+`.accountNo" name="employee.bankDetails[`+(index+1)+`].accountNo" placeholder="Enter Bank Account No" class="form-control" required="true" type="text" value="">
                             </td>
                             <td>
                                 <input id="employee.bankDetails`+(index+1)+`.ifsc" name="employee.bankDetails[`+(index+1)+`].ifsc" placeholder="Enter Bank IFSC" class="form-control" required="true" type="text" value="">
                             </td>
                             <td>
                                 <input type="button" value="+" onclick="bankDetailAddRow(this)" class="btn btn-sm btn-primary">
                                 <input type="button" value="-" onclick="bankDetailDeleteRow(this)" class="btn btn-sm btn-danger">
                             </td>
                         </tr>`;

$("#bankDetailTbody > tr:eq("+index+")").find('td:eq(5)').text('')
$("#bankDetailTbody").append(bankDetailRowHtml)
}

function bankDetailDeleteRow(element) {
    let rowCount = $("#bankDetailTbody > tr").length
    if (rowCount > 1) {
        let del = confirm('Do you want to delete the "Bank Detail" row?')
        if (!del){
            return;
        }
        $("#bankDetailTbody > tr:last").remove()
        const htmlBtns = `<input type="button" value="+" onclick="bankDetailAddRow(this)" class="btn btn-sm btn-primary">
                          <input type="button" value="-" onclick="bankDetailDeleteRow(this)" class="btn btn-sm btn-danger">`
        $("#bankDetailTbody > tr:last > td:eq(5)").html(htmlBtns)
    }
}

</script>
<script src="${pageContext.request.contextPath}/js/employee.js" ></script>
<%@ include file="./common/footer.jspf" %>