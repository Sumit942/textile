package com.example.textile.serviceImpl;

import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.Orders;
import com.example.textile.repo.OrdersRepository;
import com.example.textile.service.OrdersService;
import com.example.textile.transform.TransformationEntityToDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.example.textile.transform.TransformationDTOToEntity.transformOrdersDto;
import static com.example.textile.transform.TransformationEntityToDTO.transformOrdersEntity;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrdersService {
    private OrdersRepository ordersRepo;
    private ModelMapper modelMapper;

    @Override
    public List<OrdersDto> findAll() {
        List<Orders> orders = ordersRepo.findAll();
        return modelMapper.map(orders, new TypeToken<List<OrdersDto>>(){}.getType());
    }

    @Override
    public Orders save(OrdersDto ordersDto) {
        String logPrefix = " save() orderId=" + ordersDto.getId();
        log.info("Entry{}", logPrefix);
        Orders orders = transformOrdersDto(modelMapper, ordersDto);
        ordersDto.getCompanyYarnOrders().forEach(orderItems -> {
            System.out.println("orderItem id: " + orderItems.getId());
            System.out.println("quantity: " + orderItems.getTotalQuantity());
        });
        log.info("{} [companyYarnOrderSize={}]",logPrefix, Objects.nonNull(ordersDto.getCompanyYarnOrders()) ? ordersDto.getCompanyYarnOrders().size() : "null");

        return ordersRepo.save(orders);
    }

    @Override
    @Transactional
    public OrdersDto findById(Long id) {
        Orders order = ordersRepo.findById(id).orElse(null);
        if (order != null) {
            log.debug("Getting associated objects");
            for (CompanyYarnOrder companyYarnOrder : order.getCompanyYarnOrders()) {
                companyYarnOrder.getYarnBuilties();
            }

            order.getCompany();
            return transformOrdersEntity(modelMapper, order);
        }
        return null;
    }

    @Override
    public Boolean existById(Long id) {
        return ordersRepo.existsById(id);
    }
}
