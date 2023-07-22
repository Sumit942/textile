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
<form:form name="invoice" action="${pageContext.request.contextPath}/invoices/submit" method="POST" modelAttribute="invoiceCommand">
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
            Invoice ${invoiceCommand.getInvoiceNo()} Save Successfully!!
        </div>
    </c:if>
    <div class="row mb-1">
        <div class="col-md-12 fs-1 fw-bold" style="text-align:center;">Tax Invoice</div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <form:hidden path="id"/>
            <form:hidden path="invoiceBy.address.state.code"/>
            <form:label path="invoiceNo" class="col-md-3">Invoice No:</form:label>
            <form:input path="invoiceNo" class="col-md-3"/>
            <form:errors path="invoiceNo" cssClass="error" class="col-md-3"/>
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
            <div class="fw-bold text-center bg-light">
                <span>Bill to Party</span>
            </div>
            <div class="row mb-1">
                <form:hidden path="billToParty.id" />
                <form:label path="billToParty.name" class="col-md-3">Name:</form:label>
                <form:input path="billToParty.name" onkeyup="billToPartyAutoComplete(event,this);" class="col-md-3" required="true"/>
                <form:errors path="billToParty.name" cssClass="error"/>
            </div>
            <div class="row mb-1">
                <form:hidden path="billToParty.address.id" />
                <form:label path="billToParty.address.address" class="col-md-2">Address:</form:label>
                <form:textarea path="billToParty.address.address" class="col-md-5" required="true"/>
                <form:errors path="billToParty.address.address" cssClass="error"/>

                <form:label path="billToParty.address.pinCode" class="col-md-2" style="">PinCode:</form:label>
                <form:input path="billToParty.address.pinCode" class="col-md-3" required="true"/>
                <form:errors path="billToParty.address.pinCode" cssClass="error" />
            </div>
            <div class="row mb-1">
                <form:label path="billToParty.gst" class="col-md-3">GSTIN:</form:label>
                <form:input path="billToParty.gst" class="col-md-3" required="true"/>
                <form:errors path="billToParty.gst" cssClass="error"/>
                <a class="col-md-3" href="https://services.gst.gov.in/services/searchtp" target="_blank">Verify GST</a>
            </div>
            <div class="row mb-1">
                <form:hidden path="billToParty.address.state.id" />
                <form:hidden path="billToParty.address.state.country.id" />
                <form:label path="billToParty.address.state.name" class="col-md-3">State:</form:label>
                <form:input path="billToParty.address.state.name" required="true" onkeyup="billToPartyStateAutoComplete(event, this);" class="col-md-3"/>
                <form:errors path="billToParty.address.state.name" cssClass="error"/>

                <form:label path="billToParty.address.state.code" class="col-md-3" style="">Code:</form:label>
                <form:input path="billToParty.address.state.code" class="col-md-3" required="true"/>
                <form:errors path="billToParty.address.state.code" cssClass="error"/>
            </div>
        </div>
        <div class="col-md-6 border">
            <div class="fw-bold text-center bg-light">
                <span>Ship to Party</span>
            </div>
            <div class="row mb-1">
                <form:hidden path="shipToParty.id" />
                <form:label path="shipToParty.name" class="col-md-3">Name:</form:label>
                <form:input path="shipToParty.name" onkeyup="shipToPartyAutoComplete(event, this);" class="col-md-3" required="true"/>
                <form:errors path="shipToParty.name" cssClass="error"/>
            </div>
            <div class="row mb-1">
                <form:hidden path="shipToParty.address.id" />
                <form:label path="shipToParty.address.address" class="col-md-2">Address:</form:label>
                <form:textarea path="shipToParty.address.address" class="col-md-5" required="true"/>
                <form:errors path="shipToParty.address.address" cssClass="error"/>

                <form:label path="shipToParty.address.pinCode" class="col-md-2" style="">PinCode:</form:label>
                <form:input path="shipToParty.address.pinCode" class="col-md-3" required="true"/>
                <form:errors path="shipToParty.address.pinCode" cssClass="error" />
            </div>
            <div class="row mb-1">
                <form:label path="shipToParty.gst" class="col-md-3">GSTIN:</form:label>
                <form:input path="shipToParty.gst" class="col-md-3" required="true"/>
                <form:errors path="shipToParty.gst" cssClass="error"/>
                <a class="col-md-3" href="https://services.gst.gov.in/services/searchtp" target="_blank">Verify GST</a>
            </div>
            <div class="row mb-1">
                <form:hidden path="shipToParty.address.state.id" />
                <form:hidden path="shipToParty.address.state.country.id" />
                <form:label path="shipToParty.address.state.name" class="col-md-3">State:</form:label>
                <form:input path="shipToParty.address.state.name" required="true" onkeyup="shipToPartyStateAutoComplete(event, this);" class="col-md-3"/>
                <form:errors path="shipToParty.address.state.name" cssClass="error"/>

                <form:label path="shipToParty.address.state.code" class="col-md-3" style="">Code:</form:label>
                <form:input path="shipToParty.address.state.code" class="col-md-3" required="true"/>
                <form:errors path="shipToParty.address.state.code" cssClass="error"/>
            </div>
        </div>
    </div>

    <div class="">
    <table class="table" id="productDescTable" width="100%">
    <thead>
        <tr>
          <th>Sr.No</th>
          <th>Product Description</th>
          <th>Challan No</th>
          <th>HSN code</th>
          <th>UOM</th>
          <th>Quantity</th>
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
                <!-- <form:hidden path="product[0].id" /> -->
                <form:hidden path="product[0].party.id" />
            </td>
            <td>
                <form:hidden path="product[0].product.id"/>
                <form:hidden path="product[0].product.active"/>
                <form:input path="product[0].product.name" required="true" onkeyup="autoSearchProduct(event, this,0)" class="ui-autocomplete-input" autocomplete="off"/>
                <form:errors path="product[0].product.name" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].chNo" class="numbersOnly" required="true" style="width: 100%;" onkeyup="autoSearchChallanNo(event,this, 0)"/>
                <form:errors path="product[0].chNo" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].product.hsn" class="numbersOnly" required="true" style="width: 100%;"/>
                <form:errors path="product[0].product.hsn" cssClass="error"/>
            </td>
            <td>
                <form:select path="product[0].unitOfMeasure.id" required="true">
                    <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure" itemValue="id"/>
                </form:select>
                <form:errors path="product[0].unitOfMeasure" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].quantity" required="true" class="numbersOnly" onkeyup="updateRowAmount(0)" style="width: 100%;"/>
                <form:errors path="product[0].quantity" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].rate" required="true" class="numbersOnly" onkeyup="updateRowAmount(0)" style="width: 100%;"/>
                <form:errors path="product[0].rate" cssClass="error"/>
            </td>
            <td>
                <form:input path="product[0].totalPrice" required="true" readonly="true"/>
                <form:errors path="product[0].totalPrice" cssClass="error"/>
            </td>
            <td>
            </td>
        </tr>
    </c:when>
    <c:otherwise>
        <c:forEach items="${invoiceCommand.product}" var="product" varStatus="index">
            <tr>
                <td>
                    <span id="product[${index.index}].srNo">${index.index + 1}</span>
                    <!-- <form:hidden path="product[${index.index}].id" /> -->
                    <form:hidden path="product[${index.index}].party.id" />
                </td>
                <td>
                    <form:hidden path="product[${index.index}].product.id" />
                    <form:hidden path="product[${index.index}].product.active" />
                    <form:input path="product[${index.index}].product.name" required="true" onkeyup="autoSearchProduct(event,this,${index.index})"/>
                    <form:errors path="product[${index.index}].product.name" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].chNo" required="true" class="numbersOnly" style="width: 100%;" onkeyup="autoSearchChallanNo(event, this, ${index.index})"/>
                    <form:errors path="product[${index.index}].chNo" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].product.hsn" required="true" style="width: 100%;"/>
                    <form:errors path="product[${index.index}].product.hsn" cssClass="error"/>
                </td>
                <td>
                    <form:select path="product[${index.index}].unitOfMeasure.id" required="true">
                        <form:options items="${unitOfMeasures}" itemValue="id" itemLabel="unitOfMeasure"/>
                    </form:select>
                    <form:errors path="product[${index.index}].unitOfMeasure" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].quantity" required="true" class="numbersOnly" onkeyup="updateRowAmount(${index.index})" style="width: 100%;"/>
                    <form:errors path="product[${index.index}].quantity" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].rate" required="true" class="numbersOnly" onkeyup="updateRowAmount(${index.index})" style="width: 100%;"/>
                    <form:errors path="product[${index.index}].rate" cssClass="error"/>
                </td>
                <td>
                    <form:input path="product[${index.index}].totalPrice" readonly="true" required="true"/>
                    <form:errors path="product[${index.index}].totalPrice" cssClass="error"/>
                </td>
                <td>
                    <input  type="button" value="-" id="productDel_${index.index}" class="btn btn-sm btn-danger rounded" onclick="productDelRow(${index.index})" style="margin-left: 18%;width: 60%;"/>
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
            <td><input type="button" class="btn btn-primary float-end" value="Load Challan" id="loadChallan"/></td>
            <td>
                <span class="challanLoader text-primary"></span>
                <input type="button" class="btn btn-primary float-end" value="Duplicate" id="addDuplicate" onclick="addProductDescRow('duplicate')"/>
            </td>
            <td>
                <input type="button" class="btn btn-primary" value="Add" id="add" onclick="addProductDescRow('new')"/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td colspan="4">
                <form:label path="pnfCharge">Packing & Forwarding Charges</form:label>
            <td></td>
            <td>
                <form:input path="pnfCharge" class="numbersOnly" onkeyup="updateTotalAmount();"/>
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
            <form:label path="gstPerc">GST: </form:label>
        </div>
        <div class="col-md-2">
            <form:input path="gstPerc" class="numbersOnly" style="width: 50%;" onkeyup="updateGstPerc(this.value)"/>  %
            <form:errors path="gstPerc" cssClass="error"/>
        </div>

    </div>
    <div class="row mb-1 stateGst">
        <div class="col-md-8 border-end">

        </div>
        <div class="col-md-2">
            <form:label path="cGst">Add: CGST <span class="gstPercentage">2.5</span>%</form:label>
        </div>
        <div class="col-md-2">
            <form:input path="cGst" readonly="true"/>
            <form:errors path="cGst" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1 stateGst">
        <div class="col-md-8 border-end">

        </div>
        <div class="col-md-2">
           <form:label path="sGst">Add: SGST <span class="gstPercentage">2.5</span>%</form:label>
        </div>
        <div class="col-md-2">
            <form:input path="sGst" readonly="true"/>
            <form:errors path="sGst" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1 interStateGst">
        <div class="col-md-8 border-end">

        </div>
        <div class="col-md-2">
           <form:label path="iGst">Add: IGST <span class="gstPercentage">2.5</span>%</form:label>
        </div>
        <div class="col-md-2">
            <form:input path="iGst" readonly="true"/>
            <form:errors path="iGst" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-8 border-end bg-light">
            <form:label path="selectedBank.id" class="col-md-2 fw-bold">Bank Details:</form:label>
            <form:select path="selectedBank.id">
                <form:options items="${invoiceCommand.invoiceBy.bankDetails}" itemValue="id" itemLabel="bankName" />
            </form:select>
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
            <form:input path="totalAmountAfterTax" required="true" class="numbersOnly" onkeyup="javascript:inWords(this.value)"/>
            <form:errors path="totalAmountAfterTax" cssClass="error"/>
        </div>
    </div>
    <input type="Submit" class="btn btn-primary" />
    <c:choose>
    <c:when test="${printInvoice}">
        <a href="${pageContext.request.contextPath}/invoices/printById/${invoiceCommand.id}" target="_blank">
            <input id="printInvoice" class="btn btn-primary" type="button" value="Print"/>
        </a>
    </c:when>
    <c:otherwise>
        <input id="demoInvoice" class="btn btn-primary" type="button" value="Demo Print" onclick="demoInvoicePrint()"/>
    </c:otherwise>
    </c:choose>
