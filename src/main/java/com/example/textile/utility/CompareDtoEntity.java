package com.example.textile.utility;

import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.YarnOrderItem;

import java.util.Objects;

public class CompareDtoEntity {

    public static boolean isEqualCompanyYarnOrder(CompanyYarnOrderDto companyYarnOrderDto, CompanyYarnOrder companyYarnOrder) {
        //TODO: compare
        return true;
    }

    public static boolean isEqualYarnOrderItem(YarnOrderItem thisOne, YarnOrderItem that) {
        return Objects.nonNull(that) && thisOne.equals(that) &&
                Objects.nonNull(that.getYarn()) && Objects.equals(thisOne.getYarn().getId(), that.getYarn().getId())
                ;
    }
}
