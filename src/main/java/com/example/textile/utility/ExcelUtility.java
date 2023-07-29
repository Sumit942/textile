package com.example.textile.utility;

import com.example.textile.entity.InvoiceView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class ExcelUtility<T> {
    private final List<T> domainList;

    private static final SimpleDateFormat sdf = new SimpleDateFormat(ShreeramTextileConstants.DATE_FORMAT_ddMMYYYY);

    public ExcelUtility(List<T> domainList) {
        this.domainList = domainList;
    }

    @SneakyThrows
    public XSSFWorkbook getWorkBook(Map<String, String> domainMap) {
        List<Map<String , Object>> valueMapList = new ArrayList<>();
        Map<String, Object> valueMap;

        int srNo = 0;
        for (T domain: domainList) {
            srNo++;

            String key, value;
            Object oValue;
            Field field;
            valueMap = new HashMap<>(domainList.size()+1);
            for (Map.Entry<String, String> entrySet: domainMap.entrySet()) {
                key = entrySet.getKey();
                value = entrySet.getValue();
                try {
                    if (ShreeramTextileConstants.SRNO.equals(value)) {
                        oValue = srNo;
                    } else {
                        field = domain.getClass().getDeclaredField(value);
                        field.setAccessible(true);
                        oValue = field.get(domain);
                    }

                    valueMap.put(key, oValue);
                } catch (NoSuchFieldException e) {
                    log.error("Field not found getWorkbook(): "+e.getLocalizedMessage());
                }
            }
            valueMapList.add(valueMap);

        }

        if (domainList != null && !domainList.isEmpty() && domainList.get(0) instanceof InvoiceView) {
            addTotalAmountRow(valueMapList);
        }

        return prepareXlsxFile(domainMap.keySet(), valueMapList);
    }

    private void addTotalAmountRow(List<Map<String, Object>> valueMapList) {
        log.info("addTotalAmountRow() Entry");
        Map<String, Object> valueMapTotal = new HashMap<>();

        //add an empty row before total amount row
        valueMapList.add(valueMapTotal);

        valueMapTotal = new HashMap<>();

        List<InvoiceView> list = (List<InvoiceView>) domainList;
        BigDecimal amount = list.stream().map(InvoiceView::getTotalAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = list.stream().map(InvoiceView::getTotalTaxAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal pnf = list.stream().map(InvoiceView::getPnfCharge)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = list.stream().map(InvoiceView::getTotalTaxAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paidAmount = list.stream().map(InvoiceView::getPaidAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal debit = list.stream().map(InvoiceView::getAmtDr)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

        valueMapTotal.put("Party Name","Total:"); //assuming to add total in party name column
        valueMapTotal.put("Amount",amount);
        valueMapTotal.put("Total Tax",tax);
        valueMapTotal.put("PnF",pnf);
        valueMapTotal.put("Total Amount",totalAmount);
        valueMapTotal.put("Paid",paidAmount);
        valueMapTotal.put("Debit",debit);
        valueMapList.add(valueMapTotal);
    }

    private XSSFWorkbook prepareXlsxFile(Set<String> headerSet, List<Map<String, Object>> valueMapList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet;
        Row row;
        Cell cell;
        int rowNum = 0;

        sheet = workbook.createSheet();


        /* Creating Excel Header **/
        CellStyle headerStyle = createHeaderStyle(workbook);
        int headerCellNo = 0;
        row = sheet.createRow(rowNum++);
        for (String headerRow: headerSet) {
            cell = row.createCell(headerCellNo++);
            cell.setCellValue(headerRow);
            cell.setCellStyle(headerStyle);
        }

        /* Creating Excel Body **/
        CellStyle dataStyle = createDataStyle(workbook);
        for (Map<String, Object> dataRow: valueMapList) {
            int cellNo = 0;
            row = sheet.createRow(rowNum++);
            for (String header: headerSet) {
                cell = row.createCell(cellNo++);
                cell.setCellStyle(dataStyle);
                setCellValue(workbook, cell, dataRow, header);
            }
        }

        /*  Applying Excel Footer Style   **/
        CellStyle totalStyle = createTotalStyle(workbook);
        row = sheet.getRow(sheet.getLastRowNum());
        int footerCellNo = 0;
        for (String footer: headerSet) {
            cell = row.getCell(footerCellNo++);
            if (!footer.equals("Party Name")) {
                CellStyle totalAmount = createTotalStyle(workbook);
                DataFormat dataFormat = workbook.createDataFormat();
                totalAmount.setDataFormat(dataFormat.getFormat("#,##0.00"));
                cell.setCellStyle(totalAmount);
            } else {
                cell.setCellStyle(totalStyle);
            }

            //AutoSize Column
            sheet.autoSizeColumn(footerCellNo);
        }


        return workbook;
    }
    private void setCellValue(XSSFWorkbook workbook, Cell cell, Map<String, Object> dataRow, String header) {
        Object value = dataRow.get(header);

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue(getStringCellValueForBool((Boolean) value, header));
        } else if (value instanceof Date) {
            cell.setCellValue(sdf.format(value));
        } else if (value instanceof BigDecimal) {
            CellStyle cellStyle = createDataStyle(workbook);
            DataFormat dataFormat = workbook.createDataFormat();
            cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

            cell.setCellStyle(cellStyle);
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            if (value != null)
                cell.setCellValue(value.toString());
        }
    }

    private String getStringCellValueForBool(Boolean value, String header) {
        if (header.contains("Payment")) {
            return value ? "Paid" : "UnPaid";
        } else {
            return value ? "Yes" : "No";
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLUE_GREY.getIndex());
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
