function searchByChallanNo(chNo) {
    const openCh = confirm("Do you want to open challan: " + chNo);
  
    if (!openCh) return;
  
    window.location.href = "/textile/productDetail?searchByCh=" + chNo;
  }
  
  function toggleChallans(divId) {
    $("#" + divId).toggle("fade");
  }
  
  $(".challanCancelBtn").on("click", function () {
    const challanNo = $(this).val();
    const parentElement = $(this).parent();
    let input = confirm("Do you want to delete challan: " + challanNo);
    if (input) {
      const data = {
        chNo: challanNo,
        product: {
          id: $('input[name="chCancelled"]').val(),
        },
      };
  
      const csrfToken = $('input[name="_csrf"]').val();
      const csrfHeader = $('input[name="_csrf_header"]').val();
  
      $.ajax({
        url: "/textile/productDetail/save",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
        beforeSend: function (xhr) {
          xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (response) {
          if (data != "") {
            console.log("chNo deleted: ", challanNo);
            parentElement.remove();
          }
        },
        error: function (error) {
          console.log("Error:", error);
        },
      });
    } else {
      console.log("chCancelled aborted");
    }
  });
  
  $(document).ready(function () {
    var popoverTriggerList = [].slice.call(
      document.querySelectorAll('[data-bs-toggle="popover"]')
    );
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
      return new bootstrap.Popover(popoverTriggerEl);
    });
  });
  