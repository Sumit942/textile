<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1"> -->
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
<form:form name="invoice" action="submit" method="POST" modelAttribute="invoiceCommand">
<div class="container-fluid">
    <div class="row mb-1">
        <div class="col-md-12 fs-1 fw-bold" style="text-align:center;">Tax Invoice</div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <form:hidden path="id"/>
            <form:label path="invoiceNo" class="col-md-3">Invoice No:</form:label>
            <form:input path="invoiceNo" class="col-md-3"/>
        </div>
        <div class="col-md-6 border">
            <form:label path="transportMode" class="col-md-3">Transport Mode:</form:label>
            <form:select path="transportMode" class="col-md-3">
                <form:options items="${transportModes}" itemValue="id" itemLabel="mode" />
            </form:select>
            <form:errors path="transportMode" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">

            <form:label path="invoiceDate" class="col-md-3">Invoice date:</form:label>
            <fmt:formatDate pattern="dd/MM/yyyy" value="${invoiceCommand.invoiceDate}" var="invoiceDateFormatted"/>
            <input name="invoiceDate" id="invoiceDate" value="${invoiceDateFormatted}" class="col-md-3" readonly/>
            <form:errors path="invoiceDate" cssClass="error"/>
        </div>
        <div class="col-md-6 border">
            <form:label path="vehicleNo" class="col-md-3">Vehicle Number:</form:label>
            <form:input path="vehicleNo" class="col-md-3"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <form:label path="reverseCharge" class="col-md-3">Reverse Charge (Y/N):</form:label>
            <form:select path="reverseCharge" class="col-md-3">
                <form:option value="NO" label="NO"/>
                <form:option value="YES" label="YES"/>
            </form:select>
            <form:errors path="reverseCharge" cssClass="error"/>
        </div>
        <div class="col-md-6 border">
            <form:label path="dateOfSupply" class="col-md-3">Date of Supply:</form:label>
            <fmt:formatDate pattern="dd/MM/yyyy" value="${invoiceCommand.dateOfSupply}" var="dateOfSupplyFormatted"/>
            <input name="dateOfSupply" id="dateOfSupply" value="${dateOfSupplyFormatted}" class="col-md-3" readonly/>
            <form:errors path="dateOfSupply" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
         <div class="col-md-6 border">
            <form:label path="invoiceBy.address.state.name" class="col-md-3">State:</form:label>
            <form:input path="invoiceBy.address.state.name" class="col-md-3"/>
            <form:errors path="invoiceBy.address.state.name" cssClass="error"/>
         </div>
         <div class="col-md-6 border">
            <form:label path="placeOfSupply" class="col-md-3">Place of Supply:</form:label>
            <form:input path="placeOfSupply" class="col-md-3"/>
            <form:errors path="placeOfSupply" cssClass="error"/>
         </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <div class="fw-bold text-center">
                <span>Bill to Party</span>
            </div>
            <div class="row mb-1">
                <form:hidden path="billToParty.id" />
                <form:label path="billToParty.name" class="col-md-3">Name:</form:label>
                <form:input path="billToParty.name" onkeypress="billToPartyAutoComplete(this);" class="col-md-3"/>
                <form:errors path="billToParty.name" cssClass="error"/>
            </div>
            <div class="row mb-1">
                <form:hidden path="billToParty.address.id" />
                <form:label path="billToParty.address.address" class="col-md-2">Address:</form:label>
                <form:textarea path="billToParty.address.address" class="col-md-5"/>
                <form:errors path="billToParty.address.address" cssClass="error"/>

                <form:label path="billToParty.address.pinCode" class="col-md-2" style="">PinCode:</form:label>
                <form:input path="billToParty.address.pinCode" class="col-md-3"/>
                <form:errors path="billToParty.address.pinCode" cssClass="error" />
            </div>
            <div class="row mb-1">
                <form:label path="billToParty.gst" class="col-md-3">GSTIN:</form:label>
                <form:input path="billToParty.gst" class="col-md-3"/>
                <form:errors path="billToParty.gst" cssClass="error"/>
            </div>
            <div class="row mb-1">
                <form:hidden path="billToParty.address.state.id" />
                <form:hidden path="billToParty.address.state.country.id" />
                <form:label path="billToParty.address.state.name" class="col-md-3">State:</form:label>
                <form:input path="billToParty.address.state.name" onkeypress="billToPartyStateAutoComplete(this);" class="col-md-3"/>
                <form:errors path="billToParty.address.state.name" cssClass="error"/>

                <form:label path="billToParty.address.state.code" class="col-md-3" style="">Code:</form:label>
                <form:input path="billToParty.address.state.code" class="col-md-3"/>
                <form:errors path="billToParty.address.state.code" cssClass="error"/>
            </div>
        </div>
        <div class="col-md-6 border">
            <div class="fw-bold text-center">
                <span>Ship to Party</span>
            </div>
            <div class="row mb-1">
                <form:hidden path="shipToParty.id" />
                <form:label path="shipToParty.name" class="col-md-3">Name:</form:label>
                <form:input path="shipToParty.name" onkeypress="shipToPartyAutoComplete(this);" class="col-md-3"/>
                <form:errors path="shipToParty.name" cssClass="error"/>
            </div>
            <div class="row mb-1">
                <form:hidden path="shipToParty.address.id" />
                <form:label path="shipToParty.address.address" class="col-md-2">Address:</form:label>
                <form:textarea path="shipToParty.address.address" class="col-md-5"/>
                <form:errors path="shipToParty.address.address" cssClass="error"/>

                <form:label path="shipToParty.address.pinCode" class="col-md-2" style="">PinCode:</form:label>
                <form:input path="shipToParty.address.pinCode" class="col-md-3"/>
                <form:errors path="shipToParty.address.pinCode" cssClass="error" />
            </div>
            <div class="row mb-1">
                <form:label path="shipToParty.gst" class="col-md-3">GSTIN:</form:label>
                <form:input path="shipToParty.gst" class="col-md-3"/>
                <form:errors path="shipToParty.gst" cssClass="error"/>
            </div>
            <div class="row mb-1">
                <form:hidden path="shipToParty.address.state.id" />
                <form:hidden path="shipToParty.address.state.country.id" />
                <form:label path="shipToParty.address.state.name" class="col-md-3">State:</form:label>
                <form:input path="shipToParty.address.state.name" onkeypress="shipToPartyStateAutoComplete(this);" class="col-md-3"/>
                <form:errors path="shipToParty.address.state.name" cssClass="error"/>

                <form:label path="shipToParty.address.state.code" class="col-md-3" style="">Code:</form:label>
                <form:input path="shipToParty.address.state.code" class="col-md-3"/>
                <form:errors path="shipToParty.address.state.code" cssClass="error"/>
            </div>
        </div>
    </div>

    <div class="">
    <table class="table" id="productDescTable">
    <thead>
        <tr>
          <th>Sr.No</th>
          <th>Product Description</th>
          <th>Challan No</th>
          <th>HSN code</th>
          <th>UOM</th>
          <th>Qty</th>
          <th>Rate</th>
          <th>Amount</th>
          <th></th>
        </tr>
    </thead>
    <tbody id="productDescTBody">
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
                <form:input path="product[0].product.name" onkeypress="autoSearchProduct(this,0)" class="ui-autocomplete-input" autocomplete="off"/>
                <form:errors path="product[0].product.name" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].chNo" class="numbersOnly"/>
                <form:errors path="product[0].chNo" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].product.hsn" class="numbersOnly" style="width: 100%;"/>
                <form:errors path="product[0].product.hsn" cssClass="error"/>
            </td>
            <td>
                <form:select path="product[0].unitOfMeasure.id">
                    <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure" itemValue="id"/>
                </form:select>
                <form:errors path="product[0].unitOfMeasure" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].quantity" class="numbersOnly" onkeyup="updateRowAmount(0)"/>
                <form:errors path="product[0].quantity" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].rate" class="numbersOnly" onkeyup="updateRowAmount(0)"/>
                <form:errors path="product[0].rate" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].totalPrice" readonly="true"/>
                <form:errors path="product[0].totalPrice" cssClass="error"/>
            </td>
            <td>
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
                    <form:input path="product[${index.index}].product.name" onkeypress="autoSearchProduct(this,${index.index})"/>
                    <form:errors path="product[${index.index}].product.name" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].chNo" class="numbersOnly"/>
                    <form:errors path="product[${index.index}].chNo" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].product.hsn" style="width: 100%;"/>
                    <form:errors path="product[${index.index}].product.hsn" cssClass="error"/>
                </td>
                <td>
                    <form:select path="product[${index.index}].unitOfMeasure.id">
                        <form:options items="${unitOfMeasures}" itemValue="id" itemLabel="unitOfMeasure"/>
                    </form:select>
                    <form:errors path="product[${index.index}].unitOfMeasure" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].quantity" class="numbersOnly" onkeyup="updateRowAmount(${index.index})"/>
                    <form:errors path="product[${index.index}].quantity" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].rate" class="numbersOnly" onkeyup="updateRowAmount(${index.index})"/>
                    <form:errors path="product[${index.index}].rate" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].totalPrice" readonly="true"/>
                    <form:errors path="product[${index.index}].totalPrice" cssClass="error"/>
                </td>
                <td>
                    <c:if test="${index.index > 0}">
                        <input  type="button" value="-" id="productDel_${index.index}" class="btn btn-sm btn-danger rounded" onclick="productDelRow()"/>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </c:otherwise>
    </c:choose>
    </tbody>
    <tfoot>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <input type="button" class="btn btn-primary" value="Add" id="add" onclick="addProductDescRow()"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td colspan="2">Packing & Forwarding Charges</div>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <form:input path="pnfCharge" class="numbersOnly"/>
                <form:errors path="pnfCharge" cssClass="error"/>
            </td>
            <td></td>
        </tr>
    </tfoot>
    </table>
    </div>

    <div class="row mb-1">
        <div class="col-md-8 border-end">
            <form:label path="totalInvoiceAmountInWords">Total Invoice amount in words</form:label>
        </div>
        <div class="col-md-2">
            <span>Total Amount</span>
        </div>
        <div class="col-md-2">
            <form:input path="totalAmount" readonly="true"/>
            <form:errors path="totalAmount" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-8 border-end">
            <form:textarea path="totalInvoiceAmountInWords" style="width:100%;height:100%;" readonly="true"/>
            <form:errors path="totalInvoiceAmountInWords" cssClass="error"/>
        </div>
        <div class="col-md-2">
            <span>Add: CGST 2.5%</span>
        </div>
        <div class="col-md-2">
            <form:input path="cGst" readonly="true"/>
            <form:errors path="cGst" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-8 border-end">

        </div>
        <div class="col-md-2">
            <span>Add: SGST 2.5%</span>
        </div>
        <div class="col-md-2">
            <form:input path="sGst" readonly="true"/>
            <form:errors path="sGst" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-8 border-end">

        </div>
        <div class="col-md-2">
            <span>Total Tax Amount</span>
        </div>
        <div class="col-md-2">
            <form:input path="totalTaxAmount" readonly="true"/>
            <form:errors path="totalTaxAmount" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-8 border-end">

        </div>
        <div class="col-md-2">
            <span>Round Off</span>
        </div>
        <div class="col-md-2">
            <form:input path="roundOff" readonly="true"/>
            <form:errors path="roundOff" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-8 border-end">

        </div>
        <div class="col-md-2">
            <span>Total Amount after Tax</span>
        </div>
        <div class="col-md-2">
            <form:input path="totalAmountAfterTax" class="numbersOnly"/>
            <form:errors path="totalAmountAfterTax" cssClass="error"/>
        </div>
    </div>
    <input type="Submit" class="btn btn-primary" />
    <c:if test="${printInvoice}">
        <a href="${pageContext.request.contextPath}/invoices/print/${invoiceCommand.id}" target="_blank">
            <input id="printInvoice" class="btn btn-primary" type="button" value="Print"/>
        </a>
    </c:if>
