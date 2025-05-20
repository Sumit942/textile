package com.example.textile.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LedgerEntry {
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private BigDecimal runningBalance;

    public LedgerEntry(LocalDate date, String description, BigDecimal amount) {
        this.date = date;
        this.description = description;
        this.amount = amount;
    }
}
