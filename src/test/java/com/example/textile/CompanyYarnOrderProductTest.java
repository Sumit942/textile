package com.example.textile;

import com.example.textile.entity.CompanyYarnOrderProduct;
import com.example.textile.service.CompanyYarnOrderProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CompanyYarnOrderProductTest {

    @Autowired
    CompanyYarnOrderProductService productService;

    @Test
    public void findIdAndMachineByYarnFabricProductId_test() {
        List<CompanyYarnOrderProduct> idAndMachineByYarnFabricProductId =
                productService.findIdAndMachineByYarnFabricProductId(3L);
        System.out.println(idAndMachineByYarnFabricProductId.size());
    }
}
