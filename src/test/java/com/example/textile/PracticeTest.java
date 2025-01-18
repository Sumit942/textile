package com.example.textile;

import com.example.textile.entity.Product;
import com.example.textile.entity.YarnOrderItemProduct;
import com.example.textile.repo.YarnOrderItemProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PracticeTest {

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

    @Test
    public void testSaveYarnOrderItemProduct() {
        Product product = new Product();
        product.setName("1x1 PKk");
        product.setActive(true);
        product.setId(279L);


        YarnOrderItemProduct yarnOrderProduct = new YarnOrderItemProduct();
        yarnOrderProduct.setId(4L);
//        yarnOrderProduct.setProduct(product);
//        yarnOrderProduct.setGsm("234");
        yarnOrderProduct.setQuantity(142.32);
        System.out.println("saving: " + yarnOrderProduct);
        YarnOrderItemProduct savedYarnOrderItemProduct = yarnOrderProductRepo.save(yarnOrderProduct);

        System.out.println("saved: " + savedYarnOrderItemProduct);

//        Assertions.assertSame(yarnOrderProduct.getGsm(), savedYarnOrderItemProduct.getGsm());
        Assertions.assertTrue(savedYarnOrderItemProduct.getId() > 0);

    }
}
