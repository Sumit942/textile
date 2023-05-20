package com.example.textile.enums;

public enum PaymentModeEnum {
    NEFT("National Electronic Funds Transfer"),
    NEFT_IB("Internet Banking - NEFT"),
    CASH("CASH"),
    CHEQUE("CHEQUE"),
    IB("Internet Banking"),
    IMPS("Immediate Payment Service"),
    RTGS("Real Time Gross Settlement"),
    UPI_GPAY("Unified Payments Interface - Gpay"),
    UPI_PHONEPE("Unified Payments Interface - PhonePe");

    private final String paymentMode;

    PaymentModeEnum(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }
}
