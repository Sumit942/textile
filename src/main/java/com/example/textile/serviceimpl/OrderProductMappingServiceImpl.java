package com.example.textile.serviceimpl;

import com.example.textile.dto.OrderProductMappingDto;
import com.example.textile.entity.OrderProductMapping;
import com.example.textile.repo.OrderProductMappingRepo;
import com.example.textile.service.OrderProductMappingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.textile.transform.TransformationDTOToEntity.transformOrderProductMappingDto;
import static com.example.textile.transform.TransformationEntityToDTO.transformOrderProductMappings;

@AllArgsConstructor
@Service
public class OrderProductMappingServiceImpl implements OrderProductMappingService {
    private OrderProductMappingRepo productMappingRepo;

    @Override
    public OrderProductMapping findById(Long id) {
        Optional<OrderProductMapping> byId = productMappingRepo.findById(id);
        return byId.orElse(null);
    }

    @Override
    public List<OrderProductMapping> findByOrdersId(Long orderId) {
        return productMappingRepo.findByOrdersId(orderId);
    }

    @Override
    public List<OrderProductMapping> saveOrderProductMapping(List<OrderProductMapping> orderProductMappings) {
        return productMappingRepo.saveAll(orderProductMappings);
    }

    @Override
    public OrderProductMappingDto saveOrderProductMappings(OrderProductMappingDto orderProductMappingDto) {
        List<OrderProductMapping> productMappings = transformOrderProductMappingDto(orderProductMappingDto);

        List<OrderProductMapping> savedOrderProductMapping = saveOrderProductMapping(productMappings);

        return transformOrderProductMappings(savedOrderProductMapping);
    }
}
