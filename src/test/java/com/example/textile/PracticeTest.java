package com.example.textile;

import com.example.textile.entity.*;
import com.example.textile.enums.OrderStatusType;
import com.example.textile.repo.CompanyYarnOrderRepository;
import com.example.textile.repo.OrdersRepo;
import com.example.textile.repo.YarnOrderItemProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class PracticeTest {

    public static void main(String[] args) {
        Long value = Long.valueOf("0");
        Long input = Long.valueOf("-1");
        Long input1 = Long.valueOf("0");
        Long input2 = Long.valueOf("1");

        System.out.println(input +".compareTo("+value+") -> " + input.compareTo(value));
        System.out.println(input1 +".compareTo("+value+") -> " + input1.compareTo(value));
        System.out.println(input2 +".compareTo("+value+") -> " + input2.compareTo(value));
    }

    @Autowired
    YarnOrderItemProductRepository yarnOrderProductRepo;

    @Autowired
    CompanyYarnOrderRepository companyYarnOrderRepo;

    @Autowired
    OrdersRepo ordersRepo;

    @Test
    public void testSaveYarnOrderItemProduct() {
        Product product = new Product();
        product.setName("1x1 PKk");
        product.setActive(true);
        product.setId(279L);


        YarnOrderItemProduct yarnOrderProduct = new YarnOrderItemProduct();
        yarnOrderProduct.setId(4L);
//        yarnOrderProduct.setProduct(product);
//        yarnOrderProduct.setGsm("234");
        yarnOrderProduct.setQuantity(142.32);
        System.out.println("saving: " + yarnOrderProduct);
        YarnOrderItemProduct savedYarnOrderItemProduct = yarnOrderProductRepo.save(yarnOrderProduct);

        System.out.println("saved: " + savedYarnOrderItemProduct);

//        Assertions.assertSame(yarnOrderProduct.getGsm(), savedYarnOrderItemProduct.getGsm());
        Assertions.assertTrue(savedYarnOrderItemProduct.getId() > 0);

    }

    @Test
    public void testSaveCompanyYarnOrders() {
        System.out.println("testSaveOrders()");
        Company company = new Company();
        company.setId(11L);
        Orders orders = getOrderData(company);
        orders.setId(1L);
        CompanyYarnOrder companyYarnOrder = getCompanyYarnOrder(orders);
        CompanyYarnOrder save = companyYarnOrderRepo.save(companyYarnOrder);
        System.out.println("saved: " + save);
    }

    Orders getOrderData(Company company) {
        Orders orders = new Orders();

        orders.setCompany(company);
        orders.setRemarks("Order not yet Received");
        orders.setOrderStatusType(OrderStatusType.RECEIVED);
        return orders;
    }

    CompanyYarnOrder getCompanyYarnOrder(Orders orders) {
        CompanyYarnOrder companyYarnOrder = new CompanyYarnOrder();

        companyYarnOrder.setOrders(orders);
        companyYarnOrder.setOrderDt(new Date());
        companyYarnOrder.setYarnInvoiceNo("KHDWL/24-25/046");
        companyYarnOrder.setRemark("Order Recieved");
        companyYarnOrder.setTotalQuantity(1340.00);
        return companyYarnOrder;
    }

    YarnOrderItem getYarnOrderItem(Yarn yarn, List<YarnOrderItemProduct> itemProducts) {
        YarnOrderItem yarnOrderItem = new YarnOrderItem();

        yarnOrderItem.setHsn("6001");
        yarnOrderItem.setLotNo("109236");
        yarnOrderItem.setBoxes(45);
        yarnOrderItem.setQuantity(1350.34);
        yarnOrderItem.setRate(130.00);
        yarnOrderItem.setYarn(yarn);
        yarnOrderItem.setAmount(BigDecimal.valueOf(175000));
        yarnOrderItem.setYarnOrderItemProducts(itemProducts);
        return yarnOrderItem;
    }

    YarnBuilty getYarnBuilty(CompanyYarnOrder yarnOrder, Company transportCompany){
        YarnBuilty yarnBuilty = new YarnBuilty();

        yarnBuilty.setCompanyYarnOrder(yarnOrder);
        yarnBuilty.setReceivedDt(new Date());
        yarnBuilty.setLoadUnloadCharges(3500.00);
        yarnBuilty.setBoxes(45);
        yarnBuilty.setTranportCompany(transportCompany);
        yarnBuilty.setVehicleNo("MH 04 RT 2531");
        yarnBuilty.setQuantity(1350.00);
        return yarnBuilty;
    }

    YarnOrderItemProduct getYarnOrderItemProduct(YarnOrderItem yarnOrderItem, YarnFabricDesign yarnFabricDesign, Machine machine) {
        YarnOrderItemProduct yarnOrderItemProduct = new YarnOrderItemProduct();

        yarnOrderItemProduct.setYarnOrderItem(yarnOrderItem);
        yarnOrderItemProduct.setYarnFabricDesign(yarnFabricDesign);
        yarnOrderItemProduct.setMachine(machine);
        yarnOrderItemProduct.setQuantity(1350.00);
        yarnOrderItemProduct.setRemarks("design mostly run on machine no 6, with lycra");
        return yarnOrderItemProduct;
    }

    YarnFabricDesign getYarnFabricDesign(FabricDesign fabricDesign, List<FabricDesignYarnMapping> designYarnMappings) {
        YarnFabricDesign yarnFabricDesign = new YarnFabricDesign();

        yarnFabricDesign.setYarns(designYarnMappings);
        yarnFabricDesign.setFabricDesign(fabricDesign);
        yarnFabricDesign.setGsm("195/200");
        return yarnFabricDesign;
    }


}
