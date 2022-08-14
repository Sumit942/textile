<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- css -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">

    <!-- js -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>

</head>
<style>
.error {
    color: #ff0000;
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
<form:form action="submit" method="POST" modelAttribute="invoiceCommand">
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">Tax Invoice</div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:label path="invoiceNo">Invoice No:</form:label>
            <form:input path="invoiceNo" />
        </div>
        <div class="col-md-6">
            <form:label path="transportMode">Transport Mode:</form:label>
            <form:select path="transportMode">
                <form:options items="${transportModes}" itemValue="id" itemLabel="mode" />
            </form:select>
            <form:errors path="transportMode" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">

            <form:label path="invoiceDate">Invoice date:</form:label>
            <fmt:formatDate pattern="dd/MM/yyyy" value="${invoiceCommand.invoiceDate}" var="invoiceDateFormatted"/>
            <input name="invoiceDate" value="${invoiceDateFormatted}"/>
            <form:errors path="invoiceDate" cssClass="error"/>
        </div>
        <div class="col-md-6">
            <form:label path="vehicleNo">Vehicle Number:</form:label>
            <form:input path="vehicleNo" />
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:label path="reverseCharge">Reverse Charge (Y/N):</form:label>
            <form:select path="reverseCharge">
                <form:option value="NO" label="NO"/>
                <form:option value="YES" label="YES"/>
            </form:select>
            <form:errors path="reverseCharge" cssClass="error"/>
        </div>
        <div class="col-md-6">
            <form:label path="dateOfSupply">Date of Supply:</form:label>
            <form:input path="dateOfSupply" />
            <form:errors path="dateOfSupply" cssClass="error"/>
        </div>
    </div>
    <div class="row">
         <div class="col-md-6">
            <form:label path="invoiceBy.address.state.name">State:</form:label>
            <form:input path="invoiceBy.address.state.name" />
            <form:errors path="invoiceBy.address.state.name" cssClass="error"/>
         </div>
         <div class="col-md-6">
            <form:label path="placeOfSupply">Place of Supply:</form:label>
            <form:input path="placeOfSupply"/>
            <form:errors path="placeOfSupply" cssClass="error"/>
         </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <span>Bill to Party</span>
        </div>
        <div class="col-md-6">
            <span>Ship to Party</span>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:hidden path="billToParty.id" />
            <form:label path="billToParty.name">Name:</form:label>
            <form:input path="billToParty.name" />
            <form:errors path="billToParty.name" cssClass="error"/>
        </div>
        <div class="col-md-6">
            <form:hidden path="shipToParty.id" />
            <form:label path="shipToParty.name">Name:</form:label>
            <form:input path="shipToParty.name" />
            <form:errors path="shipToParty.name" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:hidden path="billToParty.address.id" />
            <form:label path="billToParty.address.address">Address:</form:label>
            <form:input path="billToParty.address.address" />
            <form:errors path="billToParty.address.address" cssClass="error"/>
            <form:label path="billToParty.address.pinCode">PinCode:</form:label>
            <form:input path="billToParty.address.pinCode" />
            <form:errors path="billToParty.address.pinCode" cssClass="error" />
        </div>
        <div class="col-md-6">
            <form:hidden path="shipToParty.address.id" />
            <form:label path="shipToParty.address.address">Address:</form:label>
            <form:input path="shipToParty.address.address" />
            <form:errors path="shipToParty.address.address" cssClass="error"/>
            <form:label path="shipToParty.address.pinCode">PinCode:</form:label>
            <form:input path="shipToParty.address.pinCode" />
            <form:errors path="shipToParty.address.pinCode" cssClass="error" />
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:label path="billToParty.gst">GSTIN:</form:label>
            <form:input path="billToParty.gst" />
            <form:errors path="billToParty.gst" cssClass="error"/>
        </div>
        <div class="col-md-6">
            <form:label path="shipToParty.gst">GSTIN:</form:label>
            <form:input path="shipToParty.gst" />
            <form:errors path="shipToParty.gst" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-3">
            <form:hidden path="billToParty.address.state.id" />
            <form:hidden path="billToParty.address.state.country.id" />
            <form:label path="billToParty.address.state.name">State:</form:label>
            <form:input path="billToParty.address.state.name" />
            <form:errors path="billToParty.address.state.name" cssClass="error"/>
        </div>
        <div class="col-md-3">
            <form:label path="billToParty.address.state.code">Code:</form:label>
            <form:input path="billToParty.address.state.code" />
            <form:errors path="billToParty.address.state.code" cssClass="error"/>
        </div>
        <div class="col-md-3">
            <form:label path="shipToParty.address.state.name">State:</form:label>
            <form:input path="shipToParty.address.state.name" />
            <form:errors path="shipToParty.address.state.name" cssClass="error"/>
        </div>
        <div class="col-md-3">
            <form:label path="shipToParty.address.state.code">Code:</form:label>
            <form:input path="shipToParty.address.state.code" />
            <form:errors path="shipToParty.address.state.code" cssClass="error"/>
        </div>
    </div>

    <table class="table">
    <thead>
        <tr>
          <th scope="col">Sr.No</th>
          <th scope="col">Product Description</th>
          <th scope="col">Challan No</th>
          <th scope="col">HSN code</th>
          <th scope="col">UOM</th>
          <th scope="col">Qty</th>
          <th scope="col">Rate</th>
          <th scope="col">Amount</th>
        </tr>
    </thead>
    <tbody>
    <tr>
        <td></td>
        <td colspan="2">
            <form:select path="saleType.id" >
                <form:options items="${saleTypes}" itemValue="id" itemLabel="saleType" />
            </form:select>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <c:choose>
    <c:when test="${empty invoiceCommand.product}">
        <tr>
            <td>
                <span id="product[0].srNo">1</span>
                <form:hidden path="product[0].id" />
            </td>
            <td>
                <input type="hidden" name="product[0].product.id" />
                <form:input path="product[0].product.name" />
                <form:errors path="product[0].product.name" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].chNo" />
                <form:errors path="product[0].chNo" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].product.hsn" />
                <form:errors path="product[0].product.hsn" cssClass="error"/>
            </td>
            <td>
                <form:select path="product[0].unitOfMeasure.id">
                    <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure" itemValue="id"/>
                </form:select>
                <form:errors path="product[0].unitOfMeasure" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].quantity" />
                <form:errors path="product[0].quantity" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].rate" />
                <form:errors path="product[0].rate" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].totalPrice" />
                <form:errors path="product[0].totalPrice" cssClass="error"/>
            </td>
        </tr>
    </c:when>
    <c:otherwise>
        <c:forEach items="product" varStatus="index">
            <tr>
                <td>
                    <span id="product[${index.index}].srNo">${index.index + 1}</span>
                    <form:hidden path="product[${index.index}].id" />
                </td>
                <td>
                    <form:hidden path="product[${index.index}].product.id" />
                    <form:input path="product[${index.index}].product.name" />
                    <form:errors path="product[${index.index}].product.name" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].chNo" />
                    <form:errors path="product[${index.index}].chNo" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].product.hsn" />
                    <form:errors path="product[${index.index}].product.hsn" cssClass="error"/>
                </td>
                <td>
                    <form:select path="product[${index.index}].unitOfMeasure.id">
                        <form:options items="${unitOfMeasures}" itemValue="id" itemLabel="unitOfMeasure"/>
                    </form:select>
                    <form:errors path="product[${index.index}].unitOfMeasure" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].quantity" />
                    <form:errors path="product[${index.index}].quantity" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].rate" />
                    <form:errors path="product[${index.index}].rate" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].totalPrice" />
                    <form:errors path="product[${index.index}].totalPrice" cssClass="error"/>
                </td>
            </tr>
        </c:forEach>
    </c:otherwise>
    </c:choose>
    <tr>
        <td></td>
        <td colspan="2">Packing & Forwarding Charges</div>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>
            <form:input path="pnfCharge"/>
            <form:errors path="pnfCharge" cssClass="error"/>
        </td>
    </tr>
    </tbody>
    </table>

    <div class="row">
        <div class="col-md-8">
            <span>Total Invoice amount in words</span>
        </div>
        <div class="col-md-2">
            <span>Total Amount</span>
        </div>
        <div class="col-md-2">
            <form:input path="totalAmount"/>
            <form:errors path="totalAmount" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <form:input path="totalInvoiceAmountInWords"/>
            <form:errors path="totalInvoiceAmountInWords" cssClass="error"/>
        </div>
        <div class="col-md-2">
            <span>Add: CGST 2.5%</span>
        </div>
        <div class="col-md-2">
            <form:input path="cGst"/>
            <form:errors path="cGst" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">

        </div>
        <div class="col-md-2">
            <span>Add: SGST 2.5%</span>
        </div>
        <div class="col-md-2">
            <form:input path="sGst"/>
            <form:errors path="sGst" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">

        </div>
        <div class="col-md-2">
            <span>Total Tax Amount</span>
        </div>
        <div class="col-md-2">
            <form:input path="totalTaxAmount"/>
            <form:errors path="totalTaxAmount" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">

        </div>
        <div class="col-md-2">
            <span>Round Off</span>
        </div>
        <div class="col-md-2">
            <form:input path="roundOff"/>
            <form:errors path="roundOff" cssClass="error"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">

        </div>
        <div class="col-md-2">
            <span>Total Amount after Tax</span>
        </div>
        <div class="col-md-2">
            <form:input path="totalAmountAfterTax"/>
            <form:errors path="totalAmountAfterTax" cssClass="error"/>
        </div>
    </div>
    <input class="btn btn-primary" type="Submit"/>
    <c:if test="${printInvoice}">
        <input id="printInvoice" class="btn btn-primary" type="button" value="Print"/>
    </c:if>