</div>
</form:form>
<script>
$(document).ready(function() {
    $( "#dateOfSupply" ).datepicker({
        dateFormat: 'dd/mm/yy'
    })
    $( "#invoiceDate" ).datepicker({
       format: 'dd/mm/yy'
    })
    $(".numbersOnly").on("keypress paste",function(e){
        var charCode = (e.which) ? e.which : event.keyCode
        console.log('charCode: '+charCode+', event: '+e.type)
        if (e.type == "paste")
            e.preventDefault()
        if (String.fromCharCode(charCode).match(/[^0-9]/g))
            return false
    })

    /**                 billToParty script                  **/
    $("#billToParty\\.address\\.address").on("focusout",function(e){
        $("#shipToParty\\.address\\.address").val(this.value);
        var pinCode = this.value.substr(-6)
        if (isNaN(pinCode) && $("#billToParty.id").val() == '') {
            return;
        }
        $("#billToParty\\.address\\.pinCode").val(pinCode);
        $("#shipToParty\\.address\\.pinCode").val(pinCode);
    })
    $("#billToParty\\.address\\.pinCode").on("focusout",function(e){
        $("#shipToParty\\.address\\.pinCode").val(this.value);
    })
    $("#billToParty\\.gst").on("focusout",function(e){
        $("#shipToParty\\.gst").val(this.value);
    })
    $("#billToParty\\.name").on("focusout",function(e){
        if (!$("#billToParty\\.id").val() && !$("#shipToParty\\.id").val()) {
            $("#shipToParty\\.name").val(this.value)
        }
    })


});
    //reset state fields in billToParty
    function resetBillToPartyState() {
        $("#billToParty\\.address\\.state\\.id").val('')
        $("#billToParty\\.address\\.state\\.country\\.id").val('')
        $("#billToParty\\.address\\.state\\.name").val('')
        $("#billToParty\\.address\\.state\\.code").val('')
    }

    //set state fields in billToParty
    function setBillToPartyState(item) {
        $("#billToParty\\.address\\.state\\.id").val(item.id)
        $("#billToParty\\.address\\.state\\.name").val(item.name)
        $("#billToParty\\.address\\.state\\.country\\.id").val(item.country.id)
        $("#billToParty\\.address\\.state\\.code").val(item.code)

        if (!$("#shipToParty\\.address\\.state\\.id").val()) {
            setShipToPartyState(item)
        }
    }

    //reset all fields in billToParty
    function resetBillToParty(){
        $("#billToParty\\.id").val('')
        $("#billToParty\\.address\\.address").val('')
        $("#billToParty\\.address\\.pinCode").val('')
        $("#billToParty\\.gst").val('')
    }

    //set all fields in billToParty
    function setBillToParty(item) {
        $("#billToParty\\.id").val(item.id)
        $("#billToParty\\.name").val(item.name)
        $("#billToParty\\.address\\.address").val(item.address.address)
        $("#billToParty\\.address\\.pinCode").val(item.address.pinCode)
        $("#billToParty\\.gst").val(item.gst)
        setBillToPartyState(item.address.state)
        if (!$("#shipToParty\\.id").val()) {
            setShipToParty(item)
        }
    }

    //reset state fields in billToParty
    function resetShipToPartyState() {
        $("#shipToParty\\.address\\.state\\.id").val('')
        $("#shipToParty\\.address\\.state\\.country\\.id").val('')
        $("#shipToParty\\.address\\.state\\.name").val('')
        $("#shipToParty\\.address\\.state\\.code").val('')
    }
    //set state fields in billToParty
    function setShipToPartyState(item) {
        $("#shipToParty\\.address\\.state\\.id").val(item.id)
        $("#shipToParty\\.address\\.state\\.country\\.id").val(item.country.id)
        $("#shipToParty\\.address\\.state\\.name").val(item.name)
        $("#shipToParty\\.address\\.state\\.code").val(item.code)
    }

    //reset all fields in shipToParty
    function resetShipToParty(){
        $("#shipToParty\\.id").val('')
        $("#shipToParty\\.address\\.address").val('')
        $("#shipToParty\\.address\\.pinCode").val('')
        $("#shipToParty\\.gst").val('')
    }

    //set all fields in shipToParty
    function setShipToParty(item) {
        $("#shipToParty\\.id").val(item.id)
        $("#shipToParty\\.name").val(item.name)
        $("#shipToParty\\.address\\.address").val(item.address.address)
        $("#shipToParty\\.address\\.pinCode").val(item.address.pinCode)
        $("#shipToParty\\.gst").val(item.gst)
        setShipToPartyState(item.address.state)
    }

    /**                 billToParty AutoComplete functions                  **/
