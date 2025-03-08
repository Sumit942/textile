package com.example.textile;

import com.example.textile.action.OrderSubmitAction;
import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.*;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.OrderStatusType;
import com.example.textile.executors.ActionResponse;
import com.example.textile.repo.CompanyYarnOrderRepository;
import com.example.textile.repo.OrdersRepository;
import com.example.textile.repo.YarnOrderItemProductRepository;
import com.example.textile.service.CompanyYarnOrderService;
import com.example.textile.service.OrdersService;
import com.example.textile.utility.ShreeramTextileConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
@Service
public class OrdersSaveTest {

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
    OrdersRepository ordersRepo;

    @Autowired
    OrdersService ordersService;

    @Autowired
    CompanyYarnOrderService yarnOrderService;

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void getIdAndOrderNo_by_OrderNo_like() {
        List<OrdersDto> test = ordersService.getIdAndOrderNoByOrderNoLike("MAK");
        if (!CollectionUtils.isEmpty(test)) {
            Assertions.assertTrue(test.get(0).getOrderNo().startsWith("MAK"));
        }
    }

    @Test
    void testCascadePersist_Orders() {
        Orders orders = ordersRepo.findById(2L).orElseThrow();

        CompanyYarnOrder companyYarnOrder = new CompanyYarnOrder();
        companyYarnOrder.setRemark("yarn order bill received 5");
        companyYarnOrder.setTotalQuantity(103.00);

        CompanyYarnOrder companyYarnOrder1 = new CompanyYarnOrder();
        companyYarnOrder1.setRemark("yarn order2 bill received 6");
        companyYarnOrder1.setTotalQuantity(154.40);

        orders.setCompanyYarnOrders(List.of(companyYarnOrder, companyYarnOrder1));

        ordersRepo.save(orders);
    }

    @Test
    void testCascadeRemove_Orders() {
//        Orders orders = ordersRepo.findById(4L).orElseThrow();
        ordersRepo.deleteById(4L);
    }

    @Test
    void testCascadeMerge_Orders() {
        System.out.println("fetching...");
        Orders orders = ordersRepo.findById(4L).orElseThrow();
        orders.setOrderStatusType(OrderStatusType.HOLD);
        for (CompanyYarnOrder companyYarnOrder : orders.getCompanyYarnOrders()) {
            companyYarnOrder.setTotalQuantity(companyYarnOrder.getTotalQuantity() + 1);
        }

        System.out.println("saving...");
        ordersRepo.save(orders);
    }

    @Test
    void testCascadeDetach_Orders() {
//        Orders orders = ordersRepo.findById(4L).orElseThrow();
        ordersRepo.deleteById(4L);
    }

    @Test
    public void testSaveCompanyYarnOrders() {
        System.out.println("testSaveOrders()");
//        Company company = new Company();
//        company.setId(11L);
        Orders orders = ordersRepo.findById(2L).orElseThrow();
        System.out.println("now printing");
        System.out.println(orders.getId());
        CompanyYarnOrder companyYarnOrder = getCompanyYarnOrder(orders);
        CompanyYarnOrder save = companyYarnOrderRepo.save(companyYarnOrder);
        System.out.println("saved: " + save);
    }

    @Test
    public void testSaveYarnOrderItemProduct() {
        Product product = new Product();
        product.setName("1x1 PKk");
        product.setActive(true);
        product.setId(279L);


        CompanyYarnOrderProduct yarnOrderProduct = new CompanyYarnOrderProduct();
        yarnOrderProduct.setId(4L);
//        yarnOrderProduct.setProduct(product);
//        yarnOrderProduct.setGsm("234");
//        yarnOrderProduct.setQuantity(142.32);
        System.out.println("saving: " + yarnOrderProduct);
        CompanyYarnOrderProduct savedCompanyYarnOrderProduct = yarnOrderProductRepo.save(yarnOrderProduct);

        System.out.println("saved: " + savedCompanyYarnOrderProduct);

//        Assertions.assertSame(yarnOrderProduct.getGsm(), savedYarnOrderItemProduct.getGsm());
        Assertions.assertTrue(savedCompanyYarnOrderProduct.getId() > 0);

    }

