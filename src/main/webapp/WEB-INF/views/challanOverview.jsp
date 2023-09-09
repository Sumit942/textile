<%@ include file="./common/header.jspf" %>
<title>Missing/UnBilled Challan</title>
<style>
</style>
<body>
<%@ include file="./common/navigation.jspf" %>
<div class="container-fluid">
<table id="missingChallanTable" class="table table-striped table-bordered">

    <thead>
        <tr><th class="text-center" colspan="15">Missing Challan Nos (${missingChallanNos.size()})</th></tr>
    </thead>
    <tbody>
        <tr>
            <td colspan="7"><b>min:</b> ${minChallanNo}</td>
            <td colspan="8"><b>max:</b> ${maxChallanNo}</td>
        </tr>
        <tr>
        <c:forEach items="${missingChallanNos}" var="challanNo" varStatus="index">
            <td>${challanNo}</td>
            ${(index.index + 1) % 15 == 0 ? '</tr><tr>' : ''}
        </c:forEach>
        <c:forEach begin="1" end="${15 - missingChallanNos.size() % 15}" >
            <td></td>
        </c:forEach>
        </tr>
    </tbody>
    <tfoot>
        <tr><td class="text-center" colspan="15">--------------END------------</td></tr>
    </tfoot>
</table>
<hr>
<table id="unBilledChallanTable" class="table table-striped table-bordered">

    <thead>
        <tr><th class="text-center" colspan="15">UnBilled Challan Nos (${unBilledChNo.size()})</th></tr>
    </thead>
    <tbody>
        <tr>
        <c:forEach items="${unBilledChNo}" var="productDetail" varStatus="index">
            <td>
                <span onclick="searchByChallanNo(${productDetail.chNo})" style="text-decoration: underline; cursor: pointer;">${productDetail.chNo}</span>
            </td>
            ${(index.index + 1) % 15 == 0 ? '</tr><tr>' : ''}
        </c:forEach>
        <c:forEach begin="1" end="${15 - unBilledChNo.size() % 15}" >
            <td></td>
        </c:forEach>
        </tr>
    </tbody>
    <tfoot>
        <tr><td class="text-center" colspan="15">--------------END------------</td></tr>
    </tfoot>
</table>
<hr>
<table id="yarnChallanTable" class="table table-striped table-bordered">

    <thead>
        <tr><th class="text-center" colspan="15">Yarn Return Challan Nos (${allExcludedChNo.size()})</th></tr>
    </thead>
    <tbody>
        <tr>
        <c:forEach items="${allExcludedChNo}" var="productDetail" varStatus="index">
            <td>
                <span onclick="searchByChallanNo(${productDetail.chNo})" style="text-decoration: underline; cursor: pointer;">${productDetail.chNo}</span>
            </td>
            ${(index.index + 1) % 15 == 0 ? '</tr><tr>' : ''}
        </c:forEach>
        <c:forEach begin="1" end="${15 - allExcludedChNo.size() % 15}" >
            <td></td>
        </c:forEach>
        </tr>
    </tbody>
    <tfoot>
        <tr><td class="text-center" colspan="15">--------------END------------</td></tr>
    </tfoot>
</table>
</div>
<script>
$(document).ready(function(e){
    
})

function searchByChallanNo(chNo) {
    const openCh = confirm('Do you want to open challan: '+chNo)

    if (!openCh)
        return;

    window.location.href = '${pageContext.request.contextPath}/productDetail?searchByCh='+chNo;
}
</script>
<%@ include file="./common/footer.jspf" %>