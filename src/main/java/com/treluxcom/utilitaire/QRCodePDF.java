/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.treluxcom.metier.Produit;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author apple
 */
public class QRCodePDF {

    private static String nomFichier = "devis";

    private static String sep = File.separator;
    private static String PATH = "src" + sep + "main" + sep + "resources" + sep + "tmp" + sep + "qrcode" + sep;
    private static String PATHQRC = "src" + sep + "main" + sep + "resources" + sep + "pdf" + sep + "qrcode" + sep;
    private static String FILE = PATH + nomFichier + ".pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, BaseColor.BLUE);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.BLUE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD, BaseColor.BLUE);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD, BaseColor.BLUE);

    public static void imprimerDevis(HashMap<String, List<Produit>> rangement, String codestock) {
        try {
            Document document = new Document();
            FILE = PATHQRC + codestock + ".pdf";
            FileOutputStream fichier = new FileOutputStream(FILE);
            PdfWriter.getInstance(document, fichier);
            document.open();
            addMetaData(document);
            addTitlePage(document, codestock);
            addContent(document, rangement);
            document.close();
            FileManager.deleteDirectory(PATH);
            Desktop.getDesktop().open(new File(FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("Trelux Distribution Service");
        document.addSubject("QRCode");
        document.addAuthor("Smag");
        document.addCreator("Smag");
    }

    private static void addTitlePage(Document document, String codestock) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Generation QRCode du Stock " + codestock + " enregistrer le " + DateManager.getDate().toString(), blueFont));
        addEmptyLine(preface, 1);

        document.add(preface);

    }

    private static void addContent(Document document, HashMap<String, List<Produit>> rangement) throws DocumentException {
          
        for (Map.Entry<String, List<Produit>> entry : rangement.entrySet()) {
            String key = entry.getKey();
            List<Produit> value = rangement.get(key);
            
            Paragraph preface = new Paragraph();
            preface.add(new Paragraph("Les QRCodes du produit "+value.get(0).getFamille().getNom(), blueFont));
            addEmptyLine(preface, 1);
            document.add(preface);

            PdfPTable table = new PdfPTable(4);
            PdfPCell c1 = new PdfPCell(new Phrase("Block 1"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(BaseColor.CYAN);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Block 2"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(BaseColor.CYAN);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Block 3"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(BaseColor.CYAN);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Block 4"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(BaseColor.CYAN);
            table.addCell(c1);

            Iterator prods = value.iterator();
            while (prods.hasNext()) {
                try {
                    Produit prod = (Produit) prods.next();
                    String IMG = PATH + prod.getCodeproduit() + ".png";
                    Image image = Image.getInstance(IMG);
                    table.addCell(image);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
            document.add(table);
        }

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
