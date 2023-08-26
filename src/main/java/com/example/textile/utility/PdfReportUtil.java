package com.example.textile.utility;

import com.lowagie.text.pdf.PdfDocument;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.util.List;
import java.util.Map;

public class PdfReportUtil<T> {
    private final List<T> domainList;

    public PdfReportUtil(List<T> domainList) {
        this.domainList = domainList;
    }

    @SneakyThrows
    public PDDocument getPdf(Map<String, String> domainMap) {
        //TODO: pdf report download

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Set font and font size
        PDFont pdFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        contentStream.setFont(pdFont,12);

        // Define table parameters
        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;
        int rows = 5;
        int cols = 3;
        float rowHeight = 20;
        float tableHeight = rowHeight * rows;
        float colWidth = tableWidth / (float) cols;
        float tableXPosition = margin;

        // Create table header
        contentStream.beginText();
        contentStream.newLineAtOffset(tableXPosition, yPosition);
        contentStream.showText("Header 1");
        contentStream.newLineAtOffset(colWidth, 0);
        contentStream.showText("Header 2");
        contentStream.newLineAtOffset(colWidth, 0);
        contentStream.showText("Header 3");
        contentStream.endText();

        yPosition -= rowHeight;

        // Create table rows
        for (int i = 0; i < rows; i++) {
            contentStream.setLineWidth(0.5f);
            contentStream.moveTo(tableXPosition, yPosition);
            contentStream.lineTo(tableXPosition + tableWidth, yPosition);
            contentStream.stroke();

            contentStream.beginText();
            PDType1Font pdType1Font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            contentStream.setFont(pdType1Font, 12);
            contentStream.newLineAtOffset(tableXPosition, yPosition - 15);
            contentStream.showText("Cell " + (i + 1) + ", 1");
            contentStream.newLineAtOffset(colWidth, 0);
            contentStream.showText("Cell " + (i + 1) + ", 2");
            contentStream.newLineAtOffset(colWidth, 0);
            contentStream.showText("Cell " + (i + 1) + ", 3");
            contentStream.endText();

            yPosition -= rowHeight;
        }

        // Close the content stream
        contentStream.close();

        return  document;
    }
}