    @Test
    public void saveOrderEntity() {
        Orders orders = new Orders();
        orders.setId(2L);
        orders.setRemarks("dfd gayasdf");
        orders.setOrderStatusType(OrderStatusType.HALF_DELIVERED);
//        orders.setVersion(0L);

        /*CompanyYarnOrder companyYarnOrder = new CompanyYarnOrder();
        companyYarnOrder.setTotalAmount(BigDecimal.TEN);
        companyYarnOrder.setOrderDt(new Date());
        companyYarnOrder.setRemark("ni aya");
        companyYarnOrder.setYarnInvoiceNo("ABC/1234");
        companyYarnOrder.setTotalQuantity(100.00);

        YarnBuilty yarnBuilty = new YarnBuilty();
        yarnBuilty.setBoxes(25);
        yarnBuilty.setReceivedDt(new Date());
        yarnBuilty.setQuantity(100.00);
        yarnBuilty.setLoadUnloadCharges(200.00);
        yarnBuilty.setVehicleNo("MH 46 KK 1234");
        companyYarnOrder.setYarnBuilties(List.of(yarnBuilty));

        YarnOrderItem yarnOrderItem = getYarnOrderItem(getYarn(1L), null);
        companyYarnOrder.setYarnOrderItems(List.of(yarnOrderItem));

        orders.addCompanyYarnOrders(companyYarnOrder);*/

        ordersRepo.save(orders);
    }

    @Test
    public void submitOrder_new() {
        CompanyYarnOrderDto yarnOrderDto = new CompanyYarnOrderDto();
        yarnOrderDto.setId(32L);
        yarnOrderDto.setVersion(4L);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, -1);
        yarnOrderDto.setOrderDt(instance.getTime());
        yarnOrderDto.setRemark("Screenshot shared on whatsapp");
        yarnOrderDto.setYarnInvoiceNo("KE/002");
        yarnOrderDto.setTotalQuantity(549.530);
        yarnOrderDto.setTotalAmount(BigDecimal.valueOf(68884.00));
//        YarnOrderItem yarnOrderItem = getYarnOrderItem(getYarn(1L), null);
//        yarnOrderDto.setYarnOrderItems(List.of(yarnOrderItem));

        CompanyYarnOrderDto yarnOrderDto1 = new CompanyYarnOrderDto();
        yarnOrderDto1.setId(33L);
        yarnOrderDto1.setVersion(4L);
        instance.add(Calendar.DATE, -1);
        yarnOrderDto1.setOrderDt(instance.getTime());
        yarnOrderDto1.setRemark("Screenshot shared on whatsapp2");
        yarnOrderDto1.setYarnInvoiceNo("KE/001");
        yarnOrderDto1.setTotalQuantity(549.530);
        yarnOrderDto1.setTotalAmount(BigDecimal.valueOf(68884.00));
//        YarnOrderItem yarnOrderItem1 = getYarnOrderItem(getYarn(1L), null);
//        yarnOrderDto1.setYarnOrderItems(List.of(yarnOrderItem1));

        CompanyYarnOrderDto yarnOrderDto2 = new CompanyYarnOrderDto();
//        yarnOrderDto2.setId(33L);
//        yarnOrderDto2.setVersion(4L);
        instance.add(Calendar.DATE, -1);
        yarnOrderDto2.setOrderDt(instance.getTime());
        yarnOrderDto2.setRemark("Screenshot shared on whatsapp2");
        yarnOrderDto2.setYarnInvoiceNo("KE/003");
        yarnOrderDto2.setTotalQuantity(600.530);
        yarnOrderDto2.setTotalAmount(BigDecimal.valueOf(73884.00));


        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setId(8L);
        ordersDto.setVersion(5L);
        ordersDto.setRemarks("Order is in process");
        ordersDto.setOrderStatusType(OrderStatusType.IN_PROCESS);
        ordersDto.setCompanyYarnOrders(List.of(yarnOrderDto, yarnOrderDto1, yarnOrderDto2));


        OrderSubmitAction action = new OrderSubmitAction(ordersService, yarnOrderService);
        Map<String, Object> parameterMap = new HashMap<>();
        Map<String, String[]> errorMap = new HashMap<>();

        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        ActionResponse<OrdersDto> actionResponse = action.executeRest(ordersDto, parameterMap, errorMap);

        System.out.println("status : " + actionResponse.getResponseType());
        errorMap.forEach((key ,value) -> {
            System.out.println("key: " + key);
            System.out.println("value: " + Arrays.toString(value));
        });

        OrdersDto dbObj = actionResponse.getDbObj();

