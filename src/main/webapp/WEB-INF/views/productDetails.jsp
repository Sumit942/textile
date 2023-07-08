<%@ include file="./common/header.jspf" %>
<style>
</style>
<body>
    <h1>Student Details</h1>
    <form:form method="POST" action="/submit" modelAttribute="productDetailsCommand">
        <table id="productDetailsTable" class="table table-striped table-bordered">
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
            <tbody>
                <c:choose>
                    <c:when test="${empty productDetailsCommand.productDetails}">
                        <tr>
                            <td>
                                <span id="productDetails[0].srNo">1</span>
                                <!-- <form:hidden path="productDetails[0].id" /> -->
                            </td>
                            <td>
                                <form:hidden path="productDetails[0].product.id"/>
                                <form:input path="productDetails[0].product.name" required="true" onkeyup="autoSearchProduct(event, this,0)" class="ui-autocomplete-input" autocomplete="off"/>
                                <form:errors path="productDetails[0].product.name" cssClass="error"/>
                            </td>
                            <td>
                                <form:input path="productDetails[0].chNo" class="numbersOnly" style="width: 100%;"/>
                                <form:errors path="productDetails[0].chNo" cssClass="error"/>
                            </td>
                            <td>
                                <form:input path="productDetails[0].product.hsn" class="numbersOnly" required="true" style="width: 100%;"/>
                                <form:errors path="productDetails[0].product.hsn" cssClass="error"/>
                            </td>
                            <td>
                                <form:select path="productDetails[0].unitOfMeasure.id" required="true">
                                    <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure" itemValue="id"/>
                                </form:select>
                                <form:errors path="productDetails[0].unitOfMeasure" cssClass="error"/>
                            </td>
                            <td>
                                <form:input path="productDetails[0].quantity" required="true" class="numbersOnly" onkeyup="updateRowAmount(0)" style="width: 100%;"/>
                                <form:errors path="productDetails[0].quantity" cssClass="error"/>
                            </td>
                            <td>
                                <form:input path="productDetails[0].rate" required="true" class="numbersOnly" onkeyup="updateRowAmount(0)" style="width: 100%;"/>
                                <form:errors path="productDetails[0].rate" cssClass="error"/>
                            </td>
                            <td>
                                <form:input path="productDetails[0].totalPrice" required="true" readonly="true"/>
                                <form:errors path="productDetails[0].totalPrice" cssClass="error"/>
                            </td>
                            <td>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${invoiceCommand.product}" var="productDetails" varStatus="index">
                                <tr>
                                    <td>
                                        <span id="productDetails[${index.index}].srNo">${index.index + 1}</span>
                                        <!-- <form:hidden path="productDetails[${index.index}].id" /> -->
                                    </td>
                                    <td>
                                        <form:hidden path="productDetails[${index.index}].product.id" />
                                        <form:input path="productDetails[${index.index}].product.name" required="true" onkeyup="autoSearchProduct(event,this,${index.index})"/>
                                        <form:errors path="productDetails[${index.index}].product.name" cssClass="error"/>
                                    </td>
                                    <td>
                                        <form:input path="productDetails[${index.index}].chNo" class="numbersOnly" style="width: 100%;"/>
                                        <form:errors path="productDetails[${index.index}].chNo" cssClass="error"/>
                                    </td>
                                    <td>
                                        <form:input path="productDetails[${index.index}].product.hsn" required="true" style="width: 100%;"/>
                                        <form:errors path="productDetails[${index.index}].product.hsn" cssClass="error"/>
                                    </td>
                                    <td>
                                        <form:select path="productDetails[${index.index}].unitOfMeasure.id" required="true">
                                            <form:options items="${unitOfMeasures}" itemValue="id" itemLabel="unitOfMeasure"/>
                                        </form:select>
                                        <form:errors path="productDetails[${index.index}].unitOfMeasure" cssClass="error"/>
                                    </td>
                                    <td>
                                        <form:input path="productDetails[${index.index}].quantity" required="true" class="numbersOnly" onkeyup="updateRowAmount(${index.index})" style="width: 100%;"/>
                                        <form:errors path="productDetails[${index.index}].quantity" cssClass="error"/>
                                    </td>
                                    <td>
                                        <form:input path="productDetails[${index.index}].rate" required="true" class="numbersOnly" onkeyup="updateRowAmount(${index.index})" style="width: 100%;"/>
                                        <form:errors path="productDetails[${index.index}].rate" cssClass="error"/>
                                    </td>
                                    <td>
                                        <form:input path="productDetails[${index.index}].totalPrice" readonly="true" required="true"/>
                                        <form:errors path="productDetails[${index.index}].totalPrice" cssClass="error"/>
                                    </td>
                                    <td>
                                        <c:if test="${index.index == invoiceCommand.product.size()-1}">
                                            <input  type="button" value="-" id="productDel_${index.index}" class="btn btn-sm btn-danger rounded" onclick="productDelRow()" style="margin-left: 18%;width: 60%;"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>                        
                    </c:otherwise>
                    </c:choose>
            </tbody>
        </table>
        <button type="button" class="btn btn-primary" onclick="addRow('new')">Add Row</button>
        <button type="submit" class="btn btn-success">Submit</button>
    </form:form>


    <script>
        $(document).ready(function () {
            $('#productDetailsTable').DataTable();
        });

        function addRow(addRowType) {
            var i = $("#productDetailsTable tbody > tr").length;
                //check if the last row data is entered or not
                var lastPrdId = '#productDetails'+(i-1)+'\\.product\\.id'
                var lastPrdName = '#productDetails'+(i-1)+'\\.product\\.name'
                var lastChNo = '#productDetails'+(i-1)+'\\.chNo'
                if ( $(lastPrdName).val() == '' ) {
                    alert ("Please enter 'Product Description' in last row")
                    $(lastPrdName).focus()
                    return;
                }
                var lastHsn = '#productDetails'+(i-1)+'\\.product\\.hsn'
                if ( $(lastHsn).val() == '' ) {
                    alert ("Please enter 'HSN' in last row")
                    $(lastHsn).focus()
                    return;
                }
                var lastUom = '#productDetails'+(i-1)+'\\.unitOfMeasure\\.id'
                if ( $(lastUom).val() == '' ) {
                    alert ("Please enter 'Unit Of Measure' in last row")
                    $(lastUom).focus()
                    return;
                }
                var lastQty = '#productDetails'+(i-1)+'\\.quantity'
                if ( $(lastQty).val() == '' ) {
                    alert ("Please enter 'Quantity' in last row")
                    $(lastQty).focus()
                    return;
                }
                var lastRate = '#productDetails'+(i-1)+'\\.rate'
                if ( $(lastRate).val() == '' ) {
                    alert ("Please enter 'Rate' in last row")
                    $(lastRate).focus()
                    return;
                }
                /*if ( $('#productDetails'+(i-1)+'\\.totalPrice').val() == '' ) {
                    alert ("Please enter 'Total Price' in last row")
                    return;
                }*/
                //check duplicate chNos
                if (hasDuplicateChNos()){
                    alert ("Please entry unique 'challan no.' in last row")
                    $(lastChNo).focus()
                    return;
                }

            var row = '<tr>' +
                        '<td>'+
                            '<span id="productDetails['+i+'].srNo">'+(i+1)+'</span>'+
                            //'<input id="productDetails'+i+'.id" name="productDetails['+i+'].id" type="hidden" value="">'+
                        '</td>'+
                        '<td>'+
                            '<input type="hidden" name="productDetails['+i+'].product.id" value="">'+
                            '<input id="productDetails'+i+'.product.name" name="productDetails['+i+'].product.name" required="required" onkeyup="autoSearchProduct(event,this,'+i+')" type="text" value="'+(addRowType == 'duplicate' ? $(lastPrdName).val() : '')+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="productDetails'+i+'.chNo" name="productDetails['+i+'].chNo" type="text" class="numbersOnly" value="'+($(lastChNo).val() != '' ? (parseInt($(lastChNo).val())+1) : '')+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="productDetails'+i+'.product.hsn" name="productDetails['+i+'].product.hsn" required="required" type="text" value="6006" style="width: 100%;">'+
                        '</td>'+
                        '<td>'+
                            '<select id="productDetails'+i+'.unitOfMeasure.id" name="productDetails['+i+'].unitOfMeasure.id" required="required">'+
                                $("#productDetails0\\.unitOfMeasure\\.id").html()+
                            '</select>'+
                        '</td>'+
                        '<td>'+
                            '<input id="productDetails'+i+'.quantity" name="productDetails['+i+'].quantity" required="required" type="text" class="numbersOnly" onkeyup="updateRowAmount('+i+')" >'+
                        '</td>'+
                        '<td>'+
                            '<input id="productDetails'+i+'.rate" name="productDetails['+i+'].rate" required="required" type="text" class="numbersOnly" value="'+(addRowType == 'duplicate' ? $(lastRate).val(): '')+'">'+
                        '</td>'+
                        '<td>'+
                            '<input id="productDetails'+i+'.totalPrice" name="productDetails['+i+'].totalPrice" required="required" type="text" value="0" readonly>'+
                        '</td>'+
                        '<td>'+
                            '<input type="button" value="-" id="productDel_'+i+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow()" style="margin-left: 18%;width: 60%;">'+
                        '</td>'+
                      '</tr>';

            $('#productDetailsTable tbody').append(row);
        }

        function hasDuplicateChNos() {
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
        function autoSearchProduct(event,obj,index) {
        if (event.key == 'Enter') {
            return;
        }
        //$("#productDetails"+index+"\\.product\\.hsn").val('')
        $("#productDetails"+index+"\\.product\\.id").val('')
            $(obj).autocomplete({
                source : function(request, response) {
                    $.ajax({
                        url : "${pageContext.request.contextPath}/product/searchByName",
                        dataType : 'json',
                        data : {
                            name : request.term
                        },
                        success : function(data) {
                            //$("#productDetails"+index+"\\.product\\.hsn").val('')
                            $("#productDetails"+index+"\\.product\\.id").val('')
                            response(data);
                        },
                        error : function(err) {
                            //$("#productDetails"+index+"\\.product\\.hsn").val('')
                            $("#productDetails"+index+"\\.product\\.id").val('')
                            console.error(err)
                        }
                    });
                },
                minLength: 3,
                select : function(event, ui) {
                    this.value = ui.item.name
                    var productId = ui.item.id
                    //$("#productDetails"+index+"\\.product\\.hsn").val(ui.item.hsn)
                    $("#productDetails"+index+"\\.product\\.id").val(productId)
                    //get the max rate for company
                    $("#productDetails"+index+"\\.chNo").focus()
                    //if (productId != '') {
                    //    getProductMaxRateByCompanyId(index,productId)
                    //}
                    return false;
                }
            }).data("ui-autocomplete")._renderItem = function(ul, item) {
                return $("<li>").append(
                        "<a><strong>" + item.name + "</strong></a>").appendTo(ul);
            };
        }
    </script>
<%@ include file="./common/footer.jspf" %>
