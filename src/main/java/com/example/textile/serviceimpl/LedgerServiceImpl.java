package com.example.textile.serviceimpl;

import com.example.textile.dto.BankTransactionDto;
import com.example.textile.dto.LedgerResponseDto;
import com.example.textile.entity.InvoiceView;
import com.example.textile.service.InvoiceViewService;
import com.example.textile.service.LedgerService;
import com.example.textile.utility.CsvUtil;
import com.example.textile.utility.LedgerCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.*;
import java.util.Date;
import java.util.List;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@Service
public class LedgerServiceImpl implements LedgerService {

    @Autowired
    InvoiceViewService invoiceViewService;

    @Override
    public LedgerResponseDto processBankStatement(MultipartFile file, LocalDate startDt, LocalDate endDt) {
        String logPrefix = "processBankStatement()";
        List<BankTransactionDto> transaction;
        List<InvoiceView> invoiceViews;

        log.info(createEntryLog(logPrefix));
        String logSuffix = createNameValue("startDt - endDt", String.format("%s - %s", startDt, endDt));
        try {
            transaction = CsvUtil.parseCsv(file);

            LocalDateTime startDateTime = startDt.atStartOfDay();
            Instant startDate = startDateTime.atZone(ZoneId.systemDefault()).toInstant();

            LocalDateTime endDateTime = endDt.atStartOfDay();
            Instant endDate = endDateTime.atZone(ZoneId.systemDefault()).toInstant();

            invoiceViews = invoiceViewService.getInvoiceReport(
                                                Date.from(startDate),
                                                Date.from(endDate),
                                                null, null, null);

        } catch (IOException e) {
            log.error(String.format("%s %s - Exception: %s",logSuffix, logSuffix, e.getLocalizedMessage()), e);
            throw new RuntimeException(e);
        }
        log.info(createExitLog(logPrefix, logSuffix));
        return LedgerCalculator.generateLedger(transaction, invoiceViews, startDt, endDt);
    }
}
