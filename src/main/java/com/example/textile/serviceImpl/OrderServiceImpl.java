package com.example.textile.serviceImpl;

import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.Orders;
import com.example.textile.repo.OrdersRepository;
import com.example.textile.service.OrdersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.textile.transform.TransformationDTOToEntity.transformOrdersDto;
import static com.example.textile.transform.TransformationEntityToDTO.transformOrdersEntity;
import static com.example.textile.utility.CompareDtoAndEntityObjects.isEqualCompanyYarnOrder;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

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

    @Transactional
    @Override
    public Orders save(OrdersDto ordersDto) {
        String logPrefix = " save() orderId=" + ordersDto.getId();
        log.info("Entry{}", logPrefix);
        log.info("{} [companyYarnOrderSize={}]",logPrefix, Objects.nonNull(ordersDto.getCompanyYarnOrders()) ? ordersDto.getCompanyYarnOrders().size() : "null");
        Orders orders = transformOrdersDto(modelMapper, ordersDto);
        if (Objects.nonNull(orders.getId()) && orders.getId().compareTo(0L) > 0) {
            Optional<Orders> optionalOrders = ordersRepo.findById(orders.getId());
            if (optionalOrders.isPresent()) {
                Orders persisted = optionalOrders.get();
                updatePersistedOrders(orders, persisted);
                return ordersRepo.save(persisted);
            }
        }

        return ordersRepo.save(orders);
    }

    private void updatePersistedOrders(Orders orders, Orders persisted) {
        String logPrefix = "updatedPersistedOrderObject() orderId="+orders.getId();
        log.info("{} Entry", logPrefix);
        if (persisted.getVersion().compareTo(orders.getVersion()) != 0) {
            throw new OptimisticLockException("Order has been already updated id="+ orders.getId() + " | Version received="+orders.getVersion()+", persisted="+ persisted.getVersion());
        }
        persisted.setRemarks(orders.getRemarks());
        if (!isEmpty(orders.getCompanyYarnOrders())) {
            Map<Long, CompanyYarnOrder> companyYarnOrderMap = orders.getCompanyYarnOrders().stream()
                    .collect(Collectors.toMap(CompanyYarnOrder::getId, Function.identity()));

            for (CompanyYarnOrder companyYarnOrder : persisted.getCompanyYarnOrders()) {
                var companyYarnOrderUpdate = companyYarnOrderMap.get(companyYarnOrder.getId());
                if (Objects.isNull(companyYarnOrderUpdate)) {
                    companyYarnOrder.setId(null);
                } else {
                    if (!isEqualCompanyYarnOrder(companyYarnOrder, companyYarnOrderUpdate)) {
                        log.info("{} updating companyYarnOrderId={}",logPrefix ,companyYarnOrder.getId());
                        if (companyYarnOrder.getVersion().compareTo(companyYarnOrderUpdate.getVersion()) != 0) {
                            throw new OptimisticLockException("CompanyYarnOrder has been already updated id="+ companyYarnOrder.getId() + " | Version received="+companyYarnOrderUpdate.getVersion()+", persisted="+ companyYarnOrder.getVersion());
                        }
                        companyYarnOrder.setOrderDt(companyYarnOrderUpdate.getOrderDt());
                        companyYarnOrder.setYarnInvoiceNo(companyYarnOrderUpdate.getYarnInvoiceNo());
                        companyYarnOrder.setTotalQuantity(companyYarnOrderUpdate.getTotalQuantity());
                        companyYarnOrder.setTotalAmount(companyYarnOrderUpdate.getTotalAmount());
                        companyYarnOrder.setRemark(companyYarnOrderUpdate.getRemark());
                    }
                    //removing the updated companyYarnOrder from Map
                    companyYarnOrderMap.remove(companyYarnOrder.getId());
                }
            }

            //Adding the remaining companyYarnOrder to orders
            companyYarnOrderMap.values().forEach(persisted::addCompanyYarnOrders);
        } else {
            persisted.setCompanyYarnOrders(null);
        }
        log.info("updatedPersistedOrderObject() Exit");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
