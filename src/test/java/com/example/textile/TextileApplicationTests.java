package com.example.textile;

import com.example.textile.entity.*;
import com.example.textile.enums.UserProfileType;
import com.example.textile.repo.*;
import com.example.textile.service.InvoiceService;
import com.example.textile.service.ProductDetailService;
import com.example.textile.service.UserService;
import com.example.textile.utility.ThymeleafTemplateUtility;
import com.lowagie.text.DocumentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;


@SpringBootTest
class TextileApplicationTests {

        @Autowired
    InvoiceService invoiceService;
    //@Autowired
    InvoiceViewRepository viewRepository;
    //    @Autowired
    InvoiceRepository invoiceRepo;
    //    @Autowired
    ProductRepository productRepo;
    //    @Autowired
    UnitRepository unitRepo;
    // @Autowired
    ThymeleafTemplateUtility templateUtility;
    //@Autowired
    UserService userService;

//    @Autowired
    ProductDetailService productDetailService;


    //@Test
    void getInvoiceStartCount() {
        System.out.println("invoiceStartCount>>> " + invoiceService.getLatestInvoiceNo());
    }

    //@Test
    void test_findByFieldNameLatest() {
//        List<BankStatement> byTxnDate = statementService.findAllOrderByAndLimit("insertDt", 0, 10);
//        byTxnDate.forEach(System.out::println);
    }

    //@Test
    void test_InvoiceView_withPageSize() {
//        viewRepository.findAll(Pageable.ofSize(10).getSort().and(Sort.by("invoiceNo").descending())).forEach(System.out::println);
        viewRepository.findAll(PageRequest.of(0, 10).withSort(Sort.by("invoiceNo").descending())).getContent().forEach(System.out::println);
    }

    //    @Test
    void getInvoiceStartCountFromYml() {
        System.out.println(invoiceService.getLatestInvoiceNo());
    }

    //@Test
    void testSaveInvoiceMapping() {
        System.out.println("SUMEET running checkSaveInvoiceService test1...");
        Invoice invoice = getInvoice();
        System.out.println("saving...sdf");
        System.out.println(invoice);

        invoiceService.saveOrUpdate(invoice);
        System.out.println(invoice);
        System.out.println("save succesfull!!");
    }

    Invoice getInvoice() {
        Country country = new Country();
        country.setId(1L);
        country.setName("INDIA");
        State state = new State();
        state.setId(1L);
        state.setName("Maharashtra");
        state.setCode(27);
        state.setCountry(country);
        Address address = new Address();
        address.setId(1L);
        address.setAddress("full test");
        address.setPinCode(400013);
        address.setState(state);
        BankDetail bankDetail = new BankDetail();
        bankDetail.setBankName("IDFC Bank");
        bankDetail.setBranch("New Thane");
        bankDetail.setAccountNo("***1239");
        bankDetail.setIfsc("IDFC000005");
        BankDetail bankDetail2 = new BankDetail();
        bankDetail.setBankName("HDFC Bank");
        bankDetail.setBranch("New Thane");
        bankDetail.setAccountNo("***7239");
        bankDetail.setIfsc("HDFC000005");
        List<BankDetail> bankDetails = new ArrayList<>();
        bankDetails.add(bankDetail);
        bankDetails.add(bankDetail2);
        Company company = new Company();
        company.setAddress(address);
        //company.setBankDetails(bankDetails);
        company.setId(11L);
        company.setName("SIMPANI");
        company.setGst("27ACBPS6136N1Z4");
        SaleType saleType = new SaleType();
        saleType.setId(1L);
        saleType.setSaleType("Fabric Job Work");
        TransportMode transportMode = new TransportMode();
        transportMode.setId(1L);
        transportMode.setMode("ROAD");

        List<ProductDetail> prodList = new ArrayList<>();
        ProductDetail productDetail = new ProductDetail();
        productDetail.setChNo(16L);
        productDetail.setQuantity(500.0);
        productDetail.setRate(15.0);
        productDetail.setTotalPrice(BigDecimal.ZERO);
        prodList.add(productDetail);
        Unit unit = new Unit();
        unit.setId(1L);
        unit.setUnitOfMeasure("KG");
        productDetail.setUnitOfMeasure(unit);
        Product product = new Product();
        product.setId(1L);
        product.setName("24s Cotton");
        product.setHsn(6006);
        productDetail.setProduct(product);

        Invoice invoice = new Invoice();
        invoice.setId(23L);
        invoice.setcGst(BigDecimal.valueOf(2500));
        invoice.setDateOfSupply(new Date());
        invoice.setInvoiceDate(new Date());
        invoice.setInvoiceNo("TEST003");
        invoice.setPlaceOfSupply("Bhiwandi");
        invoice.setPnfCharge(BigDecimal.ZERO);
        invoice.setReverseCharge("NO");
        invoice.setRoundOff(0.0);
        invoice.setsGst(BigDecimal.valueOf(2500));
        invoice.setTotalAmount(BigDecimal.valueOf(100000.00));
        invoice.setTotalAmountAfterTax(BigDecimal.valueOf(105000.00));
        invoice.setTotalInvoiceAmountInWords("One Lakh Five Thousand Only.");
        invoice.setTotalTaxAmount(BigDecimal.valueOf(105000.00));
        invoice.setVehicleNo("NA");
        invoice.setBillToParty(company);
        invoice.setSaleType(saleType);
        invoice.setShipToParty(company);
        invoice.setTransportMode(transportMode);
        invoice.setProduct(prodList);
        return invoice;
    }