</div>
</form:form>
<script>
function showHideGstTab() {
    if ( $('#billToParty\\.address\\.state\\.code').val() == ''
        || ($('#invoiceBy\\.address\\.state\\.code').val() == $('#billToParty\\.address\\.state\\.code').val())) {
        $('.interStateGst').hide()
        $('.stateGst').show()
    } else {
        $('.interStateGst').show()
        $('.stateGst').hide()
    }
}

$(document).ready(function() {
    showHideGstTab()

    $( "#dateOfSupply" ).datepicker({
        dateFormat: 'dd/mm/yy'
    })
    $( "#invoiceDate" ).datepicker({
       dateFormat: 'dd/mm/yy'
    })
    if ($("#id").val() == '' ) {
        $("#invoiceDate").focus()
    }

    $(".numbersOnly").on("keypress paste",function(e){
        var charCode = (e.which) ? e.which : event.keyCode
        //console.log('charCode: '+charCode+', event: '+e.type)
        if (e.type == "paste")
            e.preventDefault()

        if (String.fromCharCode(charCode).match(/[^0-9+?.+0-9$]/g))
            return false
    })

    /**                 billToParty script                  **/
    $("#billToParty\\.address\\.address").on("focusout",function(e){
        var pinCode = this.value.substr(-6)
        if ($("#shipToParty\\.id").val() == '') {
            $("#shipToParty\\.address\\.address").val(this.value);
        }
        if (isNaN(pinCode)) {
            return;
        }
        $("#billToParty\\.address\\.pinCode").val(pinCode);

        if ($("#shipToParty\\.id").val() == '') {
            $("#shipToParty\\.address\\.pinCode").val(pinCode);
        }
    })
    $("#billToParty\\.address\\.pinCode").on("focusout",function(e){
        $("#shipToParty\\.address\\.pinCode").val(this.value);
    })
    $("#billToParty\\.name").on("focusout",function(e){
        if (!$("#billToParty\\.id").val() && !$("#shipToParty\\.id").val()) {
            $("#shipToParty\\.name").val(this.value)
        }
    })
    $("#billToParty\\.gst").on("focusout",function(e){
        if (this.value.length <= 2)
            return;
        getStateByCode('billToParty',this.value.substr(0,2))  //get code from gst
        if ($("#shipToParty\\.id").val() == '') {
            $("#shipToParty\\.gst").val(this.value);
        }
    })
    $("#shipToParty\\.gst").on("focusout",function(e){
        if (this.value.length <= 2)
            return;
        getStateByCode('shipToParty',this.value.substr(0,2))  //get code from gst
    })
});// document.ready function ends
    /**     get state by code ajax                         **/
    function getStateByCode(party, stateCode) {
        $.ajax({
            url : "${pageContext.request.contextPath}/states/findByCode/"+stateCode,
            dataType : 'json',
            success : function(data) {
                if (party == 'billToParty') {
                    setBillToPartyState(data);
                } else {
                    setShipToPartyState(data);
                }
            },
            error : function(err) {
                console.log(err)
                if (party == 'billToParty') {
                    resetBillToPartyState();
                } else {
                    resetShipToPartyState();
                }
            }
        })
    }

    //reset state fields in billToParty
    function resetBillToPartyState() {
        $("#billToParty\\.address\\.state\\.id").val('')
        $("#billToParty\\.address\\.state\\.country\\.id").val('')
        //$("#billToParty\\.address\\.state\\.name").val('')
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
        showHideGstTab()
    }

    //reset all fields in billToParty
    function resetBillToParty() {
        $("#billToParty\\.id").val('')
        $("#billToParty\\.address\\.id").val('')
        $("#billToParty\\.address\\.address").val('')
        $("#billToParty\\.address\\.pinCode").val('')
        $("#billToParty\\.gst").val('')
    }

    //set all fields in billToParty
    function setBillToParty(item) {
        $("#billToParty\\.id").val(item.id)
        $("#billToParty\\.name").val(item.name)
        $("#billToParty\\.address\\.id").val(item.address.id)
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
        //$("#shipToParty\\.address\\.state\\.name").val('')
        $("#shipToParty\\.address\\.state\\.code").val('')
    }
    //set state fields in shipToParty
    function setShipToPartyState(item) {
        $("#shipToParty\\.address\\.state\\.id").val(item.id)
        $("#shipToParty\\.address\\.state\\.country\\.id").val(item.country.id)
        $("#shipToParty\\.address\\.state\\.name").val(item.name)
        $("#shipToParty\\.address\\.state\\.code").val(item.code)
    }

    //reset all fields in shipToParty
    function resetShipToParty(){
        $("#shipToParty\\.id").val('')
        $("#shipToParty\\.address\\.id").val('')
        $("#shipToParty\\.address\\.address").val('')
        $("#shipToParty\\.address\\.pinCode").val('')
        $("#shipToParty\\.gst").val('')
    }

    //set all fields in shipToParty
    function setShipToParty(item) {
        $("#shipToParty\\.id").val(item.id)
        $("#shipToParty\\.name").val(item.name)
        $("#shipToParty\\.address\\.id").val(item.address.id)
        $("#shipToParty\\.address\\.address").val(item.address.address)
        $("#shipToParty\\.address\\.pinCode").val(item.address.pinCode)
        $("#shipToParty\\.gst").val(item.gst)
        setShipToPartyState(item.address.state)
    }
    /**                 billToParty AutoComplete functions                  **/
