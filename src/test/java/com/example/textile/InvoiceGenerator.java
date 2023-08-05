package com.example.textile;

import com.example.textile.entity.*;
import com.example.textile.repo.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
public class InvoiceGenerator {

    public static void main(String args[]) {
        String fileName = "invoice1.pdf";

        try {
            SpringTemplateEngine templateEngine = createTemplateEngine();

            Invoice invoice = createInvoice();

            Context context = new Context();
            context.setVariable("invoice", invoice);

            String htmlContent = templateEngine.process("SRTI_Invoice_new", context);

            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();

            System.out.println("Proforma Invoice generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Invoice createInvoice() {
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
        address.setAddress("fulls test");
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
        company.setBankDetails(bankDetails);
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
        invoice.setSelectedBank(bankDetail);
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

    static class Item {
        private String name;
        private int quantity;
        private BigDecimal price;

        public Item(String name, int quantity, BigDecimal price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
    private static SpringTemplateEngine createTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }
    private static ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    private static BigDecimal calculateTotal(List<Item> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (Item item : items) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        return total;
    }
}
