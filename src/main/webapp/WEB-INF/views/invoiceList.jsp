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
    <form:form method="post">
    <table class="table">
        <thead>
        <tr>
            <th style="width: 15%">From Date</th>
            <th style="width: 15%">To Date</th>
            <th style="width: 15%">Invoice No</th>
            <th style="width: 15%">Challan No</th>
            <th style="width: 25%">Company Name</th>
            <th style="width: 15%"></th>
        </tr>
        </thead>
        <tbody>
            <td><input id="fromDate" name="fromDate" readonly="readonly" class="form-control" placeholder="Select From date"/></td>
            <td><input id="toDate" name="toDate" readonly="readonly" class="form-control" placeholder="Select To date"/></td>
            <td><input id="invoiceNo" name="invoiceNo" class="form-control" placeholder="Enter Invoice No"/></td>
            <td><input id="challanNo" name="challanNo" class="form-control" placeholder="Enter Challan No"/></td>
            <td><input id="companyId" name="companyId" type="hidden"/><input name="companyName" placeholder="Enter Company Name" onkeyup="billToPartyAutoComplete(event,this);" class="form-control"/></td>
            <td><input id="invoiceReport" value="search" type="submit" class="btn btn-primary"/></td>
        </tbody>
    <table>
    </form:form>
    <div class="row mb-1">
        <div class="col-md-12 fs-1 fw-bold" style="text-align:center;">List of Invoice</div>
    </div>
    <div class="filterDiv row">
        <c:choose>
            <c:when test="${isRedirect == 'YES'}">
                <div class="col-md-2">
                    Filters:
                </div>
                <div class="col-md-10">

                </div>
            </c:when>
            <c:otherwise>
                Showing the Last 20 invoices
            </c:otherwise>
        </c:choose>
    </div>
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
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
            <c:when test="${not empty invoices.getContent()}">
                <c:forEach items="${invoices.getContent()}" var="invoice" varStatus="index">
                <tr>
                    <td>${index.index + 1}</td>
                    <td>
                        <fmt:formatDate pattern="dd/MM/yyyy" value="${invoice.invoiceDate}" var="invDateFormatted"/>
                        <c:out value="${invDateFormatted}">NA</c:out>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/invoices/invoice/${invoice.invoiceId}">${invoice.invoiceNo}</a>
                        (<a href="${pageContext.request.contextPath}/invoices/printById/${invoice.invoiceId}" target="_blank">Print</a>)
                    </td>
                    <td>${invoice.billToPartyGst}</td>
                    <td>${invoice.billToPartyName}</td>
                    <td>${invoice.totalAmount}</td>
                    <td>${invoice.totalTaxAmount}</td>
                    <td>${invoice.roundOff}</td>
                    <td>${invoice.pnfCharge}</td>
                    <td>${invoice.totalAmountAfterTax}</td>
                    <td><input type="button" value="-" onclick="deleteByInvoiceNo('${invoice.invoiceNo}')" class="btn btn-sm btn-danger" /></td>
                </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <td colspan="10">
                    No Invoice Available
                    <a href="${pageContext.request.contextPath}/invoices/submit"> click here </a> to Add...
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
    $("#invoiceTable").DataTable({
        dom: 'Bfrtip',
        buttons: [
            'excelHtml5', 'csvHtml5', 'pdfHtml5'
        ]
    })
    $( "#fromDate" ).datepicker({
        dateFormat: 'dd/mm/yy'
    })
    $( "#toDate" ).datepicker({
        dateFormat: 'dd/mm/yy'
    })
})

function deleteByInvoiceNo(invNo) {
    var del = confirm("Do you want to delete '"+invNo+"' ?")
    if (del) {
        window.location.href="${pageContext.request.contextPath}/invoices/deleteByInvoiceNo?invoiceNo="+invNo
    }
}
    /**                 billToParty AutoComplete functions                  **/
function billToPartyAutoComplete(event,thisObj) {
    if (event.key == 'Enter') {
        return
    }
    $(thisObj).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/company/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    $("#companyId").val('')
                    response(data);
                },
                error : function(err) {
                    $("#companyId").val('')
                    console.error(err)
                }
            });
        },
        minLength: 4,
        select : function(event, ui) {
            this.value = ui.item.name
            $("#companyId").val(ui.item.id)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
}
</script>
<%@ include file="./common/footer.jspf" %>