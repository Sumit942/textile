<%@ include file="./common/header.jspf" %>
<style>
</style>
<body>
<%@ include file="./common/navigation.jspf" %>
<div class="container">
<table id="challanTable" class="table table-striped table-bordered">

    <thead>
        <tr><th class="text-center" colspan="20">Missing Challan Nos</th></tr>
    </thead>
    <tbody>
        <tr>
            <td colspan="10">min: ${minChallanNo}</td>
            <td colspan="10">max: ${maxChallanNo}</td>
        </tr>
        <tr>
        <c:forEach items="${missingChallanNos}" var="challanNo" varStatus="index">
            <td>${challanNo}</td>
            ${(index.index + 1) % 20 == 0 ? '</tr><tr>' : ''}
        </c:forEach>
        <c:forEach begin="1" end="${20 - missingChallanNos.size() % 20}" >
            <td></td>
        </c:forEach>
        </tr>
    </tbody>
    <tfoot>
        <tr><td class="text-center" colspan="20">--------------END------------</td></tr>
    </tfoot>
</table>
</div>
<script>
$(document).ready(function(e){
    $("#challanTable").DataTable({
        dom: 'Bfrtip',
        buttons: [
            'excelHtml5', 'csvHtml5', 'pdfHtml5'
        ]
    })
})
</script>
<%@ include file="./common/footer.jspf" %>