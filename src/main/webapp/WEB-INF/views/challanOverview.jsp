<%@ include file="./common/header.jspf" %>
<title>Missing/UnBilled Challan</title>
<style>
</style>
<body>
<%@ include file="./common/navigation.jspf" %>
<div class="container-fluid">
<table id="missingChallanTable" class="table table-striped table-bordered">

    <thead>
        <tr><th class="text-center" colspan="20">Missing Challan Nos (${missingChallanNos.size()})</th></tr>
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
<c:if test="${!showGroupByParty}">
<table id="unBilledChallanTable" class="table table-striped table-bordered">

    <thead>
        <tr><th class="text-center" colspan="20">UnBilled Challan Nos (${unBilledChNo.size()})</th></tr>
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
    <table id="yarnChallanTable" class="table table-striped table-bordered">
        <thead>
        <tr><th class="text-center" colspan="15">Yarn Return Challan Nos (${allExcludedChNo.size()})</th></tr>
        </thead>
        <tbody>
            <tr>
        <c:forEach items="${allExcludedChNo}" var="productDetail" varStatus="index">
                    <td>
                        <span onclick="searchByChallanNo(${productDetail.chNo})"
                            style="text-decoration: underline; cursor: pointer;">${productDetail.chNo}</span>
                    </td>
                    ${(index.index + 1) % 15 == 0 ? '
            </tr>
            <tr>' : ''}
                </c:forEach>
        <c:forEach begin="1" end="${15 - allExcludedChNo.size() % 15}" >
                    <td></td>
                </c:forEach>
            </tr>
        </tbody>
        <tfoot>
            <tr>
                <td class="text-center" colspan="15">--------------END------------</td>
            </tr>
        </tfoot>
    </table>
</c:if>
<c:if test="${showGroupByParty}">
    <div class="unBilledChallanDiv">
        <h5 class="text-center">------------UnBilled Challan Nos (${unBilledChNo.size()})------------</h5>
        <div class="accordion" id="accordionExample1">
            <c:forEach items="${unBilledChNoByPartyName}" var="unBilledPartyName" varStatus="unBilledPartyNameIndex">
                <div class="accordion-item">
                    <h2 class="accordion-header" id="heading_${unBilledPartyNameIndex.index}">
                        <button class="accordion-button ${unBilledPartyNameIndex.index == 0 ? '' : 'collapsed'}" type="button"
                            data-bs-toggle="collapse" data-bs-target="#collapse_${unBilledPartyNameIndex.index}"
                            aria-expanded="${unBilledPartyNameIndex.index == 0 ? 'true' : 'false'}"
                            aria-controls="collapse_${unBilledPartyNameIndex.index}">
                            ${unBilledPartyName.key} (${unBilledPartyName.value.size()})
                        </button>
                    </h2>
                    <div id="collapse_${unBilledPartyNameIndex.index}"
                        class="accordion-collapse collapse ${unBilledPartyNameIndex.index == 0 ? 'show' : ''}"
                        aria-labelledby="heading_${unBilledPartyNameIndex.index}" data-bs-parent="#accordionExample1">
                        <div class="accordion-body">
                            <table class="table table-striped table-bordered">
                                <tbody>
                                    <tr>
                                        <c:forEach items="${unBilledPartyName.value}" var="unBilledPartyNameChNo" varStatus="index">
                                            <td>
                                                <span onclick="searchByChallanNo(${unBilledPartyNameChNo.chNo})" style="text-decoration: underline; cursor: pointer;">${unBilledPartyNameChNo.chNo}</span>
                                            </td>
                                            ${(index.index + 1) % 15 == 0 ? '</tr><tr>' : ''}
                                        </c:forEach>
                                        <c:forEach begin="1" end="${15 - unBilledPartyName.value.size() % 15}" >
                                            <td></td>
                                        </c:forEach>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</br></br>
    <div class="yarnReturnDiv">
        <h5 class="text-center">------------Yarn Return Challan Nos (${yarnReturnChNo.size()})------------</h5>
        <div class="accordion" id="accordionExample2">
            <c:forEach items="${yarnReturnChNoByPartyName}" var="yarnReturnPartyName" varStatus="yarnReturnPartyNameIndex">
                <div class="accordion-item">
                    <h2 class="accordion-header" id="heading_return_${yarnReturnPartyNameIndex.index}">
                        <button class="accordion-button ${yarnReturnPartyNameIndex.index == 0 ? '' : 'collapsed'}" type="button"
                            data-bs-toggle="collapse" data-bs-target="#collapse_return_${yarnReturnPartyNameIndex.index}"
                            aria-expanded="${yarnReturnPartyNameIndex.index == 0 ? 'true' : 'false'}"
                            aria-controls="collapse_return_${yarnReturnPartyNameIndex.index}">
                            ${yarnReturnPartyName.key} (${yarnReturnPartyName.value.size()})
                        </button>
                    </h2>
                    <div id="collapse_return_${yarnReturnPartyNameIndex.index}"
                        class="accordion-collapse collapse ${yarnReturnPartyNameIndex.index == 0 ? 'show' : ''}"
                        aria-labelledby="heading_return_${yarnReturnPartyNameIndex.index}" data-bs-parent="#accordionExample2">
                        <div class="accordion-body">
                            <table class="table table-striped table-bordered">
                                <tbody>
                                    <tr>
                                        <c:forEach items="${yarnReturnPartyName.value}" var="yarnReturnPartyNameChNo" varStatus="index">
                                            <td>
                                                <span onclick="searchByChallanNo(${yarnReturnPartyNameChNo.chNo})" style="text-decoration: underline; cursor: pointer;">${yarnReturnPartyNameChNo.chNo}</span>
                                            </td>
                                            ${(index.index + 1) % 15 == 0 ? '</tr><tr>' : ''}
                                        </c:forEach>
                                        <c:forEach begin="1" end="${15 - yarnReturnPartyName.value.size() % 15}" >
                                            <td></td>
                                        </c:forEach>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

</div>
<script src="${pageContext.request.contextPath}/js/challanOverview.js"></script>
<%@ include file="./common/footer.jspf" %>