        System.out.println("responseObj: " + dbObj);
    }

    @Test
    public void submitOrder_existing() {
        OrdersDto ordersDto = ordersService.findById(5L);

        System.out.println("updating existing yarnOrder for company");
        ordersDto.getCompanyYarnOrders().get(0).setRemark("updating...");

        System.out.println("adding new yarnOrder for company");
        CompanyYarnOrderDto yarnOrderDto = new CompanyYarnOrderDto();
        ordersDto.getCompanyYarnOrders().add(yarnOrderDto);
//        yarnOrderDto.setId(13L);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, -1);
        instance.add(Calendar.DATE, -8);
        yarnOrderDto.setOrderDt(instance.getTime());
        yarnOrderDto.setRemark("Screenshot shared on whatsapp");
        yarnOrderDto.setYarnInvoiceNo("RSMW/0628");
        yarnOrderDto.setTotalQuantity(549.530);
        yarnOrderDto.setTotalAmount(BigDecimal.valueOf(68884.00));
        YarnOrderItem yarnOrderItem = getYarnOrderItem(getYarn(1L), null);
        yarnOrderItem.setLotNo("MD234");
        yarnOrderDto.setYarnOrderItems(List.of(yarnOrderItem));

        OrderSubmitAction action = new OrderSubmitAction(ordersService, yarnOrderService);
        Map<String, Object> parameterMap = new HashMap<>();
        Map<String, String[]> errorMap = new HashMap<>();

        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        ActionResponse actionResponse = action.executeRest(ordersDto, parameterMap, errorMap);

        System.out.println("status : " + actionResponse.getResponseType());
        errorMap.forEach((key ,value) -> {
            System.out.println("key: " + key);
            System.out.println("value: " + Arrays.toString(value));
        });

        Object dbObj = actionResponse.getDbObj();

        System.out.println("responseObj: " + dbObj);
    }

    @Test
    public void deleteOrder() {
        ordersRepo.deleteById(4L);
    }

    Orders getOrderData(Company company) {
        Orders orders = new Orders();

        orders.setCompany(company);
        orders.setRemarks("Order Created");
        orders.setOrderStatusType(OrderStatusType.RECEIVED);
        return orders;
    }

    CompanyYarnOrder getCompanyYarnOrder(Orders orders) {
        CompanyYarnOrder companyYarnOrder = new CompanyYarnOrder();

        companyYarnOrder.setOrder(orders);
        companyYarnOrder.setOrderDt(new Date());
        companyYarnOrder.setYarnInvoiceNo("KHDWL/24-25/052");
        companyYarnOrder.setRemark("Yarn bill send");
        companyYarnOrder.setTotalQuantity(1130.00);
        return companyYarnOrder;
    }

    YarnOrderItem getYarnOrderItem(Yarn yarn, List<CompanyYarnOrderProduct> itemProducts) {
        YarnOrderItem yarnOrderItem = new YarnOrderItem();

        yarnOrderItem.setHsn("54023300");
        yarnOrderItem.setLotNo("K27092");
        yarnOrderItem.setBoxes(16);
        yarnOrderItem.setQuantity(549.530);
        yarnOrderItem.setRate(113.00);
        yarnOrderItem.setYarn(yarn);
        yarnOrderItem.setAmount(BigDecimal.valueOf(62096.89));
//        yarnOrderItem.setYarnOrderItemProducts(itemProducts);
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

    CompanyYarnOrderProduct getYarnOrderItemProduct(YarnOrderItem yarnOrderItem, YarnFabricDesign yarnFabricDesign, Machine machine) {
        CompanyYarnOrderProduct companyYarnOrderProduct = new CompanyYarnOrderProduct();

//        companyYarnOrderProduct.setYarnOrderItem(yarnOrderItem);
        companyYarnOrderProduct.setYarnFabricDesign(yarnFabricDesign);
        companyYarnOrderProduct.setMachine(machine);
//        companyYarnOrderProduct.setQuantity(1350.00);
        companyYarnOrderProduct.setRemarks("design mostly run on machine no 6, with lycra");
        return companyYarnOrderProduct;
    }

    YarnFabricDesign getYarnFabricDesign(FabricDesign fabricDesign, List<FabricDesignYarnMapping> designYarnMappings) {
        YarnFabricDesign yarnFabricDesign = new YarnFabricDesign();

        yarnFabricDesign.setFabricDesignYarnMappings(null);
        yarnFabricDesign.setFabricDesign(fabricDesign);
        yarnFabricDesign.setGsm("195/200");
        return yarnFabricDesign;
    }

    Yarn getYarn(Long id) {
        Yarn yarn = new Yarn();
        yarn.setId(id);
        return yarn;
    }
}