function billToPartyAutoComplete(event,thisObj) {
    if (event.key == 'Enter') {
        autoFocusProductDescField()
        return
    }
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
            this.value = ui.item.name
            setBillToParty(ui.item)
            getProductDetailsByCompanyId(ui.item.id)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
}
function billToPartyStateAutoComplete(event, thisObj) {
    if (event.key == 'Enter') {
        autoFocusProductDescField()
        return
    }
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
            //autoFocusProductDescField()
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append("<a><strong>" + item.name + "</strong> - " + item.code + "</a>").appendTo(ul);
    };
}
    /**                 shipToParty autocomplete functions script                  **/
function shipToPartyStateAutoComplete(event,thisObj) {
    if (event.key == 'Enter')
        return
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
            autoFocusProductDescField()
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append("<a><strong>" + item.name + "</strong> - " + item.code + "</a>").appendTo(ul);
    };
}

function shipToPartyAutoComplete(event,thisObj) {
    if (event.key == 'Enter')
        return
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
            autoFocusProductDescField()
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
}
    /**                 productDescription script                  **/
function autoSearchChallanNo(event,obj,index) {
    if (event.key == 'Enter') {
        return;
    }
    $(obj).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/productDetail/searchByChallanNo",
                dataType : 'json',
                data : {
                    chNo : request.term
                },
                success : function(data) {
                    response(data);
                },
                error : function(err) {
                    console.error(err)
                }
            });
        },
        minLength: 3,
        select : function(event, ui) {
            populateProductDetail(ui, index)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.chNo + "</strong>"+(item.invoiceNo ? ' - '+item.invoiceNo : '')+"</a>").appendTo(ul);
    };
}
function populateProductDetail(ui,i) {
    $('#product'+i+'\\.product\\.id').val(ui.item.product.id)
    $('#product'+i+'\\.product\\.party\\.id').val(ui.item.product.party.id)
    $('#product'+i+'\\.product\\.active').val(ui.item.product.active)
    $('#product'+i+'\\.product\\.name').val(ui.item.product.name)
    $('#product'+i+'\\.chNo').val(ui.item.chNo)
    $('#product'+i+'\\.hsn').val(ui.item.hsn)
    $('#product'+i+'\\.unitOfMeasure\\.id').val(ui.item.unitOfMeasure.id)
    $('#product'+i+'\\.quantity').val(ui.item.quantity)
    $('#product'+i+'\\.rate').val(ui.item.rate)
    //$('#product'+i+'\\.totalPrice').val(ui.item.totalPrice)
    updateRowAmount(i)
}
    /**                 productDescription script                  **/
