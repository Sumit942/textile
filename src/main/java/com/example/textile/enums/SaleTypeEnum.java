package com.example.textile.enums;

public enum SaleTypeEnum {
    FJW("Fabric Job Work"),FFS("Finish Fabric Sale"),GFS("Grey Fabric Sale");

    private final String saleType;

    SaleTypeEnum(String saleType) {
        this.saleType = saleType;
    }

    public String getSaleType() {
        return saleType;
    }
}
