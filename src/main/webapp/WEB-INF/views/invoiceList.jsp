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
    <table id="invoiceTable" class="table table-striped">
        <thead>
            <tr>
                <th>S.No</th>
                <th>Date</th>
                <th>Invoice No</th>
                <th>GSTIN</th>
                <th>Company Name</th>
                <th>Total Amount</th>
                <th>GST</th>
                <th>Round Off</th>
                <th>P&F</th>
                <th>Total</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
            <c:when test="${not empty invoices}">
                <c:forEach items="${invoices}" var="invoice" varStatus="index">
                <tr>
                    <td>${index.index + 1}</td>
                    <td>
                        <fmt:formatDate pattern="dd/MM/yyyy" value="${invoice.invoiceDate}" var="invDateFormatted"/>
                        <c:out value="${invDateFormatted}">NA</c:out>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/invoices/invoice/${invoice.invoiceId}" target="_blank">${invoice.invoiceNo}</a>
                        (<a href="${pageContext.request.contextPath}/invoices/printById/${invoice.invoiceId}" target="_blank">Print</a>)
                    </td>
                    <td>${invoice.billToPartyGst}</td>
                    <td>${invoice.billToPartyName}</td>
                    <td>${invoice.totalAmount}</td>
                    <td>${invoice.totalTaxAmount}</td>
                    <td>${invoice.roundOff}</td>
                    <td>${invoice.pnfCharge}</td>
                    <td>${invoice.totalAmountAfterTax}</td>
                </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <td colspan="10">
                    No Invoice Available
                    <a href="${pageContext.request.contextPath}/invoices/submit" target="_blank"> click here </a> to Add...
                </td>
            </c:otherwise>
            </c:choose>
        </tbody>
        <tfoot>
            <td colspan="10"><a href="${pageContext.request.contextPath}/invoices/submit" class="btn btn-primary float-end" target="_blank">Add</a></td>
        </tfoot>
    </table>
</div>
<script>
$(document).ready(function(e){
    $("#invoiceTable").DataTable()
})
</script>
<%@ include file="./common/footer.jspf" %>