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
            <th style="width: 9%">Payment Status</th>
            <th style="width: 6%"></th>
        </tr>
        </thead>
        <tbody>
            <td><input id="fromDate" name="fromDate" readonly="readonly" class="form-control" placeholder="Select From date"/></td>
            <td><input id="toDate" name="toDate" readonly="readonly" class="form-control" placeholder="Select To date"/></td>
            <td><input id="invoiceNo" name="invoiceNo" class="form-control" placeholder="Enter Invoice No"/></td>
            <td><input id="challanNo" name="challanNo" class="form-control" placeholder="Enter Challan No"/></td>
            <td><input id="companyId" name="companyId" type="hidden"/><input name="companyName" placeholder="Enter Company Name" onkeyup="billToPartyAutoComplete(event,this);" class="form-control"/></td>
            <td>
                <select id="paymentStatus" name="paymentStatus" class="form-select">
                    <option value="">-Select-</option>
                    <option value="false">UnPaid</option>
                    <option value="true" >Paid</option>
                </select>
            </td>
            <td>
                <input name="showInvoiceReport" value="search" type="submit" class="btn btn-primary"/>
                <input name="downloadInvoiceReport" value="Download" type="submit" class="btn btn-primary"/>
            </td>

        </tbody>
    <table>
    </form:form>
    <div class="row mb-1">
        <div class="col-md-12 fs-1 fw-bold" style="text-align:center;">List of Invoice</div>
    </div>
    <div class="filterDiv row border">
        <c:choose>
            <c:when test="${isRedirect == 'YES'}">
                <div class="col-md-12">
                    <span><b>Filters:</b></span>
                </div>
                <c:if test="${not empty fromDate}">
                    <div class="col-md-2">
                        <span><b>From Date:</b> <fmt:formatDate value="${fromDate}" type="date" pattern="dd-MMM-yyyy"/></span>
                    </div>
                </c:if>
                <c:if test="${not empty toDate}">
                    <div class="col-md-2">
                        <span><b>To Date:</b> <fmt:formatDate value="${toDate}" type="date" pattern="dd-MMM-yyyy"/></span>
                    </div>
                </c:if>
                <c:if test="${not empty invoiceNo}">
                    <div class="col-md-2">
                        <span><b>Invoice No:</b> ${invoiceNo}</span>
                    </div>
                </c:if>
                <c:if test="${not empty challanNo}">
                    <div class="col-md-2">
                        <span><b>Challan No:</b> ${challanNo}</span>
                    </div>
                </c:if>
                <c:if test="${not empty companyName}">
                    <div class="col-md-4">
                        <span><b>Company Name :</b> ${companyName}</span>
                    </div>
                </c:if>
                <c:if test="${not empty paymentStatus}">
                    <div class="col-md-4">
                        <span><b>Payment Status :</b> ${paymentStatus ? 'Paid' : 'UnPaid'}</span>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <span class="col-md-12 text-center">Showing the Last 20 invoices<span>
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
                <th>Total</th>
                <th>Pymt Date/Status</th>
                <th>Amt Cr</th>
                <th>Amt Dr</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
            <c:when test="${not empty invoices.getContent()}">
                <c:forEach items="${invoices.getContent()}" var="invoice" varStatus="index">
                <tr id="tr${index.index}" style="${invoice.paid ? 'background: lightgreen;' : ''}">
                    <td>${index.index + 1}</td>
                    <td>
                        <fmt:formatDate pattern="dd/MM/yyyy" value="${invoice.invoiceDate}" var="invDateFormatted"/>
                        <input id="invoice${index.index}.invoiceDate" name="invoice[${index.index}].invoiceDate" value="${invDateFormatted}" class="form-control invoiceDate" readonly/>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/invoices/invoice/${invoice.invoiceId}">${invoice.invoiceNo}</a>
                        (<a href="${pageContext.request.contextPath}/invoices/printById/${invoice.invoiceId}" target="_blank">Print</a>)
                    </td>
                    <td>${invoice.billToPartyGst}</td>
                    <td>${invoice.billToPartyName}</td>
                    <td>${invoice.totalAmount}</td>
                    <td>${invoice.totalAmountAfterTax}</td>
                    <td>
                    <select id="invoice${index.index}.paid" name="invoice[${index.index}].paid" class="form-select paidClass" onchange="autoSetAmtCr(${index.index})">
                        <option value="false">UnPaid</option>
                        <option value="true" ${invoice.paid ? "selected='selected'" : ""}>Paid</option>
                    </select>
                    <span>
                        <fmt:formatDate pattern="dd/MM/yyyy" value="${invoice.paymentDt}" var="paymentDtFmt"/>
                        <input id="invoice${index.index}.paymentDt" name="invoice[${index.index}].paymentDt" value="${paymentDtFmt}" class="form-control paymentDt" readonly/>
                    </span>
                    </td>
                    <td><input id="invoice${index.index}.paidAmount" name="invoice[${index.index}].paidAmount" value="${invoice.paidAmount}" class="form-control" /></td>
                    <td><input id="invoice${index.index}.amtDr" name="invoice[${index.index}].amtDr" value="${invoice.amtDr}" class="form-control" /></td>
                    <td>
                    <input type="button" value="Delete" onclick="deleteByInvoiceNo('${invoice.invoiceNo}')" class="btn btn-sm btn-danger" />
                    <input type="button" value="Update" onclick="updateInvoice(${invoice.invoiceId},${index.index},'${invoice.invoiceNo}')" class="btn btn-sm btn-primary" />
                    </td>
                </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <td colspan="11">
                    No Invoice Available
                    <a href="${pageContext.request.contextPath}/invoices/submit"> click here </a> to Add...
                </td>
            </c:otherwise>
            </c:choose>
        </tbody>
        <tfoot>
            <td colspan="11"><a href="${pageContext.request.contextPath}/invoices/submit" class="btn btn-primary float-end" target="_blank">Add</a></td>
        </tfoot>
    </table>
    <input type="hidden" id="listSize" value="${invoices.getContent().size()}" />
