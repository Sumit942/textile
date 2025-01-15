package com.example.textile;

import com.example.textile.entity.Product;
import com.example.textile.entity.YarnOrderProduct;
import com.example.textile.repo.YarnOrderProductRepository;
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
    YarnOrderProductRepository yarnOrderProductRepo;

    @Test
    public void testSaveYarnOrderProduct() {
        Product product = new Product();
        product.setName("1x1 PKk");
        product.setActive(true);
        product.setId(279L);


        YarnOrderProduct yarnOrderProduct = new YarnOrderProduct();
        yarnOrderProduct.setId(4L);
//        yarnOrderProduct.setProduct(product);
        yarnOrderProduct.setGsm("234");
        yarnOrderProduct.setQuantity(142.32);
        System.out.println("saving: " + yarnOrderProduct);
        YarnOrderProduct savedYarnOrderProduct = yarnOrderProductRepo.save(yarnOrderProduct);

        System.out.println("saved: " + savedYarnOrderProduct);

        Assertions.assertSame(yarnOrderProduct.getGsm(), savedYarnOrderProduct.getGsm());
        Assertions.assertTrue(savedYarnOrderProduct.getId() > 0);

    }
}
