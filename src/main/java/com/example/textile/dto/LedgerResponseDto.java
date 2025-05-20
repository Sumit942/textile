package com.example.textile.dto;

import com.example.textile.entity.InvoiceView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LedgerResponseDto {
    private BigDecimal openingBalance;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private BigDecimal closingBalance;
    private List<BankTransactionDto> unmatchedTransactions;
    private List<InvoiceView> matchedSales;
    private List<LedgerEntry> ledgerEntries;
}