</div>
<script>
$(document).ready(function(e){
    if ( $('#listSize').val() != '') {
        $("#invoiceTable").DataTable({
            dom: 'Bfrtip',
            buttons: [
                'excelHtml5', 'csvHtml5', 'pdfHtml5'
            ]
        })
    }
    $("#invoiceTable").on('draw.dt',function(){
        console.log('datepicker initialized')
        $('.paymentDt').datepicker({dateFormat: 'dd/mm/yy'});
        $('.invoiceDate').datepicker({dateFormat: 'dd/mm/yy'});
    })

    $( "#fromDate, #toDate, .paymentDt, .invoiceDate" ).datepicker({
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

/**  update invoice date and payment details function   **/
function updateInvoice(invId, index, invNo) {
    var invDt = $('#invoice'+index+'\\.invoiceDate').val();
    var paidDt = $('#invoice'+index+'\\.paymentDt').val();
    var paidStatus = $('#invoice'+index+'\\.paid').val();
    var paidAmount = $('#invoice'+index+'\\.paidAmount').val();
    var amtDebit = $('#invoice'+index+'\\.amtDr').val();
    $.ajax({
        url : "${pageContext.request.contextPath}/invoices/update",
        type : 'PATCH',
        data : {
            invoiceId : invId,
            invoiceDt : invDt,
            paymentDt : paidDt,
            paymentStatus : paidStatus,
            paidAmount : paidAmount,
            amtDr : amtDebit,
            _csrf : $('input[name="_csrf"]').val()
        },
        success : function(data) {
            if ( paidStatus == 'true' ) {
                $('#tr'+index).css("background-color","rgba(144, 238, 144)")
            } else {
                $('#tr'+index).css("background-color","rgba(0, 0, 0, 0)")
            }
            alert('Invoice: '+invNo+' details updated successfully!!')
        },
        error : function(err) {
            alert('Failed to Update Invoice: '+invNo+' Details')
            console.error(err)
        }
    });
}

function autoSetAmtCr(index) {
    if ( $('#invoice'+index+'\\.paid').val() == 'true') {
        $('#invoice'+index+'\\.paidAmount').val( $('#tr'+index).find('td:eq(6)').text() )
    } else {
        $('#invoice'+index+'\\.paidAmount').val('0.00')
    }
}

</script>
<%@ include file="./common/footer.jspf" %>