function billToPartyAutoComplete(thisObj) {
    resetBillToParty()
    $(thisObj).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/company/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    resetBillToParty()
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
            console.log('selected')
            this.value = ui.item.name
            setBillToParty(ui.item)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
}
function billToPartyStateAutoComplete(thisObj) {
    resetBillToPartyState()
    $( thisObj ).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/states/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    resetBillToPartyState()
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
            setBillToPartyState(ui.item)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append("<a><strong>" + item.name + "</strong> - " + item.code + "</a>").appendTo(ul);
    };
}
    /**                 shipToParty autocomplete functions script                  **/
function shipToPartyStateAutoComplete(thisObj) {
    resetShipToPartyState()
    $( thisObj ).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/states/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    resetShipToPartyState()
                    response(data);
                },
                error : function(err) {
                    resetShipToPartyState()
                    console.error(err)
                }
            });
        },
        minLength: 3,
        select : function(event, ui) {
            this.value = ui.item.name
            setShipToPartyState(ui.item)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append("<a><strong>" + item.name + "</strong> - " + item.code + "</a>").appendTo(ul);
    };
}

function shipToPartyAutoComplete(thisObj) {
    resetShipToParty()
    $(thisObj).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/company/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    resetShipToParty()
                    response(data);
                },
                error : function(err) {
                    resetShipToParty()
                    console.error(err)
                }
            });
        },
        minLength: 4,
        select : function(event, ui) {
            this.value = ui.item.name
            setShipToParty(ui.item)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
}

    /**                 productDescription script                  **/
