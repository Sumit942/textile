package com.example.textile.command;

import com.example.textile.entity.Product;

public class ProductCommand {

    private Product searchProduct;

    private Product product;

    public Product getSearchProduct() {
        return searchProduct;
    }

    public void setSearchProduct(Product searchProduct) {
        this.searchProduct = searchProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
