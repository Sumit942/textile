<%@ include file="./common/header.jspf" %>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Add & View Challan</title>
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
div#productDescTBody .row .col-6,div#productDescTBody .row .col-12 {
    margin-bottom: 0.5rem;
}
div#productDescTBody .row:nth-child(odd) {
    background-color: #dee2e6;
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
            ${successMessage}
        </div>
    </c:if>
    <hr>
    <form:form method="POST" action="productDetail" modelAttribute="productDetailsCommand" onSubmit="return validateForm();">
        <table class="table table-striped table-bordered">
        <tr>
            <td>
                <form:input path="challanNos" class="numbersOnly form-control" placeholder="Challan Nos"/>
            </td>
            <sec:authorize access="hasRole('ADMIN')">
                <td>
                    <form:hidden path="company.id" />
                    <form:input path="company.name" placeholder="Company Name" onkeyup="billToPartyAutoComplete(event, this, -1)" class="form-control ui-autocomplete-input" autocomplete="off" />
                </td>
                <td>
                    <form:hidden path="product.id"/>
                    <form:input path="product.name" placeholder="Product Name" onkeyup="autoSearchProduct(event, this,-1)" class="form-control ui-autocomplete-input" autocomplete="off"/>
                </td>
            </sec:authorize>
            <td>
                <input type="submit" value="search" name="searchChallans" class="btn btn-primary"/>
            </td>
        </tr>
        </table>


        <div class="row mb-1">
            <div class="col-md-12 fs-1 fw-bold" style="text-align:center;">List of Challans</div>
        </div>

            <div id="productDescTBody" class="container">
                <c:choose>
                <c:when test="${empty productDetailsCommand.productDetails}">
                <div class="row">
                    <div class="col-12 text-center" style="border: 1px solid">
                        <span id="productDetails[0].srNo">1.</span>
                    </div>
                    <div class="col-6">
                        <form:hidden path="productDetails[0].id" />
                        <form:input path="productDetails[0].chNo" class="numbersOnly form-control" placeholder="Challan No"/>
                        <form:errors path="productDetails[0].chNo" cssClass="error"/>
                    </div>
                    <div class="col-6">
                        <form:input path="productDetails[0].challanDt" class="challanDt form-control" readonly="true" required="true" placeholder="Challan date"/>
                        <form:errors path="productDetails[0].challanDt" cssClass="error"/>
                    </div>
                    <div class="col-12">
                        <form:hidden path="productDetails[0].party.id" />
                        <form:input path="productDetails[0].party.name" required="true" onkeyup="billToPartyAutoComplete(event, this, 0)" class="form-control ui-autocomplete-input" autocomplete="off" placeholder="Party name"/>
                        <form:errors path="productDetails[0].party" cssClass="error"/>
                    </div>
                    <div class="col-12">
                        <form:hidden path="productDetails[0].product.id"/>
                        <form:input path="productDetails[0].product.name" onblur="preventProductAddManually(0)" required="true" onkeyup="autoSearchProduct(event, this,0)" class="form-control ui-autocomplete-input" autocomplete="off" placeholder="Quality"/>
                        <form:hidden path="productDetails[0].product.active" />
                        <form:errors path="productDetails[0].product.name" cssClass="error"/>
                    </div>
                    <div class="col-6">
                        <form:input path="productDetails[0].product.hsn" class="numbersOnly form-control" required="true" placeholder="HSN code"/>
                        <form:errors path="productDetails[0].product.hsn" cssClass="error"/>
                    </div>
                    <div class="col-6">
                        <form:select path="productDetails[0].unitOfMeasure.id" required="true" class="form-select">
                            <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure" itemValue="id"/>
                        </form:select>
                        <form:errors path="productDetails[0].unitOfMeasure" cssClass="error"/>
                    </div>
                    <div class="col-6">
                        <form:input path="productDetails[0].quantity" required="true" class="numbersOnly form-control" placeholder="Quantity"/>
                        <form:errors path="productDetails[0].quantity" cssClass="error"/>
                    </div>
                    <div class="col-6">
                        <form:input path="productDetails[0].rate" class="numbersOnly form-control" placeholder="Rate"/>
                        <form:errors path="productDetails[0].rate" cssClass="error"/>
                    </div>
                </div>
                </c:when>
                    <c:otherwise>
                    <c:forEach items="${productDetailsCommand.productDetails}" var="productDetails" varStatus="index">
                        <div class="row mb-1 mt-1">
                            <div class="col-12 text-center" style="border: 1px solid">
                                <span id="productDetails[${index.index}].srNo">${index.index + 1}.</span>
                            </div>
                            <div class="col-6">
                                <form:hidden path="productDetails[${index.index}].id" />
                                <form:input path="productDetails[${index.index}].chNo" class="numbersOnly form-control" placeholder="Challan No"/>
                                <form:errors path="productDetails[${index.index}].chNo" cssClass="error"/>
                            </div>
                            <div class="col-6">
                                <form:input path="productDetails[${index.index}].challanDt" class="challanDt form-control" readonly="true" required="true" placeholder="Challan date"/>
                                <form:errors path="productDetails[${index.index}].challanDt" cssClass="error"/>
                            </div>
                            <div class="col-12">
                                <form:hidden path="productDetails[${index.index}].party.id" />
                                <form:input path="productDetails[${index.index}].party.name" required="true" onkeyup="billToPartyAutoComplete(event, this, 0)" class="form-control ui-autocomplete-input" autocomplete="off" placeholder="Party name"/>
                                <form:errors path="productDetails[${index.index}].party" cssClass="error"/>
                            </div>
                            <div class="col-12">
                                <form:hidden path="productDetails[${index.index}].product.id"/>
                                <form:input path="productDetails[${index.index}].product.name" onblur="preventProductAddManually(0)" required="true" onkeyup="autoSearchProduct(event, this,0)" class="form-control ui-autocomplete-input" autocomplete="off" placeholder="Quality"/>
                                <form:hidden path="productDetails[${index.index}].product.active" />
                                <form:errors path="productDetails[${index.index}].product.name" cssClass="error"/>
                            </div>
                            <div class="col-6">
                                <form:input path="productDetails[${index.index}].product.hsn" class="numbersOnly form-control" required="true" placeholder="HSN code"/>
                                <form:errors path="productDetails[${index.index}].product.hsn" cssClass="error"/>
                            </div>
                            <div class="col-6">
                                <form:select path="productDetails[${index.index}].unitOfMeasure.id" required="true" class="form-select">
                                    <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure" itemValue="id"/>
                                </form:select>
                                <form:errors path="productDetails[${index.index}].unitOfMeasure" cssClass="error"/>
                            </div>
                            <div class="col-6">
                                <form:input path="productDetails[${index.index}].quantity" required="true" class="numbersOnly form-control" placeholder="Quantity"/>
                                <form:errors path="productDetails[${index.index}].quantity" cssClass="error"/>
                            </div>
                            <div class="col-6">
                                <form:input path="productDetails[${index.index}].rate" class="numbersOnly form-control" placeholder="Rate"/>
                                <form:errors path="productDetails[${index.index}].rate" cssClass="error"/>
                            </div>
                        </div>
                    </c:forEach>
                    </c:otherwise>
                </c:choose>

               <button type="submit" name="saveChallans" class="btn btn-success" value="saveChallans">Submit</button>
               <button type="button" class="btn btn-primary" onclick="addRow('new')">Add Row+</button>
               <button type="button" class="btn btn-primary" onclick="resetRow()">Reset</button>
            </div>

    </form:form>