function autoSearchProduct(obj,index) {
$("#product"+index+"\\.product\\.hsn").val('')
$("#product"+index+"\\.product\\.id").val('')
    $(obj).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/product/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    $("#product"+index+"\\.product\\.hsn").val('')
                    $("#product"+index+"\\.product\\.id").val('')
                    response(data);
                },
                error : function(err) {
                    $("#product"+index+"\\.product\\.hsn").val('')
                    $("#product"+index+"\\.product\\.id").val('')
                    console.error(err)
                }
            });
        },
        minLength: 3,
        select : function(event, ui) {
            this.value = ui.item.name
            $("#product"+index+"\\.product\\.hsn").val(ui.item.hsn)
            $("#product"+index+"\\.product\\.id").val(ui.item.id)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong></a>").appendTo(ul);
    };
}


    /**                 add button script                  **/
function addProductDescRow() {

    var i = $("#productDescTBody > tr").length - 1;

    var prodDescRow = '<tr>'+
                        '<td>'+
                            '<span id="product['+i+'].srNo">'+(i+1)+'</span>'+
                            '<input id="product'+i+'.id" name="product['+i+'].id" type="hidden" value="">'+
                        '</td>'+
                        '<td>'+
                            '<input type="hidden" name="product['+i+'].product.id">'+
                            '<input id="product'+i+'.product.name" name="product['+i+'].product.name" onkeypress="autoSearchProduct(this,'+i+')" type="text" value="">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.chNo" name="product['+i+'].chNo" type="text" class="numbersOnly">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.product.hsn" name="product['+i+'].product.hsn" type="text" value="6006" style="width: 100%;">'+
                        '</td>'+
                        '<td>'+
                            '<select id="product'+i+'.unitOfMeasure.id" name="product['+i+'].unitOfMeasure.id">'+
                                $("#product0\\.unitOfMeasure\\.id").html()+
                            '</select>'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.quantity" name="product['+i+'].quantity" type="text" class="numbersOnly" onkeyup="updateRowAmount('+i+')" >'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.rate" name="product['+i+'].rate" type="text" class="numbersOnly" onkeyup="updateRowAmount('+i+')">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.totalPrice" name="product['+i+'].totalPrice" type="text" value="0" readonly>'+
                        '</td>'+
                        '<td>'+
                            '<input type="button" value="-" id="productDel_'+i+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow()" style="margin-left: 18%;width: 60%;">'+
                        '</td>'+
                      '</tr>';

    $("#productDescTBody > tr:eq("+i+")").find('td:eq(8)').html('')
    $("#productDescTBody").append(prodDescRow)
}
function productDelRow() {
    var rowCount = $("#productDescTBody > tr").length
    if (rowCount > 2) {
        $("#productDescTBody > tr:last").remove()
        if (rowCount > 3) {
            $("#productDescTBody > tr:last > td:eq(8)").html('<input type="button" value="-" id="productDel_'+(rowCount-1)+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow()" style="margin-left: 18%;width: 60%;">')
        }
    }
    updateTotalAmount()
}
    /**                 calculation script                  **/
function updateRowAmount(index) {

    var quantity = $("#product"+index+"\\.quantity").val()
    var rate= $("#product"+index+"\\.rate").val()
    var amount = quantity * rate
    //console.log('quantity: '+quantity+', rate: '+rate+', amount: '+amount)
    $("#product"+index+"\\.totalPrice").val(amount)
    if ($("#product"+index+"\\.totalPrice").val())
    updateTotalAmount()
}
function updateTotalAmount() {

    var totalAmount = 0;
    $("input[id$='.totalPrice']").each(function(k,v){
        totalAmount += parseFloat(v.value)
        //console.log('adding... '+v.value)
    })
    $("#totalAmount").val(totalAmount)

    var gst = parseFloat((totalAmount*(2.5/100)).toFixed(2));
    $("#cGst").val(gst)
    $("#sGst").val(gst)

    var totalTaxAmount = gst*2
    $("#totalTaxAmount").val(totalTaxAmount)
    //console.log('totalAmount: '+totalAmount+', gst: '+gst+', totalTaxAmount: '+totalTaxAmount)

    var totalAmountAfterTax = totalAmount + totalTaxAmount
    var roundedTotalAmountAfterTax = Math.round(totalAmountAfterTax)

    $("#roundOff").val((totalAmountAfterTax-roundedTotalAmountAfterTax).toFixed(2))

    $("#totalAmountAfterTax").val(roundedTotalAmountAfterTax)

    inWords(roundedTotalAmountAfterTax)
}

var a = ['','One ','Two ','Three ','Four ', 'Five ','Six ','Seven ','Eight ','Nine ','Ten ','Eleven ','Twelve ','Thirteen ','Fourteen ','Fifteen ','Sixteen ','Seventeen ','Eighteen ','Nineteen '];
var b = ['', '', 'Twenty','Thirty','Forty','Fifty', 'Sixty','Seventy','Eighty','Ninety'];

function inWords (num) {
    if ((num = num.toString()).length > 9) return 'overflow';
    n = ('000000000' + num).substr(-9).match(/^(\d{2})(\d{2})(\d{2})(\d{1})(\d{2})$/);
    if (!n) return; var str = '';
    str += (n[1] != 0) ? (a[Number(n[1])] || b[n[1][0]] + ' ' + a[n[1][1]]) + 'Crore ' : '';
    str += (n[2] != 0) ? (a[Number(n[2])] || b[n[2][0]] + ' ' + a[n[2][1]]) + 'Lakh ' : '';
    str += (n[3] != 0) ? (a[Number(n[3])] || b[n[3][0]] + ' ' + a[n[3][1]]) + 'Thousand ' : '';
    str += (n[4] != 0) ? (a[Number(n[4])] || b[n[4][0]] + ' ' + a[n[4][1]]) + 'Hundred ' : '';
    str += (n[5] != 0) ? ((str != '') ? 'And ' : '') + (a[Number(n[5])] || b[n[5][0]] + ' ' + a[n[5][1]]) + 'Only.' : '.';

    $("#totalInvoiceAmountInWords").val(str)
}
</script>
<script src="${pageContext.request.contextPath}/js/invoice.js" ></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>