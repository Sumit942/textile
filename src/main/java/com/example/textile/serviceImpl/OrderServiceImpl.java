package com.example.textile.serviceImpl;

import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.Orders;
import com.example.textile.entity.OrdersView;
import com.example.textile.entity.YarnBuilty;
import com.example.textile.repo.OrdersRepository;
import com.example.textile.repo.OrdersViewRepo;
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
import static com.example.textile.utility.DtoAndEntityComparator.isEqualCompanyYarnOrder;
import static com.example.textile.utility.DtoAndEntityComparator.isEqualYarnBuilty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrdersService {
    private OrdersRepository ordersRepo;
    private ModelMapper modelMapper;
    private OrdersViewRepo ordersViewRepo;

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
                Orders saved = ordersRepo.save(persisted);
                //removing dissociated yarnOrder to prevent rendering it to FrontEnd
                saved.getCompanyYarnOrders().removeAll(
                        saved.getCompanyYarnOrders().stream()
                                .filter(yarnOrder -> Objects.isNull(yarnOrder.getOrder()))
                                .collect(Collectors.toList())
                );
                return saved;
            }
        }

        return ordersRepo.save(orders);
    }

    private void updatePersistedOrders(Orders orders, Orders persisted) {
        String logPrefix = "updatedPersistedOrderObject() orderId="+orders.getId();
        log.info("{} Entry", logPrefix);
        if (persisted.getVersion().compareTo(orders.getVersion()) != 0) {
            throw new OptimisticLockException("Order has been already updated orderId="+ orders.getId() + " [ Version received="+orders.getVersion()+", persisted="+ persisted.getVersion() +"]");
        }
        persisted.setRemarks(orders.getRemarks());
        persisted.setOrderStatusType(orders.getOrderStatusType());
        persisted.setCompany(orders.getCompany());
        if (!isEmpty(orders.getCompanyYarnOrders())) {
            Map<Long, CompanyYarnOrder> companyYarnOrderMap = orders.getCompanyYarnOrders().stream()
                    .filter(yarnOrder -> Objects.nonNull(yarnOrder.getId()) && yarnOrder.getId().compareTo(0L) > 0)
                    .collect(Collectors.toMap(CompanyYarnOrder::getId, Function.identity()));

            //Add new companyYarnOrders where orderId=null
            orders.getCompanyYarnOrders().stream()
                    .filter(yarnOrder -> Objects.isNull(yarnOrder.getId()) || yarnOrder.getId().compareTo(0L) <= 0)
                    .forEach(persisted::addCompanyYarnOrders);

            for (CompanyYarnOrder companyYarnOrder : persisted.getCompanyYarnOrders()) {
                var companyYarnOrderUpdate = companyYarnOrderMap.get(companyYarnOrder.getId());
                if (Objects.isNull(companyYarnOrderUpdate)) {
                    companyYarnOrder.setOrder(null);
                } else {
                    if (!isEqualCompanyYarnOrder(companyYarnOrder, companyYarnOrderUpdate)) {
                        log.info("{} updating companyYarnOrderId={}",logPrefix ,companyYarnOrder.getId());
                        if (companyYarnOrder.getVersion().compareTo(companyYarnOrderUpdate.getVersion()) != 0) {
                            throw new OptimisticLockException("CompanyYarnOrder has been already updated id="+ companyYarnOrder.getId() + " [Version received="+companyYarnOrderUpdate.getVersion()+", persisted="+ companyYarnOrder.getVersion()+"]");
                        }
                        companyYarnOrder.setOrderDt(companyYarnOrderUpdate.getOrderDt());
                        companyYarnOrder.setYarnInvoiceNo(companyYarnOrderUpdate.getYarnInvoiceNo());
                        companyYarnOrder.setTotalQuantity(companyYarnOrderUpdate.getTotalQuantity());
                        companyYarnOrder.setTotalAmount(companyYarnOrderUpdate.getTotalAmount());
                        companyYarnOrder.setRemark(companyYarnOrderUpdate.getRemark());
                    }
                    updateYarnBuilties(companyYarnOrder, companyYarnOrderUpdate);
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

    private static void updateYarnBuilties(CompanyYarnOrder companyYarnOrder, CompanyYarnOrder companyYarnOrderUpdate) {
        //checking yarnBuilties
        if (!isEmpty(companyYarnOrderUpdate.getYarnOrderItems())) {
            Map<Long, YarnBuilty> yarnBuiltyMap = companyYarnOrderUpdate.getYarnBuilties().stream()
                    .filter(yarnBuilty -> Objects.nonNull(yarnBuilty.getId()) && yarnBuilty.getId().compareTo(0L) > 0)
                    .collect(Collectors.toMap(YarnBuilty::getId, Function.identity()));

            for (YarnBuilty yarnBuilty : companyYarnOrder.getYarnBuilties()) {
                YarnBuilty yarnBuiltyRecd = yarnBuiltyMap.get(yarnBuilty.getId());
                if (Objects.isNull(yarnBuiltyRecd)) {
                    yarnBuilty.setCompanyYarnOrder(null);
                } else {
                    if (yarnBuilty.getVersion().compareTo(yarnBuiltyRecd.getVersion()) != 0) {
                        throw new OptimisticLockException("YarnBuilty has been already updated id="+ yarnBuilty.getId() + " [Version received="+yarnBuiltyRecd.getVersion()+", persisted="+ yarnBuilty.getVersion()+"]");
                    }
                    if (!isEqualYarnBuilty(yarnBuilty, yarnBuiltyRecd)) {
                        yarnBuilty.setBoxes(yarnBuiltyRecd.getBoxes());
                        yarnBuilty.setQuantity(yarnBuiltyRecd.getQuantity());
                        yarnBuilty.setReceivedDt(yarnBuiltyRecd.getReceivedDt());
                        yarnBuilty.setVehicleNo(yarnBuiltyRecd.getVehicleNo());
                        yarnBuilty.setLoadUnloadCharges(yarnBuiltyRecd.getLoadUnloadCharges());
                        yarnBuilty.setTranportCompany(yarnBuiltyRecd.getTranportCompany());
                    }
                    yarnBuiltyMap.remove(yarnBuilty.getId());
                }
            }
            //Adding the extra added yarnBuilties
            yarnBuiltyMap.values().forEach(companyYarnOrder::addYarnBuilty);
        } else {
            companyYarnOrder.setYarnBuilties(null);
        }
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
            return transformOrdersEntity(modelMapper, order);
        }
        return null;
    }

    @Override
    public Boolean existById(Long id) {
        return ordersRepo.existsById(id);
    }

    //TODO: only show client specific order
    @Override
    public List<OrdersView> fetchView() {

        return ordersViewRepo.findAll();
    }
}