<script>
$(document).ready(function () {
    $( ".challanDt" ).datepicker({
        responsive: true, // Enable responsive mode
        dateFormat: 'dd/mm/yy'
    })
});
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
                    name : request.term,
                    active: true
                },
                success : function(data) {
                    if (index < 0 ) {
                        $('#product\\.id').val('')
                    } else {
                        $("#productDetails"+index+"\\.product\\.id").val('')
                    }
                    response(data);
                },
                error : function(err) {
                    if (index < 0 ) {
                        $('#product\\.id').val('')
                    } else {
                        $("#productDetails"+index+"\\.product\\.id").val('')
                    }
                    console.error(err)
                }
            });
        },
        minLength: 3,
        select : function(event, ui) {
            this.value = ui.item.name
            var productId = ui.item.id
            if (index < 0 ) {
                $('#product\\.id').val(productId)
            } else {
                $("#productDetails"+index+"\\.product\\.id").val(productId)
                $("#productDetails"+index+"\\.product\\.active").val(ui.item.active)
                $("#productDetails"+index+"\\.quantity").focus()
                if (productId != '') {
                    getProductMaxRateByCompanyId(index,productId)
                }
            }

            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append(
                "<a class='dropdown-item'><strong>" + item.name + "</strong></a>").appendTo(ul);
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
                    if (i < 0 ) {
                        $('#company\\.id').val('')
                    } else {
                        $('#productDetails'+i+'\\.party\\.id').val('')
                    }
                    response(data);
                },
                error : function(err) {
                    if (i < 0 ) {
                        $('#company\\.id').val('')
                    } else {
                        $('#productDetails'+i+'\\.party\\.id').val('')
                    }
                    console.error(err)
                }
            });
        },
        minLength: 4,
        select : function(event, ui) {
            this.value = ui.item.name
            if (i < 0 ) {
                $('#company\\.id').val(ui.item.id)
            } else {
                $('#productDetails'+i+'\\.party\\.id').val(ui.item.id)
                $('#productDetails'+i+'\\.product\\.name').focus()
            }
            return false;
        }
    }).data("ui-autocomplete")._renderItem = function(ul, item) {
        return $("<li>").append("<a class='dropdown-item'><strong>" + item.name + "</strong> - " + item.gst + "</a>").appendTo(ul);
    };
}

