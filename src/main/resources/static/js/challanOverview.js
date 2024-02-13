function searchByChallanNo(chNo) {
    const openCh = confirm('Do you want to open challan: '+chNo)

    if (!openCh)
        return;

    window.location.href = '/textile/productDetail?searchByCh='+chNo;
}