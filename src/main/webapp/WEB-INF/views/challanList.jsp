<%@ include file="./common/header.jspf" %>
<style>
</style>
<body>
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
    <table id="challanTable" class="table table-striped">
        <thead>
            <tr>
                <th>S.No</th>
                <th>Date</th>
                <th>Challan No</th>
                <th>Type</th>
                <th>Party Name</th>
                <th>Quality</th>
                <th>Machine</th>
                <th>Gsm</th>
                <th>Finish Dia</th>
                <th>Quantity</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
            <c:when test="${not empty challans.getContent()}">
                <c:forEach items="${challans.getContent()}" var="challan" varStatus="index">
                <tr>
                    <td>${index.index + 1}</td>
                    <td>
                        <fmt:formatDate pattern="dd/MM/yyyy" value="${challan.chDate}" var="chDateFormatted"/>
                        <c:out value="${chDateFormatted}">NA</c:out>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/challan/${challan.challanNo}">${challan.fullChallanNo}</a>
                    </td>
                    <td>${challan.type}</td>
                    <td>${challan.partyName.name}</td>
                    <td>${challan.fabricDesign.design}</td>
                    <td>${challan.machine.machineNo}</td>
                    <td>${challan.gsm}</td>
                    <td>${challan.finishDia}</td>
                    <td>${challan.quantity}</td>
                    <td><input type="button" value="-" onclick="deleteByChallanNo('${challan.challanNo}')" class="btn btn-sm btn-danger" /></td>
                </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <td colspan="10">
                    No challan Available
                    <a href="${pageContext.request.contextPath}/challan"> click here </a> to Add...
                </td>
            </c:otherwise>
            </c:choose>
        </tbody>
        <tfoot>
            <td colspan="10"><a href="${pageContext.request.contextPath}/challans/submit" class="btn btn-primary float-end" target="_blank">Add</a></td>
        </tfoot>
    </table>
</div>
<script>
$(document).ready(function(e){
    $("#challanTable").DataTable()
})

function deleteByChallanNo(chNo) {
    var del = confirm("Do you want to delete '"+chNo+"' ?")
    if (del) {
        window.location.href="${pageContext.request.contextPath}/challans/deleteBychallanNo?challanNo="+invNo
    }
}
</script>
<%@ include file="./common/footer.jspf" %>