function autoSearchProduct(event,obj,index) {
if (event.key == 'Enter') {
    return;
}
//$("#product"+index+"\\.product\\.hsn").val('')
$("#product"+index+"\\.product\\.id").val('')
    $(obj).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/product/searchByName",
                dataType : 'json',
                data : {
                    name : request.term
                },
                success : function(data) {
                    //$("#product"+index+"\\.product\\.hsn").val('')
                    $("#product"+index+"\\.product\\.id").val('')
                    $("#product"+index+"\\.product\\.party\\.id").val('')
                    response(data);
                },
                error : function(err) {
                    //$("#product"+index+"\\.product\\.hsn").val('')
                    $("#product"+index+"\\.product\\.id").val('')
                    $("#product"+index+"\\.product\\.party\\.id").val('')
                    console.error(err)
                }
            });
        },
        minLength: 3,
        select : function(event, ui) {
            this.value = ui.item.name
            //$("#product"+index+"\\.product\\.hsn").val(ui.item.hsn)
            $("#product"+index+"\\.product\\.id").val(ui.item.id)
            $("#product"+index+"\\.product\\.party.id").val(ui.item.party.id)
            $("#product"+index+"\\.product\\.active").val(ui.item.active)
            //get the max rate for company
            $("#product"+index+"\\.chNo").focus()
            if (productId != '') {
                getProductMaxRateByCompanyId(index,productId)
            }
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong></a>").appendTo(ul);
    };
}
/**             get Product Rate                    **/
function getProductMaxRateByCompanyId(index,productId) {

    var companyId = $("#billToParty\\.id").val()
    var rate = $("#product"+index+"\\.rate").val();
    console.log('inside getProductRate(): compId-'+companyId+', prodId-'+productId+', rate-'+rate)
    if (productId != '' && productId != 0 && rate == '') {
        $.ajax({
            url : "${pageContext.request.contextPath}/product/rateByProductAndCompanyId?companyId="+companyId+"&productId="+productId,
            success : function(data) {
                if (data != '') {
                    $("#product"+index+"\\.rate").val(data)
                    updateRowAmount(index)
                }
            },
            error : function(err) {
                $("#product"+index+"\\.rate").val('')
                console.error(err)
            }
        })
    }
}


    /**                 add button script                  **/
