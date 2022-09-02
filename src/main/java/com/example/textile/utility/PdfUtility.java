package com.example.textile.utility;

import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;

public class PdfUtility {

    public static void createPdf(OutputStream outputStream,String pdfTemplateProcess) throws DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(pdfTemplateProcess);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();
    }
}
