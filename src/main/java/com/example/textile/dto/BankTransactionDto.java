package com.example.textile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class BankTransactionDto {
//    Transaction Date,Value Date,Description,Reference,Credit Amount,Debit Amount,Available Balance

    public BankTransactionDto(LocalDate txnDate, String description, BigDecimal creditAmt, BigDecimal debitAmt) {
        this.txnDate = txnDate;
        this.description = description;
        this.creditAmt = creditAmt;
        this.debitAmt = debitAmt;
    }

    private LocalDate txnDate;
    private LocalDate valueDate;
    private String description;
    private String reference;
    private BigDecimal creditAmt;
    private BigDecimal debitAmt;
    private BigDecimal avlBalance;
}
