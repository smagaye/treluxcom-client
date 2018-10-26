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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.treluxcom.metier.Lignedevis;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author apple
 */
public class DevisPDF {

    private static String nomFichier = "devis";
    private static String adresseFournisseur = "23 rue Ely Manel Fall";
    private static String villeFournisseur = "Dakar";
    private static String telephone = "+221786338816";
    private static String email = "Cisse@gmail.com";
    private static String nomClient = "Ousseynou Cisse";
    private static String societeClient = "Trelux Distribution Service ";
    private static String reference = "002";
    private static String date = DateManager.getDate();
    private static String numeroClient = "13";
    private static String adresseClient = "114 ,Zone de Captage";
    private static String intitule = "Commande de stock de boisson";

    private static String sep = File.separator;
    private static String PATH = "." + sep + "src" + sep + "main" + sep + "resources" + sep + "pdf" + sep + "devis" + sep;
    private static String FILE = PATH + nomFichier + ".pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, BaseColor.BLUE);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.BLUE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD, BaseColor.BLUE);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD, BaseColor.BLUE);

    public static void setAdresseFournisseur(String adresseFournisseur) {
        DevisPDF.adresseFournisseur = adresseFournisseur;
    }

    public static void setVilleFournisseur(String villeFournisseur) {
        DevisPDF.villeFournisseur = villeFournisseur;
    }

    public static void setTelephone(String telephone) {
        DevisPDF.telephone = telephone;
    }

    public static void setEmail(String email) {
        DevisPDF.email = email;
    }

    public static void setNomClient(String nomClient) {
        DevisPDF.nomClient = nomClient;
    }

    public static void setSocieteClient(String societeClient) {
        DevisPDF.societeClient = societeClient;
    }

    public static void setDate(String date) {
        DevisPDF.date = date;
    }

    public static void setNumeroClient(String numeroClient) {
        DevisPDF.numeroClient = numeroClient;
    }

    public static void setAdresseClient(String adresseClient) {
        DevisPDF.adresseClient = adresseClient;
    }

    public static void setIntitule(String intitule) {
        DevisPDF.intitule = intitule;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public static void imprimerDevis(List<Lignedevis> devis) {
        try {
            Document document = new Document();
            FILE = PATH + devis.get(0).getDevis().getCodedevis() + ".pdf";
            FileOutputStream fichier = new FileOutputStream(FILE);
            PdfWriter.getInstance(document, fichier);
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document, devis);
            document.close();
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
        document.addSubject("Devis");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Smag");
        document.addCreator("Smag");
    }

    private static void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Fruit and Juice Society                     "
                + "                        Devis ", catFont));
        addEmptyLine(preface, 1);

        preface.add(new Paragraph(adresseFournisseur, smallBold));
        preface.add(new Paragraph(villeFournisseur, smallBold));
        preface.add(new Paragraph(telephone, smallBold));
        preface.add(new Paragraph(email, smallBold));
        addEmptyLine(preface, 2);

        Paragraph societe = new Paragraph("                                                                                 " + societeClient, subFont);
        societe.setAlignment(678);
        preface.add(societe);
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Reference : " + reference, blueFont));
        preface.add(new Paragraph("Date : " + date, blueFont));
        preface.add(new Paragraph("N° Client : " + numeroClient, blueFont));
        addEmptyLine(preface, 3);

        preface.add(new Paragraph(
                "Intitule : " + intitule,
                blueFont));
        addEmptyLine(preface, 1);
        document.add(preface);

    }

    private static void addContent(Document document, List<Lignedevis> devis) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase("Quantite"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Designation"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);
        

        c1 = new PdfPCell(new Phrase("Prix Unitaire"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Prix Total"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBackgroundColor(BaseColor.CYAN);
        table.addCell(c1);

        Iterator lignedevis = devis.iterator();
        while (lignedevis.hasNext()) {
            Lignedevis ld = (Lignedevis) lignedevis.next();
            if (ld.getQuantite() != 0) {
                table.addCell(ld.getQuantite().toString());
                table.addCell(ld.getFamille().getNom());
                table.addCell(ld.getPrixunitaire().toString());
                table.addCell(ld.getPrixtotal().toString());
            }
        }

        document.add(table);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

}
