package com.land.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Pdfcompressor {

        public static void main(String[] args) {
            compressPdf("F:\\desktop\\低效用地\\低效用地.pdf", "F:\\desktop\\低效用地\\低效用地22.pdf");
        }
        public static void compressPdf(String sourcePath, String destinationPath) {
            try {
                PdfReader reader = new PdfReader(sourcePath);
                Document document = new Document();
                PdfCopy сopy = new PdfCopy(document, new FileOutputStream(destinationPath));
                document.open();
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    сopy.addPage(сopy.getImportedPage(reader, i));
                    document.close();
                    reader.close();
                    System.out.println("PDF compression complete!");}
                } catch(IOException | DocumentException e){
                    e.printStackTrace();
                }
        }
}
