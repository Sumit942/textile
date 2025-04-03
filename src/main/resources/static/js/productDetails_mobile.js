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
                url : "/textile/product/searchByName",
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
            url : "/textile/product/rateByProductAndCompanyId?companyId="+companyId+"&productId="+productId,
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
                url : "/textile/company/searchByName/"+request.term,
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
function addRow(addRowType) {

    var i = $("#productDescTBody .row").length;
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
        if ( $(lastPrdName).val() == '' ) {
            alert ("Please enter 'Product Description' in last row")
            $(lastPrdName).focus()
            return;
        }
        var lastChNo = '#productDetails'+(i-1)+'\\.chNo'
        if ( $(lastChNo).val() === '') {
            alert("Please enter 'Challan No' in last row")
            $(lastChNo).focus()
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
        var lastChallanDt = '#productDetails'+(i-1)+'\\.challanDt'
        if ( $(lastChallanDt).val() == '' ) {
            alert ("Please select 'Date' in last row")
            $(lastChallanDt).focus()
            return;
        }
        //check duplicate chNos
        if (hasDuplicateChNos()){
            alert ("Please entry unique 'challan no.' in last row")
            $(lastChNo).focus()
            return;
        }
const mobileRow =
'<div class="row">'
+'   <div class="col-12 text-center" style="border: 1px solid">'
+'       <span id="productDetails[0].srNo">'+(i+1)+'.</span>'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.id" name="productDetails['+i+'].id" type="hidden" value="">'
+'       <input id="productDetails'+i+'.chNo" name="productDetails['+i+'].chNo" placeholder="Challan No" class="numbersOnly form-control" type="text" value="'+($(lastChNo).val() != '' ? (parseInt($(lastChNo).val())+1) : '')+'">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.challanDt" name="productDetails['+i+'].challanDt" placeholder="Challan date" class="challanDt form-control" readonly="readonly" type="text" value="'+($(lastChallanDt).val() != '' ? $(lastChallanDt).val() : '')+'">'
+'   </div>'
+'   <div class="col-12">'
+'       <input id="productDetails'+i+'.party.id" name="productDetails['+i+'].party.id" type="hidden" value="">'
+'       <input id="productDetails'+i+'.party.name" name="productDetails['+i+'].party.name" onkeyup="billToPartyAutoComplete(event, this, '+i+')" placeholder="Party name" class="form-control ui-autocomplete-input" required="true" type="text" value="" autocomplete="off">'
+'   </div>'
+'   <div class="col-12">'
+'       <input id="productDetails'+i+'.product.id" name="productDetails['+i+'].product.id" type="hidden" value="">'
+'       <input id="productDetails'+i+'.product.name" name="productDetails['+i+'].product.name" onkeyup="autoSearchProduct(event, this,'+i+')" placeholder="Quality" class="form-control ui-autocomplete-input" required="true" onblur="preventProductAddManually('+i+')" type="text" value="" autocomplete="off">'
+'       <input id="productDetails'+i+'.product.active" name="productDetails['+i+'].product.active" type="hidden" value="false">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.product.hsn" name="productDetails['+i+'].product.hsn" placeholder="HSN code" class="numbersOnly form-control" required="true" type="text" value="6006">'
+'   </div>'
+'   <div class="col-6">'
+'       <select id="productDetails'+i+'.unitOfMeasure.id" name="productDetails['+i+'].unitOfMeasure.id" class="form-select" required="true">'
+           $("#productDetails0\\.unitOfMeasure\\.id").html()
+'       </select>'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.quantity" name="productDetails['+i+'].quantity" placeholder="Quantity" class="numbersOnly form-control" required="true" type="text" value="">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.rate" name="productDetails['+i+'].rate" placeholder="Rate" class="numbersOnly form-control" type="text" value="">'
+'   </div>'
+'</div>';
    $('#productDescTBody').append(mobileRow);
    $('#productDetails'+i+'\\.chNo').focus()
    $('#productDetails'+i+'\\.challanDt').datepicker({dateFormat: 'dd/mm/yy'});
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
function resetRow() {
const confirmReset = confirm('do you want to reset all the rows?')
if (!confirmReset) {
    return;
}
var i = 0;

const mobileRow =
'<div class="row">'
+'   <div class="col-12 text-center" style="border: 1px solid">'
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
+'       <input id="productDetails'+i+'.party.id" name="productDetails['+i+'].party.id" type="hidden" value="">'
+'       <input id="productDetails'+i+'.party.name" name="productDetails['+i+'].party.name" onkeyup="billToPartyAutoComplete(event, this, '+i+')" placeholder="Party name" class="form-control ui-autocomplete-input" required="true" type="text" value="" autocomplete="off">'
+'   </div>'
+'   <div class="col-12">'
+'       <input id="productDetails'+i+'.product.id" name="productDetails['+i+'].product.id" type="hidden" value="">'
+'       <input id="productDetails'+i+'.product.name" name="productDetails['+i+'].product.name" onkeyup="autoSearchProduct(event, this,'+i+')" placeholder="Quality" class="form-control ui-autocomplete-input" required="true" onblur="preventProductAddManually('+i+')" type="text" value="" autocomplete="off">'
+'       <input id="productDetails'+i+'.product.active" name="productDetails['+i+'].product.active" type="hidden" value="false">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.product.hsn" name="productDetails['+i+'].product.hsn" placeholder="HSN code" class="numbersOnly form-control" required="true" type="text" value="6006">'
+'   </div>'
+'   <div class="col-6">'
+'       <select id="productDetails'+i+'.unitOfMeasure.id" name="productDetails['+i+'].unitOfMeasure.id" class="form-select" required="true">'
+           $("#productDetails0\\.unitOfMeasure\\.id").html()
+'       </select>'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.quantity" name="productDetails['+i+'].quantity" placeholder="Quantity" class="numbersOnly form-control" required="true" type="text" value="">'
+'   </div>'
+'   <div class="col-6">'
+'       <input id="productDetails'+i+'.rate" name="productDetails['+i+'].rate" placeholder="Rate" class="numbersOnly form-control" type="text" value="">'
+'   </div>'
+'</div>';


        $('#productDescTBody').html(mobileRow);
        $('#productDetails'+i+'\\.chNo').focus()
        $('#productDetails'+i+'\\.challanDt').datepicker({dateFormat: 'dd/mm/yy'});
}

//prevent manually adding product
function preventProductAddManually(index) {
    const prodId = $("#productDetails"+index+"\\.product\\.id").val();
    if (prodId == '') {
        console.log('removing product name from input as user has not selected')
        $("#productDetails"+index+"\\.product\\.name").val('');
    }
}