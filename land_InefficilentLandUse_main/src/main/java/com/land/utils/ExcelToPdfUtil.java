package com.land.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.spire.pdf.*;
import com.spire.pdf.graphics.*;
import com.spire.xls.FileFormat;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import java.io.FileOutputStream;

public class ExcelToPdfUtil {
    /**
     * Excel文件转Pdf.
     * @param excelPath Excel文件路径.
     * @param pdfPath Pdf文件路径.
     * @throws Exception .
     */
    public static void excelToPdf(String excelPath, String pdfPath,String no_watermarkpdfPath) throws Exception {
        // 加载Excel文档.
        Workbook wb = new Workbook();
        wb.loadFromFile(excelPath);
        // 调用方法保存为PDF格式.
        wb.saveToFile(pdfPath, FileFormat.PDF);
        // 去除PDF中的水印
        try {
            PdfReader reader = new PdfReader(pdfPath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(no_watermarkpdfPath));
            int totalPages = reader. getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                PdfContentByte content = stamper.getOverContent(i);
                content.beginText();
                content.setColorFill(BaseColor.WHITE);
                content.setFontAndSize(BaseFont.createFont(),20);
                content.showTextAligned(Element.ALIGN_CENTER, "Watermark", 300, 400, 45);
                content.endText();
                stamper.close();
                reader.close();
            }
        } catch (Exception e) {
            e. printStackTrace();
        }
    }

    /**
     * Excel文件转Pdf.
     * @param excelPath Excel文件路径.
     * @param pdfPath Pdf文件路径.
     * @param sheetIndex sheet页序号.
     * @throws Exception .
     */
    public static void excelToPdf(String excelPath, String pdfPath, int sheetIndex) throws Exception {
        // 加载Excel文档.
        Workbook wb = new Workbook();
        wb.loadFromFile(excelPath);

        Worksheet sheet = wb.getWorksheets().get(sheetIndex);
        // 调用方法保存为PDF格式.
        wb.saveToFile(pdfPath, FileFormat.PDF);
    }

}
