package com.example.textile.serviceimpl;

import com.example.textile.entity.CompanyYarnOrderProduct;
import com.example.textile.entity.Machine;
import com.example.textile.repo.CompanyYarnOrderProductRepo;
import com.example.textile.service.CompanyYarnOrderProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class CompanyYarnOrderProductServiceImpl implements CompanyYarnOrderProductService {

    private CompanyYarnOrderProductRepo orderProductRepo;

    @Override
    public List<CompanyYarnOrderProduct> findIdAndMachineByYarnFabricProductId(Long yarnFabricProductId) {
        List<Object[]> idAndMachineByYarnFabricProductId = orderProductRepo.findIdAndMachineByYarnFabricProductId(yarnFabricProductId);
        List<CompanyYarnOrderProduct> productList = new ArrayList<>();
        for (Object[] objects : idAndMachineByYarnFabricProductId) {
            CompanyYarnOrderProduct product = new CompanyYarnOrderProduct();
            product.setId((Long) objects[0]);
            product.setMachine((Machine) objects[1]);
            productList.add(product);
        }
        return productList;
    }

    @Override
    public List<CompanyYarnOrderProduct> findByYarnFabricDesignIdAndMachineId(Long yarnFabricDesignId, Long mcnId) {
        return orderProductRepo.findByYarnFabricDesignIdAndMachineId(yarnFabricDesignId, mcnId);
    }

    @Override
    public CompanyYarnOrderProduct save(CompanyYarnOrderProduct companyYarnOrderProduct) {
        return orderProductRepo.save(companyYarnOrderProduct);
    }
}