function addProductDescRow(addRowType) {

    var i = $("#productDescTBody > tr").length - 1;
    //check if the last row data is entered or not
    var lastPrdId = '#product'+(i-1)+'\\.product\\.id'
    var lastPrdName = '#product'+(i-1)+'\\.product\\.name'
    var lastChNo = '#product'+(i-1)+'\\.chNo'
    if ( $(lastPrdName).val() == '' ) {
        alert ("Please enter 'Product Description' in last row")
        $(lastPrdName).focus()
        return;
    }
    var lastHsn = '#product'+(i-1)+'\\.product\\.hsn'
    if ( $(lastHsn).val() == '' ) {
        alert ("Please enter 'HSN' in last row")
        $(lastHsn).focus()
        return;
    }
    var lastUom = '#product'+(i-1)+'\\.unitOfMeasure\\.id'
    if ( $(lastUom).val() == '' ) {
        alert ("Please enter 'Unit Of Measure' in last row")
        $(lastUom).focus()
        return;
    }
    var lastQty = '#product'+(i-1)+'\\.quantity'
    if ( $(lastQty).val() == '' ) {
        alert ("Please enter 'Quantity' in last row")
        $(lastQty).focus()
        return;
    }
    var lastRate = '#product'+(i-1)+'\\.rate'
    if ( $(lastRate).val() == '' ) {
        alert ("Please enter 'Rate' in last row")
        $(lastRate).focus()
        return;
    }
    /*if ( $('#product'+(i-1)+'\\.totalPrice').val() == '' ) {
        alert ("Please enter 'Total Price' in last row")
        return;
    }*/
    //check duplicate chNos
    if (hasDuplicateChNos()){
        alert ("Please entry unique 'challan no.' in last row")
        $(lastChNo).focus()
        return;
    }

    var prodDescRow = '<tr>'+
                        '<td>'+
                            '<span id="product['+i+'].srNo">'+(i+1)+'</span>'+
                            //'<input id="product'+i+'.id" name="product['+i+'].id" type="hidden" value="">'+
                            '<input id="product'+i+'.party.id" name="product['+i+'].party.id" type="hidden" value="'+$("#billToParty\\.id").val()+'">'+
                        '</td>'+
                        '<td>'+
                            '<input type="hidden" name="product['+i+'].product.id" value="'+(addRowType == 'duplicate' ? $(lastPrdId).val() : '')+'">'+
                            '<input type="hidden" name="product['+i+'].product.active" value="true">'+
                            '<input id="product'+i+'.product.name" name="product['+i+'].product.name" required="required" onkeyup="autoSearchProduct(event,this,'+i+')" type="text" value="'+(addRowType == 'duplicate' ? $(lastPrdName).val() : '')+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.chNo" onkeyup="autoSearchChallanNo(event, this, '+i+')" required="required" name="product['+i+'].chNo" type="text" class="numbersOnly" value="'+($(lastChNo).val() != '' ? (parseInt($(lastChNo).val())+1) : '')+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.product.hsn" name="product['+i+'].product.hsn" required="required" type="text" value="6006" style="width: 100%;">'+
                        '</td>'+
                        '<td>'+
                            '<select id="product'+i+'.unitOfMeasure.id" name="product['+i+'].unitOfMeasure.id" required="required">'+
                                $("#product0\\.unitOfMeasure\\.id").html()+
                            '</select>'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.quantity" name="product['+i+'].quantity" required="required" type="text" class="numbersOnly" onkeyup="updateRowAmount('+i+')" >'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.rate" name="product['+i+'].rate" required="required" type="text" class="numbersOnly" onkeyup="updateRowAmount('+i+')" value="'+(addRowType == 'duplicate' ? $(lastRate).val(): '')+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.totalPrice" name="product['+i+'].totalPrice" required="required" type="text" value="0" readonly>'+
                        '</td>'+
                        '<td>'+
                            '<input type="button" value="-" id="productDel_'+i+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow('+i+')" style="margin-left: 18%;width: 60%;">'+
                        '</td>'+
                      '</tr>';

    $("#productDescTBody > tr:eq(0)").find('td:eq(8)').html('')
    $("#productDescTBody").append(prodDescRow)
    autoFocusProductDescField()
    $(document).scrollTop($(document).height())
}
function productDelRow(i) {
    if (i <= 0 ) return;
    console.log('delete: ' + i)
    var del = confirm('Do you want to delete '+(i ? i : "last")+' row?')
    if (!del){
        return;
    }

    var rowCount = $("#productDescTBody > tr").length
    if (rowCount > 2) {
        $("#productDescTBody > tr:eq("+(i ? i+1 : 'last')+")").remove()
        //if (rowCount > 3) {
        //    $("#productDescTBody > tr:"+(i ? i+1 : 'last')+" > td:eq(8)").html('<input type="button" value="-" id="productDel_'+(rowCount-1)+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow()" style="margin-left: 18%;width: 60%;">')
        //}
    }
    if (i && i > 0) {
        updateProdDetailsInputTagsIdAndName()
    }
    updateTotalAmount()
    //autoFocusProductDescField()
}
/**     remove all the challan row from tr>1 and set tr=1 value to ''    **/
function resetProdDetailsRow() {
    var row = 'productDescTBody > tr:eq(1)'

    $('#'+row).find('td:eq(1)').find('input:eq(0)').val('')
    $('#'+row).find('td:eq(1)').find('input:eq(1)').val('')
    $('#'+row).find('td:eq(1)').find('input:eq(2)').val('')
    $('#'+row).find('td:eq(2)').find('input:eq(0)').val('')
    //$('#'+row).find('td:eq(3)').find('input:eq(0)')
    //$('#'+row).find('td:eq(4)').find('select:eq(0)').val('')
    $('#'+row).find('td:eq(5)').find('input:eq(0)').val('')
    $('#'+row).find('td:eq(6)').find('input:eq(0)').val('')
    $('#'+row).find('td:eq(5)').find('input:eq(0)').val('')

    while ($('#productDescTBody > tr').length > 2) {
        $('#productDescTBody > tr:last').remove()
    }
}