</div>
</form:form>
<script>
$(document).ready(function() {

    $("#billToParty\\.address\\.address").on("focusout",function(e){
        $("#shipToParty\\.address\\.address").val(this.value);
    })
    $("#billToParty\\.address\\.pinCode").on("focusout",function(e){
        $("#shipToParty\\.address\\.pinCode").val(this.value);
    })
    $("#billToParty\\.gst").on("focusout",function(e){
        $("#shipToParty\\.gst").val(this.value);
    })

    $( "#billToParty\\.address\\.state\\.name" ).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/states/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    response(data);
                },
                error : function(err) {
                    resetBillToPartyState()
                    console.error(err)
                }
            });
        },
        minLength: 3,
        select : function(event, ui) {
            this.value = ui.item.name
            setBillToPartyState(ui)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append("<a><strong>" + item.name + "</strong> - " + item.code + "</a>").appendTo(ul);
    };

    $("#billToParty\\.name").autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/company/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    response(data);
                },
                error : function(err) {
                    resetBillToParty()
                    console.error(err)
                }
            });
        },
        minLength: 4,
        select : function(event, ui) {
            this.value = ui.item.name
            setBillToParty(ui)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };

    //reset state fields in billToParty
    function resetBillToPartyState() {
        $("#billToParty\\.address\\.state\\.id").val('')
        $("#billToParty\\.address\\.state\\.country\\.id").val('')
        $("#billToParty\\.address\\.state\\.name").val('')
        $("#billToParty\\.address\\.state\\.code").val('')
    }
    //set state fields in billToParty
    function setBillToPartyState(ui) {
        $("#billToParty\\.address\\.state\\.id").val(ui.item.id)
        $("#billToParty\\.address\\.state\\.country\\.id").val(ui.item.country.id)
        $("#billToParty\\.address\\.state\\.code").val(ui.item.code)
        setShipToPartyState(ui)
    }

    //reset all fields in billToParty
    function resetBillToParty(){
        $("#billToParty\\.id").val('')
        $("#billToParty\\.address\\.address").val('')
        $("#billToParty\\.address\\.pinCode").val('')
        $("#billToParty\\.gst").val('')
    }

    //set all fields in billToParty
    function setBillToParty(ui) {
        $("#billToParty\\.id").val(ui.item.id)
        $("#billToParty\\.address\\.address").val(ui.item.address.address)
        $("#billToParty\\.address\\.pinCode").val(ui.item.address.pinCode)
        $("#billToParty\\.gst").val(ui.item.gst)
    }

    //reset state fields in billToParty
    function resetShipToPartyState() {
        $("#shipToParty\\.address\\.state\\.id").val('')
        $("#shipToParty\\.address\\.state\\.country\\.id").val('')
        $("#shipToParty\\.address\\.state\\.name").val('')
        $("#shipToParty\\.address\\.state\\.code").val('')
    }
    //set state fields in billToParty
    function setShipToPartyState(ui) {
        $("#shipToParty\\.address\\.state\\.id").val(ui.item.id)
        $("#shipToParty\\.address\\.state\\.country\\.id").val(ui.item.country.id)
        $("#shipToParty\\.address\\.state\\.name").val(ui.item.name)
        $("#shipToParty\\.address\\.state\\.code").val(ui.item.code)
    }

    //reset all fields in shipToParty
    function resetShipToParty(){
        $("#shipToParty\\.id").val('')
        $("#shipToParty\\.address\\.address").val('')
        $("#shipToParty\\.address\\.pinCode").val('')
        $("#shipToParty\\.gst").val('')
    }

    //set all fields in shipToParty
    function setShipToParty(ui) {
        $("#shipToParty\\.id").val(ui.item.id)
        $("#shipToParty\\.address\\.address").val(ui.item.address.address)
        $("#shipToParty\\.address\\.pinCode").val(ui.item.address.pinCode)
        $("#shipToParty\\.gst").val(ui.item.gst)
    }
});

</script>
<script src="${pageContext.request.contextPath}/js/invoice.js" ></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>