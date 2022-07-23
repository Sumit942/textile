<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
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
                <form:options items="${transportModes}" itemLabel="mode" />
            </form:select>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">

            <form:label path="invoiceDate">Invoice date:</form:label>
            <fmt:formatDate pattern="dd/MM/yyyy" value="${invoiceCommand.invoiceDate}" var="invoiceDateFormatted"/>
            <input name="invoiceDate" value="${invoiceDateFormatted}"/>
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
            <form:hidden path="shipToParty.id" />
            <form:label path="shipToParty.name">Name:</form:label>
            <form:input path="shipToParty.name" />
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:hidden path="billToParty.address.id" />
            <form:label path="billToParty.address.address">Address:</form:label>
            <form:input path="billToParty.address.address" />
            <form:hidden path="billToParty.address.pinCode" />
        </div>
        <div class="col-md-6">
            <form:hidden path="shipToParty.address.id" />
            <form:label path="shipToParty.address.address">Address:</form:label>
            <form:input path="shipToParty.address.address" />
            <form:hidden path="shipToParty.address.pinCode" />
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <form:label path="billToParty.gst">GSTIN:</form:label>
            <form:input path="billToParty.gst" />
        </div>
        <div class="col-md-6">
            <form:label path="shipToParty.gst">GSTIN:</form:label>
            <form:input path="shipToParty.gst" />
        </div>
    </div>
    <div class="row">
        <div class="col-md-3">
            <form:hidden path="billToParty.address.state.id" />
            <form:hidden path="billToParty.address.state.country.id" />
            <form:label path="billToParty.address.state.name">State:</form:label>
            <form:input path="billToParty.address.state.name" />
        </div>
        <div class="col-md-3">
            <form:label path="billToParty.address.state.code">Code:</form:label>
            <form:input path="billToParty.address.state.code" />
        </div>
        <div class="col-md-3">
            <form:label path="shipToParty.address.state.name">State:</form:label>
            <form:input path="shipToParty.address.state.name" />
        </div>
        <div class="col-md-3">
            <form:label path="shipToParty.address.state.code">Code:</form:label>
            <form:input path="shipToParty.address.state.code" />
        </div>
    </div>

    <c:if test="${empty invoiceCommand.product}">
    <div class="row">
        <div class="col-md-1">
            <span id="product[0].srNo">1</span>
            <input type="hidden" name="product[0].id" />
        </div>
        <div class="col-md-2">
            <input type="hidden" name="product[0].product.id" />
            <input type="text" name="product[0].product.name" />
            <input type="text" name="product[0].chNo" />
        </div>
        <div class="col-md-2">
            <input type="text" name="product[0].product.hsn" />
        </div>
        <div class="col-md-1">
            <select path="product[0].unitOfMeasure">
                <c:forEach items="${unitOfMeasures}" var="uom">
                    <option value="${uom.id}">${uom.unitOfMeasure}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
            <input type="text" name="product[0].quantity" />
        </div>
        <div class="col-md-2">
            <input type="text" name="product[0].rate" />
        </div>
        <div class="col-md-2">
            <input type="text" name="product[0].totalPrice" />
        </div>
    </div>
    </c:if>

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
    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-2">Packing & Forwarding Charges</div>
        <div class="col-md-2"></div>
        <div class="col-md-1"></div>
        <div class="col-md-2"></div>
        <div class="col-md-2"></div>
        <div class="col-md-2">
            <form:input path="pnfCharge"/>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8">
            <span>Total Invoice amount in words</span>
        </div>
        <div class="col-md-2">
            <span>Total Amount</span>
        </div>
        <div class="col-md-2">
            <form:input path="totalAmount"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <form:input path="totalInvoiceAmountInWords"/>
        </div>
        <div class="col-md-2">
            <span>Add: CGST 2.5%</span>
        </div>
        <div class="col-md-2">
            <form:input path="cGst"/>
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
        </div>
    </div>
    <input type="Submit"/>
</div>
</form:form>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>