    //    @Test
    void checkSaveInvoiceService() {

        System.out.println("SUMEET running checkSaveInvoiceService test...");
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());
        invoice.setTotalAmount(BigDecimal.valueOf(100000.00));
        invoice.setsGst(BigDecimal.valueOf(2500));
        invoice.setcGst(BigDecimal.valueOf(2500));
        invoice.setTotalTaxAmount(BigDecimal.valueOf(105000.00));
        invoice.setTotalAmountAfterTax(BigDecimal.valueOf(105000.00));
        invoice.setTotalInvoiceAmountInWords("One Lakh Five Thousand Only.");
        invoice.setPlaceOfSupply("Bhiwandi");

        System.out.println("Test run completed!!");
    }

    //    @Test
    void contextLoads() {
        String url = "http://localhost:8080/textile/invoices/save";

        System.out.println("SUMEET running test1...");
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());
        invoice.setTotalAmount(BigDecimal.valueOf(100000.00));
        invoice.setsGst(BigDecimal.valueOf(2500));
        invoice.setcGst(BigDecimal.valueOf(2500));
        invoice.setTotalTaxAmount(BigDecimal.valueOf(105000.00));
        invoice.setTotalAmountAfterTax(BigDecimal.valueOf(105000.00));
        invoice.setTotalInvoiceAmountInWords("One Lakh Five Thousand Only.");
        invoice.setPlaceOfSupply("Bhiwandi");

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Invoice> request = new HttpEntity<>(invoice);


        ResponseEntity<Invoice> invoiceRes = restTemplate.postForEntity(url, request, Invoice.class);

        System.out.println(invoiceRes);

    }

    //@Test
    void updateInvoiceTest() {

        Invoice invById = invoiceRepo.findById(23L).get();
        System.out.println("invoice getByID-1");
        System.out.println(invById);
        Unit uom = unitRepo.findById(1L).get();
        invById.setInvoiceNo("001-test-1");

        ProductDetail productDetail = new ProductDetail();
        Product product = productRepo.findById(2L).get();
        productDetail.setProduct(product);
        productDetail.setUnitOfMeasure(uom);
        productDetail.setChNo(1L);
        productDetail.setQuantity(100.0);
        productDetail.setRate(10.0);
        productDetail.setTotalPrice(BigDecimal.valueOf(1000));

        invById.getProduct().add(productDetail);

        System.out.println("saving....");
        invoiceRepo.save(invById);
    }

    //@Test
    void checkDoubleSaveInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId(23L);
        invoice.setInvoiceNo("test-2");
        invoice.setProduct(null);
        invoice.setBillToParty(null);
        invoice.setShipToParty(null);

//        invoice.setTransportMode();

        invoiceRepo.save(invoice);
    }

    //@Test
    void findAllInvoiceView() {
        List<InvoiceView> view = viewRepository.findAll();
        view.forEach(e -> System.out.println(e.getId() + "," + e.getTotalTaxAmount() + "," + e.getInvoiceNo()));
    }

    // @Test
    void testPdfDownload_Invoice() throws FileNotFoundException, DocumentException {
        String invoiceNo = "SRTI/22-23/009";
        List<Invoice> invoices = invoiceService.findByInvoiceNo(invoiceNo);
        if (invoices != null && !invoices.isEmpty()) {
            Invoice invoice = invoices.get(0);
            String process = templateUtility.getProcessedTemplate("emailTemplates/SRTI_Invoice.html", "invoice", invoice);
            System.out.println(process);
//            File file = new File("invoice_009.pdf");
//            OutputStream os = new FileOutputStream(file);
//            PdfUtility.createPdf(os,process);
        }
    }

    //@Test
    void test_creatUser() {
        UserProfile userProfile = userService.findByType(UserProfileType.USER.getUserProfileType());

        User user = new User();
        user.setFirstName("");
        user.setLastName("");
        user.setUserName("");
        user.setPassword("");
        user.setEmail("@gmail.com");
        user.setUserProfiles(new HashSet<>(Arrays.asList(userProfile)));

        System.out.println(userService.saveOrUpdate(user));
    }

    /*@Test
    void read_pdf() {
        String filePath = "C:/Users/acer/Downloads/SRTI-FJW-026.pdf";
        try (PDDocument document = PDDocument.load(new File(filePath))) {

            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                System.out.println("Text:\n" + pdfFileInText);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
