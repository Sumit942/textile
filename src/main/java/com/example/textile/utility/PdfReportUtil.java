package com.example.textile.utility;

import com.example.textile.entity.InvoiceView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

import static com.example.textile.utility.ShreeramTextile.*;

@Slf4j
public class PdfReportUtil<T> {
    private final List<T> domainList;

    public PdfReportUtil(List<T> domainList) {
        this.domainList = domainList;
    }

    @SneakyThrows
    public PDDocument getPdf(Map<String, String> domainMap) {
        List<Map<String, Object>> valueMapList = new ArrayList<>();
        Map<String, Object> valueMap;

        int srNo = 0;
        for (T domain: domainList) {
            srNo++;

            String key,value;
            Object oValue;
            Field field;
            valueMap = new HashMap<>();
            for (Map.Entry<String, String> entrySet: domainMap.entrySet())   {
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
                    log.error("Field not found getPdf(): "+e);
                }
            }

            valueMapList.add(valueMap);
        }

        if (!domainList.isEmpty() && domainList.get(0) instanceof InvoiceView) {
            addTotalAmountRow(valueMapList, domainList);
        }

        return preparePdfFile(domainMap.keySet(), valueMapList);
//        return getPdf1(domainMap);
    }

    @SneakyThrows
    private PDDocument preparePdfFile(Set<String> headerSet, List<Map<String, Object>> valueMapList) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A3);
        document.addPage(page);


        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            int cols = headerSet.size(); // Number of columns in the table
            int rows = valueMapList.size(); // Number of rows in the table
            float rowHeight = 20;
            float tableHeight = rowHeight * rows;
            float colWidth = tableWidth / (float) cols;
            float tableXStart = margin;

            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDFont fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            // Draw table headers with borders
            for (String header: headerSet) {
                contentStream.beginText();
                contentStream.setFont(fontBold, 12);
                contentStream.newLineAtOffset(tableXStart, yPosition);
                contentStream.showText(header);
                contentStream.endText();

                // Draw cell border
                contentStream.moveTo(tableXStart, yPosition - rowHeight);
                contentStream.lineTo(tableXStart + colWidth, yPosition - rowHeight);
                contentStream.lineTo(tableXStart + colWidth, yPosition);
                contentStream.lineTo(tableXStart, yPosition);
                contentStream.closeAndStroke();

                tableXStart += colWidth;
            }
            yPosition -= rowHeight;

            // Draw table content with borders

            for (Map<String, Object> valueMap : valueMapList) {
                tableXStart = margin;
                for (String header: headerSet) {
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(tableXStart, yPosition);
                    contentStream.showText(getText(valueMap.get(header), header));
                    contentStream.endText();

                    // Draw cell border
                    contentStream.moveTo(tableXStart, yPosition - rowHeight);
                    contentStream.lineTo(tableXStart + colWidth, yPosition - rowHeight);
                    contentStream.lineTo(tableXStart + colWidth, yPosition);
                    contentStream.lineTo(tableXStart, yPosition);
                    contentStream.closeAndStroke();

                    tableXStart += colWidth;
                }
                yPosition -= rowHeight;
            }
        }


        return document;
    }

    private String getText(Object value, String header) {
        log.debug("getText() [value= "+value+", header= "+header+"]");
        if (value == null)
            return "";
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Double) {
            return String.format("%.2f", (Double) value);
        } else if (value instanceof Integer) {
            return String.valueOf(value);
        } else if (value instanceof Boolean) {
            return (getStringCellValueForBool((Boolean) value, header));
        } else if (value instanceof Date) {
            return (getDateFormat(ShreeramTextileConstants.DATE_FORMAT_ddMMYYYY).format(value));
        } else if (value instanceof BigDecimal) {
            return value.toString();
        } else {
            return (value.toString());
        }
    }
}