/**  validate form   **/

$('input[name="searchChallans"], input[name="saveChallans"]').on('click',function(event) {
    var formElements = document.querySelectorAll("form input[type='text'], form textarea");

    if (event.target.name == 'searchChallans') {
        // Remove the 'required' attribute for the required fields
        formElements.forEach(function(element) {
            element.required = false;
        });
    } else {
        // Set the 'required' attribute for the required fields
        formElements.forEach(function(element) {
            console.log(element)
                element.required = true;
        });
    }
    return true;
})

function resetRow() {
var i = 0;

const mobileRow =
'   <div class="col-12 text-center" style="border: 1px solid">'
+'       <span id="productDetails[0].srNo">'+(i+1)+'.</span>'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.id" name="productDetails['+i+'].id" type="hidden" value="">'
+'       <input id="productDetails'+i+'.chNo" name="productDetails['+i+'].chNo" placeholder="Challan No" class="numbersOnly form-control" type="text" value="">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.challanDt" name="productDetails['+i+'].challanDt" placeholder="Challan date" class="challanDt form-control hasDatepicker" required="true" readonly="readonly" type="text" value="">'
+'   </div>'
+'   <div class="col-12">'
+'       <input id="productDetails'+i+'.party.id" name="productDetails['+i+'].party.id" type="hidden" value="20">'
+'       <input id="productDetails'+i+'.party.name" name="productDetails['+i+'].party.name" onkeyup="billToPartyAutoComplete(event, this, '+i+')" placeholder="Party name" class="form-control ui-autocomplete-input" required="true" type="text" value="" autocomplete="off">'
+'   </div>'
+'   <div class="col-12">'
+'       <input id="productDetails'+i+'.product.id" name="productDetails['+i+'].product.id" type="hidden" value="">'
+'       <input id="productDetails'+i+'.product.name" name="productDetails['+i+'].product.name" onkeyup="autoSearchProduct(event, this,'+i+')" placeholder="Quality" class="form-control ui-autocomplete-input" required="true" onblur="preventProductAddManually('+i+')" type="text" value="24s Cotton" autocomplete="off">'
+'       <input id="productDetails'+i+'.product.active" name="productDetails['+i+'].product.active" type="hidden" value="false">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.product.hsn" name="productDetails['+i+'].product.hsn" placeholder="HSN code" class="numbersOnly form-control" required="true" type="text" value="6006">'
+'   </div>'
+'   <div class="col-6">'
+'       <select id="productDetails'+i+'.unitOfMeasure.id" name="productDetails['+i+'].unitOfMeasure.id" class="form-select" required="true">'
+           $("#productDetails"+i+"\\.unitOfMeasure\\.id").html()
+'       </select>'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.quantity" name="productDetails['+i+'].quantity" placeholder="Quantity" class="numbersOnly form-control" required="true" type="text" value="">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.rate" name="productDetails['+i+'].rate" placeholder="Rate" class="numbersOnly form-control" type="text" value="">'
+'   </div>';


        $('#productDescTBody .row').html(mobileRow);
        $('#productDetails'+i+'\\.chNo').focus()
        $('.challanDt').datepicker({dateFormat: 'dd/mm/yy'});
}

//prevent manually adding product
function preventProductAddManually(index) {
    const prodId = $("#productDetails"+index+"\\.product\\.id").val();
    if (prodId == '') {
        console.log('removing product name from input as user has not selected')
        $("#productDetails"+index+"\\.product\\.name").val('');
    }
}

</script>
<%@ include file="./common/footer.jspf" %>
