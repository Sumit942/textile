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
    <form:form method="POST" action="productDetail" modelAttribute="productDetailsCommand">
        <table id="productDetailsTable" class="table table-striped table-bordered">
            <thead>
                <tr>
                  <th style="width: 5%;">Sr.No</th>
                  <th style="width: 20%;">Party Name</th>
                  <th style="width: 20%;">Product Description</th>
                  <th style="width: 10%;">Challan No</th>
                  <th style="width: 10%;">HSN code</th>
                  <th style="width: 5%;">UOM</th>
                  <th style="width: 15%;">Quantity</th>
                  <th style="width: 5%;">Rate</th>
                  <th style="width: 5%;"></th>
                </tr>
            </thead>
            <tbody id="productDescTBody">
                <c:choose>
                    <c:when test="${empty productDetailsCommand.productDetails}">
                        <tr>
                            <td>
                                <span id="productDetails[0].srNo">1</span>
                                <!-- <form:hidden path="productDetails[0].id" /> -->
                            </td>
                            <td>
                                <form:hidden path="productDetails[0].party.id" />
                                <form:input path="productDetails[0].party.name" required="true" onkeyup="billToPartyAutoComplete(event, this, 0)" class="ui-autocomplete-input" autocomplete="off"/>
                                <form:errors path="productDetails[0].party" cssClass="error"/>
                            </td>
                            <td>
                                <form:hidden path="productDetails[0].product.id"/>
                                <form:input path="productDetails[0].product.name" required="true" onkeyup="autoSearchProduct(event, this,0)" class="ui-autocomplete-input" autocomplete="off"/>
                                <form:hidden path="productDetails[0].product.active" />
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
                                <form:input path="productDetails[0].quantity" required="true" class="numbersOnly" style="width: 100%;"/>
                                <form:errors path="productDetails[0].quantity" cssClass="error"/>
                            </td>
                            <td>
                                <form:input path="productDetails[0].rate" class="numbersOnly" style="width: 100%;"/>
                                <form:errors path="productDetails[0].rate" cssClass="error"/>
                            </td>
                            <td>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${productDetailsCommand.productDetails}" var="productDetails" varStatus="index">
                                <tr>
                                    <td>
                                        <span id="productDetails[${index.index}].srNo">${index.index + 1}</span>
                                        <form:hidden path="productDetails[${index.index}].id" />
                                    </td>
                                    <td>
                                        <form:hidden path="productDetails[${index.index}].party.id" />
                                        <form:input path="productDetails[${index.index}].party.name" required="true" onkeyup="billToPartyAutoComplete(event, this, ${index.index})"/>
                                        <form:errors path="productDetails[${index.index}].party" cssClass="error"/>
                                    </td>
                                    <td>
                                        <form:hidden path="productDetails[${index.index}].product.id" />
                                        <form:input path="productDetails[${index.index}].product.name" required="true" onkeyup="autoSearchProduct(event,this,${index.index})"/>
                                        <form:hidden path="productDetails[${index.index}].product.active" />
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
                                        <form:input path="productDetails[${index.index}].rate" class="numbersOnly" style="width: 100%;"/>
                                        <form:errors path="productDetails[${index.index}].rate" cssClass="error"/>
                                    </td>
                                    <td>
                                        <c:if test="${index.index > 0}">
                                            <input  type="button" value="-" id="productDel_${index.index}" class="btn btn-sm btn-danger rounded" onclick="productDelRow(${index.index})" style="margin-left: 18%;width: 60%;"/>
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
            var lastPartyId = '#productDetails'+(i-1)+'\\.party\\.id'
            var lastPartyName = '#productDetails'+(i-1)+'\\.party\\.name'
            if ( $(lastPartyName).val() == '') {
                alert("Please enter 'Party Name' in last row")
                $(lastPartyName).focus()
                return;
            }
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
                        '<input id="productDetails'+i+'.party.id" name="productDetails['+i+'].product.party.id" type="hidden" value="'+(addRowType == 'duplicate' ? $(lastPartyId).val() : '')+'">'+
                        '<input id="productDetails'+i+'.party.name" name="productDetails['+i+'].product.party.name" required="required" onkeyup="billToPartyAutoComplete(event,this,'+i+')" type="text" value="'+(addRowType == 'duplicate' ? $(lastPartyName).val() : '')+'">'+
                    '</td>'+
                    '<td>'+
                        '<input type="hidden" id="productDetails='+i+'.product.id" name="productDetails['+i+'].product.id" value="'+(addRowType == 'duplicate' ? $(lastPrdId).val() : '')+'">'+
                        '<input id="productDetails'+i+'.product.name" name="productDetails['+i+'].product.name" required="required" onkeyup="autoSearchProduct(event,this,'+i+')" type="text" value="'+(addRowType == 'duplicate' ? $(lastPrdName).val() : '')+'">'+
                        '<input type="hidden" id="productDetails'+i+'.product.active" name="productDetails['+i+'].product.active />'+
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
                        '<input id="productDetails'+i+'.rate" name="product['+i+'].rate" required="required" type="text" class="numbersOnly" style="width: 100%;" value="'+(addRowType == 'duplicate' ? $(lastRate).val(): '')+'">'+
                    '</td>'+
                    '<td>'+
                        '<input type="button" value="-" id="productDel_'+i+'" class="btn btn-sm btn-danger rounded" onclick="productDelRow('+i+')" style="margin-left: 18%;width: 60%;">'+
                    '</td>'+
                  '</tr>';

        $('#productDetailsTable tbody').append(row);
        $('#productDetails'+i+'.party.name').focus()
    }

