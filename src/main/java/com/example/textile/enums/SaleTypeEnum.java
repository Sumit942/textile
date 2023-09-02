package com.example.textile.enums;

public enum SaleTypeEnum {
    FJW("Fabric Job Work","SRTI/FJW/"),FFS("Finish Fabric Sale","SRTI/FFS/"),GFS("Grey Fabric Sale","SRTI/GFS/");

    private final String saleType;
    private final String challanFormat;

    SaleTypeEnum(String saleType,String challanFormat) {
        this.saleType = saleType;
        this.challanFormat = challanFormat;
    }

    public String getSaleType() {
        return saleType;
    }
    public String getChallanFormat() {
        return challanFormat;
    }
}
