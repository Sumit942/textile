package com.example.textile.utility;

import com.example.textile.entity.InvoiceView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static com.example.textile.utility.ShreeramTextile.*;

@Slf4j
public class ExcelUtility<T> {
    private final List<T> domainList;

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

        if (!domainList.isEmpty() && domainList.get(0) instanceof InvoiceView) {
            addTotalAmountRow(valueMapList, domainList);
        }

        return prepareXlsxFile(domainMap.keySet(), valueMapList);
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
        CellStyle totalAmount = createTotalStyle(workbook);
        DataFormat dataFormat = workbook.createDataFormat();
        totalAmount.setDataFormat(dataFormat.getFormat("#,##0.00"));
        row = sheet.getRow(sheet.getLastRowNum()-1);
        int totalFooterCellNo = 0;
        for (String footer: headerSet) {
            cell = row.getCell(totalFooterCellNo++);
            if (!footer.equals("Party Name")) {
                cell.setCellStyle(totalAmount);
            } else {
                cell.setCellStyle(totalStyle);
            }

        }

        /* added pending row style */
        row = sheet.getRow(sheet.getLastRowNum());
        int pendingFooterCellNo = 0;
        for (String footer: headerSet) {
            cell = row.getCell(pendingFooterCellNo++);
            if (!footer.equals("Debit")) {
                cell.setCellStyle(totalStyle);
            } else {
                cell.setCellStyle(totalAmount);
            }

            //AutoSize Column
            sheet.autoSizeColumn(pendingFooterCellNo);
        }


        return workbook;
    }
    private void setCellValue(XSSFWorkbook workbook, Cell cell, Map<String, Object> dataRow, String header) {
        Object value = dataRow.get(header);

        if (value == null) {
            cell.setCellValue("");
            return;
        }

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue(getStringCellValueForBool((Boolean) value, header));
        } else if (value instanceof Date) {
            cell.setCellValue(getDateFormat(ShreeramTextileConstants.DATE_FORMAT_ddMMYYYY).format(value));
        } else if (value instanceof BigDecimal) {
            CellStyle cellStyle = createDataStyle(workbook);
            DataFormat dataFormat = workbook.createDataFormat();
            cellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));

            cell.setCellStyle(cellStyle);
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue(value.toString());
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
