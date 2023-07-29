package com.example.textile.utility;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtility<T> {
    private List<T> domainList;

    private static final SimpleDateFormat sdf = new SimpleDateFormat(ShreeramTextileConstants.DATE_FORMAT_ddMMYYYY);

    public ExcelUtility(List<T> domainList) {
        this.domainList = domainList;
    }

    @SneakyThrows
    public XSSFWorkbook getWorkBook(Map<String, String> domainMap) {
        List<Map<String , Object>> valueMapList = new ArrayList<>();
        Map<String, Object> valueMap = null;

        int srNo = 0;
        for (T domain: domainList) {
            srNo++;

            String key, value;
            Object oValue;
            Field field;
            valueMap = new HashMap<>(domainList.size());
            for (Map.Entry<String, String> entrySet: domainMap.entrySet()) {
                key = entrySet.getKey();
                value = entrySet.getValue();

                field = domain.getClass().getDeclaredField(value);
                field.setAccessible(true);

                oValue = field.get(domain);
                valueMap.put(key, oValue);

            }
            valueMapList.add(valueMap);

        }

        return prepareXlsxFile(domainMap.keySet(), valueMapList);
    }

    private XSSFWorkbook prepareXlsxFile(Set<String> headerSet, List<Map<String, Object>> valueMapList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = null;
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        sheet = workbook.createSheet();

        /** Creating Excel Header **/
        int headerCellNo = 0;
        row = sheet.createRow(rowNum++);
        for (String headerRow: headerSet) {
            cell = row.createCell(headerCellNo++);
            cell.setCellValue(headerRow);
        }

        /** Creating Excel Body **/
        for (Map<String, Object> dataRow: valueMapList) {
            int cellNo = 0;
            row = sheet.createRow(rowNum++);
            for (String header: headerSet) {
                cell = row.createCell(cellNo++);
                setCellValue(cell, dataRow, header);
            }
        }

        return workbook;
    }
    private void setCellValue(Cell cell, Map<String, Object> dataRow, String header) {
        Object value = dataRow.get(header);

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue(sdf.format(value));
        } else {
            cell.setCellValue((String) value);
        }
    }
}
