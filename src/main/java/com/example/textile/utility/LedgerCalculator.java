package com.example.textile.utility;

import com.example.textile.dto.BankTransactionDto;
import com.example.textile.dto.LedgerEntry;
import com.example.textile.dto.LedgerResponseDto;
import com.example.textile.entity.InvoiceView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LedgerCalculator {

    public static LedgerResponseDto generateLedger(List<BankTransactionDto> transactions, List<InvoiceView> sales,
                                                   LocalDate startDt, LocalDate endDt) {

        LedgerResponseDto ledgerResponseDto = new LedgerResponseDto();
        List<LedgerEntry> combinedEntries = new ArrayList<>();

        for (BankTransactionDto txn : transactions) {
            combinedEntries.add(new LedgerEntry(txn.getTxnDate(), txn.getDescription(), txn.getCreditAmt()));
        }

        for (InvoiceView sale : sales) {
            combinedEntries.add(new LedgerEntry(
                    LocalDate.ofInstant(sale.getInvoiceDate().toInstant(), ZoneId.systemDefault()),
                    sale.getInvoiceNo(), sale.getTotalAmountAfterTax()));
        }

        combinedEntries.sort(Comparator.comparing(LedgerEntry::getDate));



        ledgerResponseDto.setLedgerEntries(combinedEntries);

        return ledgerResponseDto;
    }
}