/**             update all the <input> 'id' and 'name'              **/
function updateProdDetailsInputTagsIdAndName() {
    var rowIdSelector = 'productDescTBody > tr'

    for (var i = 2; i < $("#"+rowIdSelector).length; i++) {
        var row = rowIdSelector+':eq('+i+')'
        console.log('row-->', row)
        $('#'+row).find('td:eq(0)').html('<span id="product['+(i-1)+'].srNo">'+i+'</span>')
        $('#'+row).find('td:eq(1)').find('input:eq(0)').attr('id','product'+(i-1)+'.product.id').attr('name','product['+(i-1)+'].product.id')
        $('#'+row).find('td:eq(1)').find('input:eq(1)').attr('id','product'+(i-1)+'.product.active').attr('name','product['+(i-1)+'].product.active')
        $('#'+row).find('td:eq(1)').find('input:eq(2)').attr('id','product'+(i-1)+'.product.name').attr('name','product['+(i-1)+'].product.name').attr('onkeyup','autoSearchProduct(event,this,'+(i-1)+')')
        $('#'+row).find('td:eq(2)').find('input:eq(0)').attr('id','product'+(i-1)+'.chNo').attr('name','product['+(i-1)+'].chNo').attr('onkeyup','autoSearchChallanNo(event, this, '+(i-1)+')')
        $('#'+row).find('td:eq(3)').find('input:eq(0)').attr('id','product'+(i-1)+'.hsn').attr('name','product['+(i-1)+'].hsn')
        $('#'+row).find('td:eq(4)').find('select:eq(0)').attr('id','product'+(i-1)+'.unitOfMeasure.id').attr('name','product['+(i-1)+'].unitOfMeasure.id')
        $('#'+row).find('td:eq(5)').find('input:eq(0)').attr('id','product'+(i-1)+'.quantity').attr('name','product['+(i-1)+'].quantity')
        $('#'+row).find('td:eq(6)').find('input:eq(0)').attr('id','product'+(i-1)+'.rate').attr('name','product['+(i-1)+'].rate')
        $('#'+row).find('td:eq(7)').find('input:eq(0)').attr('id','product'+(i-1)+'.totalPrice').attr('name','product['+(i-1)+'].totalPrice')
        try {
            $('#'+row).find('td:eq(8)').find('input:eq(0)').attr('id','productDel_'+(i-1)).attr('onclick','productDelRow('+(i-1)+')')
        } catch (err) {
            console.log('error in change id for productDelRow ',err)
        }
    }
}


