package com.treluxcom.utilitaire;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import static javafx.application.Application.launch;

public class FileManager {

    static Date date = new Date();
    private static final String logspring = new String("src/main/resources/log/logspring.txt");
    private static final String loghibernate = new String("src/main/resources/log/loghibernate.txt");
    private static String log = new String("src/main/resources/log/log.txt");
    private static String sep = File.separator;

    static public void deleteDirectory(String emplacement) {
        File path = new File(emplacement);
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(path + sep + files[i]);
                }
                files[i].delete();
            }
        }
    }

    public static String getLogspring() {
        return logspring;
    }

    public static String getLoghibernate() {
        return loghibernate;
    }

    public static String getLog() {
        return log;
    }

    public static void deleteDirectoryWithContains(File file) {
        String[] entries = file.list();
        for (String s : entries) {
            File currentFile = new File(file.getPath(), s);
            currentFile.delete();
        }
        file.delete();
    }

    public static boolean effacerRepertoire(File repertoire) {
        if (repertoire.exists() && repertoire.isDirectory()) {
            boolean resultat = true;
            File[] fichiers = repertoire.listFiles();
            for (File fichier : fichiers) {
                if (fichier.isDirectory()) {
                    resultat &= effacerRepertoire(fichier);
                } else {
                    resultat &= fichier.delete();
                }
            }
            resultat &= repertoire.delete();
            return resultat;
        }
        return false;
    }

    public static boolean copier(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        
    
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            try {
                is.close();
                os.close();
            } catch (Exception ex) {
                
            }
        }
        return true;
    }

    public static String getExtension(File fichier) {
        return fichier.getAbsolutePath().substring(fichier.getAbsolutePath().indexOf("."), fichier.getAbsolutePath().length());
    }

    public static String getExtensionOnly(File fichier) {
        return fichier.getAbsolutePath().substring(fichier.getAbsolutePath().indexOf("."), fichier.getAbsolutePath().length());
    }

    public static String getExtension(String fichier) {
        return fichier.substring(fichier.indexOf(".") + 1, fichier.length());
    }

    /*
 * Lecture d'un fichier texte
 * @param url Le chemin de du fichier d'extension.txt
 * */
    public static void lire(String location) throws IOException {
        try (FileReader fichier = new FileReader(location); BufferedReader buffer = new BufferedReader(fichier)) {
            String ligne_lue = buffer.readLine();
            while (ligne_lue != null) {
                System.out.println(ligne_lue);
                ligne_lue = buffer.readLine();
            }
        }
    }

    public static String getFileInLine(String location) throws IOException {
        String ligne_lue = " ";
        try (FileReader fichier = new FileReader(location); BufferedReader buffer = new BufferedReader(fichier)) {
            ligne_lue = buffer.readLine();
            while (ligne_lue != null) {
                System.out.println(ligne_lue);
                ligne_lue += buffer.readLine();
            }
        }
        return ligne_lue.trim();
    }

    public static void lire(File location) throws IOException {
        try (FileReader fichier = new FileReader(location.getAbsoluteFile()); BufferedReader buffer = new BufferedReader(fichier)) {
            String ligne_lue = buffer.readLine();
            while (ligne_lue != null) {
                System.out.println(ligne_lue);
                ligne_lue = buffer.readLine();
            }
        }
    }


    /*
	 * Création d'un fichier texte
	 * @param url Le chemin de du fichier d'extension.txt
	 * */
    public static void creer(String file) {
        File fichier = new File(file);
        try {
            if (fichier.exists() && fichier.isFile()) {
                FileManager.ecrire(fichier.getAbsolutePath(), date.toInstant() + " | Fichier existant | " + fichier.getAbsolutePath());
            } else {
                File chemin = fichier.getParentFile();
                if (chemin.mkdirs()) {
                    fichier.createNewFile();
                    FileManager.ecrire(fichier.getAbsolutePath(), date.toInstant() + " | Fichier Cree     | " + fichier.getAbsolutePath());
                } else {
                    FileManager.ecrire(fichier.getAbsolutePath(), date.toInstant() + " | Fichier Existant | " + fichier.getAbsolutePath());
                }
            }
        } catch (IOException e) {
        }
    }

    public static void ecrire(String chemin, String texte) {
        try {
            if (!new File(chemin).exists()) {
                FileManager.creer(chemin);
            }

            FileWriter fichier = new FileWriter(chemin, true);
            try (BufferedWriter filtre = new BufferedWriter(fichier)) {
                filtre.write(texte);
                filtre.newLine();
            }
        } catch (IOException e) {
        }
    }

    public static void logErreur(String chemin, String statut, String message) {
        try {
            FileManager.ecrire(chemin, FileManager.date.toInstant() + " | 	" + statut + "  | " + message);
        } catch (Exception e) {
            FileManager.logErreur(chemin, "Echec", e.getMessage());
        }

    }
}
