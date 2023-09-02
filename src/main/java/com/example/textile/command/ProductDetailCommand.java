package com.example.textile.command;

import com.example.textile.entity.Company;
import com.example.textile.entity.Product;
import com.example.textile.entity.ProductDetail;

import java.util.List;

public class ProductDetailCommand {

    private List<Long> challanNos;
    private Company company;
    private Product product;

    private List<ProductDetail> productDetails;

    public List<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public List<Long> getChallanNos() {
        return challanNos;
    }

    public void setChallanNos(List<Long> challanNos) {
        this.challanNos = challanNos;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
