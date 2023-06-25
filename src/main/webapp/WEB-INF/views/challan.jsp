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
<form:form name="challan" action="${pageContext.request.contextPath}/challan" method="POST" modelAttribute="challanCommand">
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
            challan ${challanCommand.challan.getchallanNo()} Save Successfully!!
        </div>
    </c:if>
    <div class="row mb-1">
        <div class="col-md-12 fs-1 fw-bold" style="text-align:center;">Delivery Challan</div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <form:hidden path="challan.id"/>
            <form:label path="challan.challanNo" class="col-md-3">Challan No:</form:label>
            <form:input path="challan.challanNo" class="col-md-3"/>
            <form:errors path="challan.challanNo" cssClass="error" class="col-md-3"/>
        </div>
        <div class="col-md-6 border">
            <form:label path="challan.chDate" class="col-md-3">challan date:</form:label>
            <fmt:formatDate pattern="dd/MM/yyyy" value="${challanCommand.challan.chDate}" var="challanDateFormatted"/>
            <input name="challan.chDate" id="challan.chDate" value="${challanDateFormatted}" class="col-md-3" readonly/>
            <form:errors path="challan.chDate" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <form:hidden path="challan.partyName.id" />
            <form:label path="challan.partyName.name" class="col-md-3">Party Name:</form:label>
            <form:input path="challan.partyName.name" onkeyup="billToPartyAutoComplete(event,this);" class="col-md-3"/>
        </div>
        <div class="col-md-6 border">
            <form:label path="challan.deliveryAddress" class="col-md-3">Delivery address:</form:label>
            <form:textarea path="challan.deliveryAddress" class="col-md-3"/>
        </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <form:label path="challan.transportName" class="col-md-3">Transport Name:</form:label>
            <form:input path="challan.transportName" class="col-md-3"/>
            <form:errors path="challan.transportName" cssClass="error"/>
        </div>
        <div class="col-md-6 border">
            <form:label path="challan.yarn[0].id" class="col-md-3">Yarn:</form:label>
            <form:select path="challan.yarn[0].id" class="col-md-3">
                <form:option value="" >--Select--</form:option>
                <form:options items="${yarns}" itemValue="id" itemLabel="yarn" />
            </form:select>
            <form:errors path="challan.yarn[0].id" cssClass="error"/>
        </div>
    </div>
    <div class="row mb-1">
         <div class="col-md-6 border">
            <form:label path="challan.gsm" class="col-md-3">GSM:</form:label>
            <form:input path="challan.gsm" class="col-md-3"/>
            <form:errors path="challan.gsm" cssClass="error"/>
         </div>
         <div class="col-md-6 border">
            <form:label path="challan.machine.id" class="col-md-3">Machine:</form:label>
            <form:select path="challan.machine.id" class="col-md-3">
                <form:option value="" >--Select--</form:option>
                <form:options items="${machines}" itemValue="id" itemLabel="machineNo" />
            </form:select>
            <form:errors path="challan.machine" cssClass="error"/>

         </div>
    </div>
    <div class="row mb-1">
        <div class="col-md-6 border">
            <form:label path="challan.finishDia" class="col-md-3">Finish Dia:</form:label>
            <form:input path="challan.finishDia" class="col-md-3"/>
            <form:errors path="challan.finishDia" cssClass="error"/>
         </div>
         <div class="col-md-6 border">
            <form:label path="challan.fabricDesign.id" class="col-md-3">Quality:</form:label>
            <form:select path="challan.fabricDesign.id" class="col-md-3">
                <form:option value="" >--Select--</form:option>
                <form:options items="${fabricDesigns}" itemValue="id" itemLabel="design" />
            </form:select>
            <form:errors path="challan.fabricDesign.id" cssClass="error"/>
         </div>
         <div class="col-md-6 border">
             <form:label path="challan.quantity" class="col-md-3">Quantity:</form:label>
             <form:input path="challan.quantity" class="col-md-3"/>
             <form:errors path="challan.quantity" cssClass="error"/>
          </div>
    </div>


    <input type="Submit" class="btn btn-primary" />
</div>
</form:form>
<script>

$(document).ready(function() {

    $( "#challan\\.chDate" ).datepicker({
       dateFormat: 'dd/mm/yy'
    })
    if ($("#id").val() == '' ) {
        $("#challan\\.chDate").focus()
    }

    $(".numbersOnly").on("keypress paste",function(e){
        var charCode = (e.which) ? e.which : event.keyCode
        //console.log('charCode: '+charCode+', event: '+e.type)
        if (e.type == "paste")
            e.preventDefault()

        if (String.fromCharCode(charCode).match(/[^0-9+?.+0-9$]/g))
            return false
    })


});// document.ready function ends

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
                    response(data);
                },
                error : function(err) {
                    console.error(err)
                    $("#challan\\.partyName\\.id").val()
                }
            });
        },
        minLength: 4,
        select : function(event, ui) {
            this.value = ui.item.name
            $("#challan\\.partyName\\.id").val(ui.item.id)
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
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
                        '</td>'+
                        '<td>'+
                            '<input type="hidden" name="product['+i+'].product.id" value="">'+
                            '<input id="product'+i+'.product.name" name="product['+i+'].product.name" required="required" onkeyup="autoSearchProduct(event,this,'+i+')" type="text" value="'+(addRowType == 'duplicate' ? $(lastPrdName).val() : '')+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="product'+i+'.chNo" name="product['+i+'].chNo" type="text" class="numbersOnly" value="'+($(lastChNo).val() != '' ? (parseInt($(lastChNo).val())+1) : '')+'">'+
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
                            '<input type="button" value="-" id="productDel_'+i+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow()" style="margin-left: 18%;width: 60%;">'+
                        '</td>'+
                      '</tr>';

    $("#productDescTBody > tr:eq("+i+")").find('td:eq(8)').html('')
    $("#productDescTBody").append(prodDescRow)
    autoFocusProductDescField()
    $(document).scrollTop($(document).height())
}
function productDelRow() {
    var del = confirm('Do you want to delete the row?')
    if (!del){
        return;
    }

    var rowCount = $("#productDescTBody > tr").length
    if (rowCount > 2) {
        $("#productDescTBody > tr:last").remove()
        if (rowCount > 3) {
            $("#productDescTBody > tr:last > td:eq(8)").html('<input type="button" value="-" id="productDel_'+(rowCount-1)+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow()" style="margin-left: 18%;width: 60%;">')
        }
    }
}
</script>
<%@ include file="./common/footer.jspf" %>