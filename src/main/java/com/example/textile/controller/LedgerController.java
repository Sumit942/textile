package com.example.textile.controller;

import com.example.textile.dto.LedgerEntry;
import com.example.textile.dto.LedgerResponseDto;
import com.example.textile.service.LedgerService;
import com.example.textile.utility.ExcelUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@Controller
@RequestMapping("api/ledger")
public class LedgerController {

    @Autowired
    LedgerService ledgerService;

    @GetMapping
    public ModelAndView showLedgerPage() {
        return new ModelAndView("/ledger");
    }

    @PostMapping("/upload")
    public ModelAndView uploadBankStatement(@RequestParam("file") MultipartFile file,
                                            @RequestParam LocalDate startDt,
                                            @RequestParam LocalDate endDt,
                                            HttpServletResponse response) {
        LedgerResponseDto ledgerResponseDto = ledgerService.processBankStatement(file, startDt, endDt);
        List<LedgerEntry> ledgerEntries = ledgerResponseDto.getLedgerEntries();
        String fileName = "ledger-report";

        ledgerReportDownloadExcel(ledgerEntries, response, fileName);
        return null;
    }

    private void ledgerReportDownloadExcel(List<LedgerEntry> ledgerEntries, HttpServletResponse response, String fileName) {
        String logPrefix = "ledgerReportDownloadExcel()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = createNameValue("excelUtility", "LedgerEntry");
        if (ledgerEntries != null) {
            XSSFWorkbook workBook = getWorkbook(ledgerEntries);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            try {
                workBook.write(bos);
                byte[] content = bos.toByteArray();
                log.info("Excel file created..");

                response.setContentType("application/vnd.ms-excel");
                response.setContentLength(content.length);
                response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xlsx");

                OutputStream os = response.getOutputStream();
                os.write(content, 0, content.length);
                os.flush();
                os.close();
                log.info(logPrefix + "Excel file write to response completed!!");
            } catch (IOException e) {
                log.error(logPrefix + "Exception while ledgerReportDownloadExcel() :" + e.getLocalizedMessage(), e);
            }

        }

        log.info(createExitLog(logPrefix, logSuffix));
    }

    private XSSFWorkbook getWorkbook(List<LedgerEntry> ledgerEntries) {
        Map<String, String> headerMap = new LinkedHashMap<>();
        headerMap.put("S.No", "srNo");
        headerMap.put("Entry Date", "date");
        headerMap.put("Description", "description");
//            headerMap.put("Party Gst", "billToPartyGst");
        headerMap.put("Amount", "amount");

        ExcelUtility<LedgerEntry> excelUtility = new ExcelUtility<>(ledgerEntries);
        return excelUtility.getWorkBook(headerMap);
    }

}