function productDelRow(i) {
    console.log('delete: ' + i)
    var del = confirm('Do you want to delete '+(i ? i : "last")+' row?')
    if (!del){
        return;
    }

    var rowCount = $("#productDescTBody > tr").length
    if (rowCount > 1) {
        $("#productDescTBody > tr:eq("+i+")").remove()
        updateProdDetailsInputTagsIdAndName()
    }
}
function updateProdDetailsInputTagsIdAndName() {
    var rowIdSelector = 'productDescTBody > tr'

    for (var i = 1; i < $("#"+rowIdSelector).length; i++) {
        var row = rowIdSelector+':eq('+i+')'
        console.log('row-->', row)
        $('#'+row).find('td:eq(0)').html('<span id="productDetails['+(i-1)+'].srNo">'+i+'</span>')
        $('#'+row).find('td:eq(0)').find('input:eq(1)').attr('id','productDetails'+(i-1)+'.id').attr('name','product['+(i-1)+'].id')
        $('#'+row).find('td:eq(1)').find('input:eq(0)').attr('id','product'+(i-1)+'.party.id').attr('name','product['+(i-1)+'].party.id')

        $('#'+row).find('td:eq(2)').find('input:eq(0)').attr('id','product'+(i-1)+'.product.id').attr('name','product['+(i-1)+'].product.id')
        $('#'+row).find('td:eq(2)').find('input:eq(1)').attr('id','product'+(i-1)+'.product.name').attr('name','product['+(i-1)+'].product.name')
        $('#'+row).find('td:eq(2)').find('input:eq(2)').attr('id','product'+(i-1)+'.product.active').attr('name','product['+(i-1)+'].product.active')

        $('#'+row).find('td:eq(3)').find('input:eq(0)').attr('id','product'+(i-1)+'.chNo').attr('name','product['+(i-1)+'].chNo')
        $('#'+row).find('td:eq(4)').find('input:eq(0)').attr('id','product'+(i-1)+'.hsn').attr('name','product['+(i-1)+'].hsn')
        $('#'+row).find('td:eq(5)').find('select:eq(0)').attr('id','product'+(i-1)+'.unitOfMeasure.id').attr('name','product['+(i-1)+'].unitOfMeasure.id')
        $('#'+row).find('td:eq(6)').find('input:eq(0)').attr('id','product'+(i-1)+'.quantity').attr('name','product['+(i-1)+'].quantity')
        $('#'+row).find('td:eq(7)').find('input:eq(0)').attr('id','product'+(i-1)+'.rate').attr('name','product['+(i-1)+'].rate')
        try {
            $('#'+row).find('td:eq(8)').find('input:eq(0)').attr('id','productDel_'+(i-1)).attr('onclick','productDelRow('+(i-1)+')')
        } catch (err) {
            console.log('error in change id for productDelRow ',err)
        }
    }
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
        return hasDupli
    }
    function autoSearchProduct(event,obj,index) {
        if (event.key == 'Enter') {
            return;
        }
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
                $("#productDetails"+index+"\\.product\\.id").val(productId)
                $("#productDetails"+index+"\\.chNo").focus()
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
function getProductMaxRateByCompanyId(index,productId) {

    var companyId = $("#productDetails"+index+"\\.party\\.id").val()
    var rate = $("#productDetails"+index+"\\.rate").val();
    console.log('inside getProductRate(): compId-'+companyId+', prodId-'+productId+', rate-'+rate)
    if (productId != '' && productId != 0 && rate == '') {
        $.ajax({
            url : "${pageContext.request.contextPath}/product/rateByProductAndCompanyId?companyId="+companyId+"&productId="+productId,
            success : function(data) {
                if (data != '') {
                    $("#productDetails"+index+"\\.rate").val(data)
                }
            },
            error : function(err) {
                $("#productDetails"+index+"\\.rate").val('')
                console.error(err)
            }
        })
    }
}
    /**                 billToParty AutoComplete functions                  **/
function billToPartyAutoComplete(event,thisObj,i) {
    if (event.key == 'Enter') {
        return
    }
    $(thisObj).autocomplete({
        source : function(request, response) {
            $.ajax({
                url : "${pageContext.request.contextPath}/company/searchByName/"+request.term,
                dataType : 'json',
                success : function(data) {
                    $('#productDetails'+i+'\\.party\\.id').val('')
                    response(data);
                },
                error : function(err) {
                    $('#productDetails'+i+'\\.party\\.id').val('')
                    console.error(err)
                }
            });
        },
        minLength: 4,
        select : function(event, ui) {
            this.value = ui.item.name
            $('#productDetails'+i+'\\.party\\.id').val(ui.item.id)
            $('#productDetails'+i+'\\.product\\.name').focus()
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
}

</script>
<%@ include file="./common/footer.jspf" %>
