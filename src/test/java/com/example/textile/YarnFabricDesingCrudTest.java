package com.example.textile;

import com.example.textile.entity.FabricDesignYarnMapping;
import com.example.textile.entity.YarnFabricDesign;
import com.example.textile.service.YarnFabricDesignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class YarnFabricDesingCrudTest {

    @Autowired
    YarnFabricDesignService yarnFabricDesignService;

    @Test
    public void fetchYarnFabricDesign() {
        String input = "24 Cot";

        List<YarnFabricDesign> byYarnsAndFabricDesign = yarnFabricDesignService.findByYarnTypesAndFabricDesign(input, true);

        System.out.println("byYarnsAndFabricDesign.size() = " + byYarnsAndFabricDesign.size());
        byYarnsAndFabricDesign.forEach(yarnFabricDesign -> {
            System.out.println("yarnFabricDesign = " + yarnFabricDesign);
            System.out.println("yarnFabricDesign.quality = " + yarnFabricDesign.getQualityName());
        });
    }

    @Test
    public void fetchFabricDesignYarnMappingById() {
        List<FabricDesignYarnMapping> fabricDesignYarnMappings = yarnFabricDesignService.fetchFabricDesignYarnMappingById(1L);
        for (FabricDesignYarnMapping fabricDesignYarnMapping : fabricDesignYarnMappings) {
            System.out.println(fabricDesignYarnMapping);
        }
    }
}
