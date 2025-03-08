package com.example.textile.serviceimpl;

import com.example.textile.entity.CompanyYarnOrderProduct;
import com.example.textile.entity.Machine;
import com.example.textile.repo.CompanyYarnOrderProductRepo;
import com.example.textile.service.CompanyYarnOrderProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CompanyYarnOrderProductServiceImpl implements CompanyYarnOrderProductService {

    private CompanyYarnOrderProductRepo productRepo;

    @Override
    public List<CompanyYarnOrderProduct> findIdAndMachineByYarnFabricProductId(Long yarnFabricProductId) {
        List<Object[]> idAndMachineByYarnFabricProductId = productRepo.findIdAndMachineByYarnFabricProductId(yarnFabricProductId);
        List<CompanyYarnOrderProduct> productList = new ArrayList<>();
        for (Object[] objects : idAndMachineByYarnFabricProductId) {
            CompanyYarnOrderProduct product = new CompanyYarnOrderProduct();
            product.setId((Long) objects[0]);
            product.setMachine((Machine) objects[1]);
            productList.add(product);
        }
        return productList;
    }
}
