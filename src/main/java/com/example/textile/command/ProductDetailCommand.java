package com.example.textile.command;

import com.example.textile.entity.ProductDetail;

import java.util.List;

public class ProductDetailCommand {

    private List<ProductDetail> productDetails;

    public List<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }
}
