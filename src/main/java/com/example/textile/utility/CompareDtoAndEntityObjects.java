package com.example.textile.utility;

import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.YarnOrderItem;

import java.math.BigDecimal;
import java.util.Objects;

import static com.example.textile.utility.AppUtility.YYYY_MM_DD;

public class CompareDtoAndEntityObjects {

    public static boolean   isEqualCompanyYarnOrder(CompanyYarnOrder source, CompanyYarnOrder that) {
        if (source == that) {
            return true;
        }
        String thisOrderDt = YYYY_MM_DD.format(source.getOrderDt());
        String thatOrderDt = YYYY_MM_DD.format(that.getOrderDt());
        BigDecimal totalAmount = source.getTotalAmount() == null ? BigDecimal.ZERO : source.getTotalAmount();
        BigDecimal thatTotalAmount = that.getTotalAmount() == null ? BigDecimal.ZERO : that.getTotalAmount();

        return Objects.equals(thisOrderDt, thatOrderDt)
                && Objects.equals(source.getYarnInvoiceNo(), that.getYarnInvoiceNo())
                && Objects.equals(source.getRemark(), that.getRemark())
                && Objects.equals(source.getTotalQuantity(), that.getTotalQuantity())
                && totalAmount.compareTo(thatTotalAmount) == 0;
    }

    public static boolean isEqualYarnOrderItem(YarnOrderItem thisOne, YarnOrderItem that) {
        return Objects.nonNull(that) && thisOne.equals(that) &&
                Objects.nonNull(that.getYarn()) && Objects.equals(thisOne.getYarn().getId(), that.getYarn().getId())
                ;
    }
}