/**             Auto focus to last product name fields              **/
function autoFocusProductDescField() {
    $(document).scrollTop($(document).height())
    if ($("#product"+rowCount+"\\.id").val()) {
    var rowCount = $("#productDescTBody > tr").length - 2
        $("#product"+rowCount+"\\.product\\.name").focus()
    } else {
        $('#addDuplicate').focus()
    }
}

$('#loadChallan').on('click',function(){
    $('#loadChallan').addClass("disabled")
    getProductDetailsByCompanyId()
})

/**             get Challans by Company/billToParty                    **/
function getProductDetailsByCompanyId(companyId) {
    $('.challanLoader').addClass('spinner-border');
    companyId = companyId ? companyId : $("#billToParty\\.id").val();
    console.log('inside getProductRate(): compId-'+companyId)
    if (companyId != '' && companyId != 0) {
        $.ajax({
            url : "${pageContext.request.contextPath}/productDetail/getByParty?partyId="+companyId,
            success : function(data) {
                if (data != '' && data != null && data.length > 0) {
                    createProductDetailsRow(data)
                    $('.challanLoader').removeClass('spinner-border')
                } else {
                    //resetProdDetailsRow()
                    $('.challanLoader').removeClass('spinner-border')
                    autoFocusProductDescField()
                }
            },
            error : function(err) {
                //TODO: create Empty Row
                $('.challanLoader').removeClass('spinner-border')
                resetProdDetailsRow()
                console.error(err)
            }
        })
    }
}

function createProductDetailsRow(prodDetails) {
    //console.log(prodDetails)
    var unitOfMeasureHtml = $("#product0\\.unitOfMeasure\\.id").html();
    var prdId = $('#productDescTBody > tr:last').find('td:eq(1)').find('input:eq(0)').val()
    var prdName = $('#productDescTBody > tr:last').find('td:eq(1)').find('input:eq(2)').val()
    if (prdId == '' || prdName == '') {
        $("#productDescTBody > tr:last").remove()
    }
    var rowNum = $('#productDescTBody > tr').length -1

    if (prodDetails != '' && prodDetails.length > 0 ) {
    var prodDescRow = "";
    for (var i = 0; i < prodDetails.length; i++ ) {
    prodDescRow = prodDescRow + '<tr>'+
                        '<td>'+
                            '<span id="product['+(i+rowNum)+'].srNo">'+(i+1+rowNum)+'</span>'+
                            //'<input id="product'+(i+rowNum)+'.id" name="product['+(i+rowNum)+'].id" type="hidden" value="'+prodDetails[i].id+'">'+
                            '<input id="product'+(i+rowNum)+'.party.id" name="product['+(i+rowNum)+'].party.id" type="hidden" value="'+prodDetails[i].party.id+'">'+
                        '</td>'+
                        '<td>'+
                            '<input type="hidden" id="product'+(i+rowNum)+'.product.id" name="product['+(i+rowNum)+'].product.id" value="'+prodDetails[i].product.id+'">'+
                            '<input type="hidden" id="product'+(i+rowNum)+'.product.active" name="product['+(i+rowNum)+'].product.active" value="'+prodDetails[i].product.active+'">'+
                            '<input id="product'+(i+rowNum)+'.product.name" name="product['+(i+rowNum)+'].product.name" required="required" onkeyup="autoSearchProduct(event,this,'+(i+rowNum)+')" type="text" value="'+prodDetails[i].product.name+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+(i+rowNum)+'.chNo" name="product['+(i+rowNum)+'].chNo" required="required" type="text" class="numbersOnly" value="'+prodDetails[i].chNo+'" onkeyup="autoSearchChallanNo(event, this, '+(i+rowNum)+')">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+(i+rowNum)+'.product.hsn" name="product['+(i+rowNum)+'].product.hsn" required="required" type="text" value="'+prodDetails[i].product.hsn+'" style="width: 100%;">'+
                        '</td>'+
                        '<td>'+
                            '<select id="product'+(i+rowNum)+'.unitOfMeasure.id" name="product['+(i+rowNum)+'].unitOfMeasure.id" required="required">'+
                                unitOfMeasureHtml+
                            '</select>'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+(i+rowNum)+'.quantity" name="product['+(i+rowNum)+'].quantity" required="required" type="text" class="numbersOnly" onkeyup="updateRowAmount('+(i+rowNum)+')" value="'+prodDetails[i].quantity+'" >'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+(i+rowNum)+'.rate" name="product['+(i+rowNum)+'].rate" required="required" type="text" class="numbersOnly" onkeyup="updateRowAmount('+(i+rowNum)+')" value="'+(prodDetails[i].rate ? prodDetails[i].rate : "0")+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+(i+rowNum)+'.totalPrice" name="product['+(i+rowNum)+'].totalPrice" required="required" type="text" value="'+(prodDetails[i].totalPrice ? prodDetails[i].totalPrice : "0")+'" readonly>'+
                        '</td>'+
                        '<td>'+
                            '<input type="button" value="-" id="productDel_'+(i+rowNum)+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow('+(i+rowNum)+')" style="margin-left: 18%;width: 60%;">'+
                        '</td>'+
                      '</tr>';
    }
    }

    $("#productDescTBody").append(prodDescRow)
    $("#productDescTBody > tr:eq(1)").find('td:eq(8)').html('')
    for (var i = rowNum; i < rowNum+prodDetails.length; i++ ) {
        updateRowAmount(i)
    }
    autoFocusProductDescField()
    $(document).scrollTop($(document).height())

}

    /**                 calculation script                  **/
