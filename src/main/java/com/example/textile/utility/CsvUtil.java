package com.example.textile.utility;

import com.example.textile.dto.BankTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvUtil {

    public static List<BankTransactionDto> parseCsv(MultipartFile file) throws IOException {
        List<BankTransactionDto> transactionDtoList = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser csvParser = new CSVParser(reader,
                    CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());

            for (CSVRecord csvRecord: csvParser) {
                log.info("csvRecord: " + csvRecord);
                LocalDate txnDt = LocalDate.parse(csvRecord.get(0), formatter);
                String description = csvRecord.get(2);
                BigDecimal creditAmt = BigDecimal.ZERO;
                if (StringUtils.isNotBlank(csvRecord.get(4))) {
//                    log.info("csvRecord: " + csvRecord.get(4));
                    creditAmt = new BigDecimal(csvRecord.get(4).replaceAll(",",""));
                }
                BigDecimal debitAmt = BigDecimal.ZERO;
                if (StringUtils.isNotBlank(csvRecord.get(5))) {
                    debitAmt = new BigDecimal(csvRecord.get(5).replaceAll(",", ""));
                }
                transactionDtoList.add(new BankTransactionDto(txnDt, description, creditAmt, debitAmt));
            }
        }
        return transactionDtoList;
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
}
