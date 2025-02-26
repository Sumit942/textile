package com.example.textile;

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
        String input = "24Cot";

        List<YarnFabricDesign> byYarnsAndFabricDesign = yarnFabricDesignService.findByYarnTypesAndFabricDesign(input);

        System.out.println(byYarnsAndFabricDesign);
    }
}
