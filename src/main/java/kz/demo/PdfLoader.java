package kz.demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

public class PdfLoader {

    public static void main(String[] args) {
        try {
            PDDocument document = PDDocument.load(new File("C:\\Users\\Azamat\\Downloads\\11C48881-FBC9-4380-983B-4E666CC625FE (1).pdf"));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            System.out.println(text);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