function updateRowAmount(index) {

    var quantity = $("#product"+index+"\\.quantity").val()
    var rate= $("#product"+index+"\\.rate").val()
    var amount = quantity * rate
    amount = parseFloat(amount.toFixed(2))
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
    $("#totalAmount").val(totalAmount.toFixed(2))

    let gstPerc = parseFloat($('#gstPerc').val())
    if (isNaN(gstPerc))
        gstPerc = 0
    var gst = parseFloat((totalAmount*(gstPerc/100)).toFixed(2));
    $("#cGst").val(parseFloat(gst/2))
    $("#sGst").val(parseFloat(gst/2))

    var totalTaxAmount = gst
    $("#totalTaxAmount").val(totalTaxAmount)
    $('#iGst').val(totalTaxAmount)
    //console.log('totalAmount: '+totalAmount+', gst: '+gst+', totalTaxAmount: '+totalTaxAmount)

    var pnfCharge = parseFloat($("#pnfCharge").val())
    var totalAmountAfterTax = totalAmount + totalTaxAmount
    var roundedTotalAmountAfterTax = Math.round(totalAmountAfterTax)

    var roundOff = (roundedTotalAmountAfterTax-totalAmountAfterTax).toFixed(2)
    $("#roundOff").val(roundOff)
    //console.log('pnfCharge: '+pnfCharge+', totalAmountAfterTax: '+totalAmountAfterTax)
    //adding the pnfCharge after round off
    roundedTotalAmountAfterTax += pnfCharge;
    $("#totalAmountAfterTax").val(roundedTotalAmountAfterTax)

    inWords(roundedTotalAmountAfterTax)
}

var a = ['','One ','Two ','Three ','Four ', 'Five ','Six ','Seven ','Eight ','Nine ','Ten ','Eleven ','Twelve ','Thirteen ','Fourteen ','Fifteen ','Sixteen ','Seventeen ','Eighteen ','Nineteen '];
var b = ['', '', 'Twenty','Thirty','Forty','Fifty', 'Sixty','Seventy','Eighty','Ninety'];

function inWords (num) {
    var str = '';
    if ((num = num.toString()).length > 9) {
        str = 'overflow';
    } else {
        n = ('000000000' + num).substr(-9).match(/^(\d{2})(\d{2})(\d{2})(\d{1})(\d{2})$/);
        if (!n) {
            str = '';
        } else {
            str += (n[1] != 0) ? (a[Number(n[1])] || b[n[1][0]] + ' ' + a[n[1][1]]) + 'Crore ' : '';
            str += (n[2] != 0) ? (a[Number(n[2])] || b[n[2][0]] + ' ' + a[n[2][1]]) + 'Lakh ' : '';
            str += (n[3] != 0) ? (a[Number(n[3])] || b[n[3][0]] + ' ' + a[n[3][1]]) + 'Thousand ' : '';
            str += (n[4] != 0) ? (a[Number(n[4])] || b[n[4][0]] + ' ' + a[n[4][1]]) + 'Hundred ' : '';
            str += (n[5] != 0) ? ((str != '') ? 'And ' : '') + (a[Number(n[5])] || b[n[5][0]] + ' ' + a[n[5][1]]) + '' : '';
        }
    }
    if (str === '') {
        $("#totalInvoiceAmountInWords").val('')
    } else if (str === 'overflow') {
        $("#totalInvoiceAmountInWords").val('overflow...!!!')
    } else {
        $("#totalInvoiceAmountInWords").val(str+'Only.')
    }
}
function demoInvoicePrint() {
    $("#invoiceCommand").attr("action","${pageContext.request.contextPath}/invoices/demoInvoicePrint");
    $("#invoiceCommand").submit();
}

function hasDuplicateChNos() {
    //console.log('duplicate chNos validation')
    var tempArr = new Array();
    var hasDupli = false;
    $("input[id$='.chNo']").each(function(k,v){
        if (v.value === '')
            return;
        if (tempArr.indexOf(v.value) >= 0) {
            hasDupli = true;
        } else {
            tempArr.push(v.value)
        }
    })
    //console.log(tempArr)
    return hasDupli
}

function updateGstPerc(value) {
    $(".gstPercentage:eq(0)").text(parseFloat(value/2).toFixed(2))
    $(".gstPercentage:eq(1)").text(parseFloat(value/2).toFixed(2))
    $(".gstPercentage:eq(2)").text(parseFloat(value).toFixed(2))
    updateTotalAmount()
}
</script>
<script src="${pageContext.request.contextPath}/js/invoice.js" ></script>
<%@ include file="./common/footer.jspf" %>