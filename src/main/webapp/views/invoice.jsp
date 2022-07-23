<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>
<form:form action="save" method="POST" modelAttribute="invoiceCommand">
<div class="container">
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
                <form:options items="${transportModes}" itemLabel="mode" />
            </form:select>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:label path="invoiceDate">Invoice date:</form:label>
            <form:input path="invoiceDate" />
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
        </div>
        <div class="col-md-6">
        <form:label path="dateOfSupply">Date of Supply:</form:label>
            <form:input path="dateOfSupply" />
        </div>
    </div>
    <div class="row">
         <div class="col-md-6">
            <form:label path="invoiceBy.address.state.name">State:</form:label>
            <form:input path="invoiceBy.address.state.name" />
         </div>
         <div class="col-md-6">
            <form:label path="placeOfSupply">Place of Supply:</form:label>
            <form:input path="placeOfSupply" />
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
        </div>
        <div class="col-md-6">
            <form:input path="shipToParty.id" />
            <form:label path="shipToParty.name">Name:</form:label>
            <form:input path="shipToParty.name" />
        <div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:hidden path="billToParty.address.id" />
            <form:label path="billToParty.address.address">Name:</form:label>
            <form:input path="billToParty.address.address" />
            <form:input path="billToParty.address.pincode" />
        </div>
        <div class="col-md-6">
            <form:input path="shipToParty.address.id" />
            <form:label path="shipToParty.address.address">Name:</form:label>
            <form:input path="shipToParty.address.address" />
        <div>
    </div>


    <form:input path="billToParty.gst" />
    <form:input path="billToParty.address.state.name" />
    <form:input path="billToParty.address.state.code" />


    <form:input path="shipToParty.gst" />
    <form:input path="shipToParty.address.state.name" />
    <form:input path="shipToParty.address.state.code" />

    <c:if test="${!empty invoiceCommand.product}">
        <c:forEach items="product" var="particular" varStatus="index">
            <span id="product[${index}].srNo">${index}</span>
            <form:hidden path="product[${index}].id" />

            <form:hidden path="product[${index}].product.id" />
            <form:input path="product[${index}].product.name" />
            <form:input path="product[${index}].chNo" />
            <form:input path="product[${index}].product.hsn" />

            <form:select path="product[${index}].unitOfMeasure">
                <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure"/>
            </form:select>

            <form:input path="product[${index}].quantity" />
            <form:input path="product[${index}].rate" />
            <form:input path="product[${index}].totalPrice" />
        </c:forEach>
    </c:if>
    <c:if test="${empty invoiceCommand.product}">
        <span id="product[0].srNo">1</span>
        <form:hidden path="product[0].id" />

        <form:hidden path="product[0].product.id" />
        <form:input path="product[0].product.name" />
        <form:input path="product[0].chNo" />
        <form:input path="product[0].product.hsn" />

        <form:select path="product[0].unitOfMeasure">
            <form:options items="${unitOfMeasures}" itemLabel="unitOfMeasure"/>
        </form:select>

        <form:input path="product[0].quantity" />
        <form:input path="product[0].rate" />
        <form:input path="product[0].totalPrice" />
    </c:if>

    <form:input path="pnfCharge"/>
    <form:input path="totalAmount"/>
    <form:input path="cGst"/>
    <form:input path="sGst"/>
    <form:input path="totalTaxAmount"/>
    <form:input path="roundOff"/>
    <form:input path="totalAmountAfterTax"/>
    <form:input path="totalInvoiceAmountInWords"/>
    <form:input path="roundOff"/>
    <form:input path="roundOff"/>

    <form:input path="invoiceBy.bankDetails[0].bankName"/>
    <form:input path="invoiceBy.bankDetails[0].accountNo"/>
    <form:input path="invoiceBy.bankDetails[0].ifsc"/>
</div>
</form:form>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>