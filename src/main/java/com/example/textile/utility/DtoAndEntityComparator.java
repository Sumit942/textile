package com.example.textile.utility;

import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.YarnBuilty;

import java.math.BigDecimal;
import java.util.Objects;

import static com.example.textile.utility.AppUtility.YYYY_MM_DD;

public class DtoAndEntityComparator {

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

    public static boolean isEqualYarnBuilty(YarnBuilty thisOne, YarnBuilty that) {
        if (thisOne == that) {
            return true;
        }

        String thisOneRecdDt = YYYY_MM_DD.format(thisOne.getReceivedDt());
        String thatRecdDo = YYYY_MM_DD.format(that.getReceivedDt());
        boolean transportBoolean = thisOne.getTranportCompany() == that.getTranportCompany();
        if (thisOne.getTranportCompany() != that.getTranportCompany()) {
            Long thisOneTransId = Objects.nonNull(thisOne.getTranportCompany()) ? thisOne.getTranportCompany().getId() : 0L;
            Long thatTransId = Objects.nonNull(that.getTranportCompany()) ? that.getTranportCompany().getId() : 0L;
            transportBoolean = thisOneTransId.compareTo(thatTransId) == 0;
        }

        return  transportBoolean
                && thisOneRecdDt.equals(thatRecdDo)
                && thisOne.getBoxes().compareTo(that.getBoxes()) == 0
                && thisOne.getQuantity().compareTo(that.getQuantity()) == 0
                && thisOne.getVehicleNo().equals(that.getVehicleNo())
                && thisOne.getLoadUnloadCharges().compareTo(that.getLoadUnloadCharges()) == 0;
    }
}
