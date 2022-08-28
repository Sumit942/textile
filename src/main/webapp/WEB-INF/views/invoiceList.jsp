<%@ include file="./common/header.jspf" %>
<style>
</style>
<body>
<div>
    <%@ include file="./common/navigation.jspf" %>
</div>
<div class="container-fluid">

    <table id="invoiceTable" class="table table-striped" style="width:100%;">
        <thead>
            <tr>
                <th>Sr.No</th>
                <th>Date</th>
                <th>Invoice No</th>
                <th>GSTIN</th>
                <th>Company Name</th>
                <th>Total Amount</th>
                <th>CGST+IGST(5%)</th>
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