package com.treluxcom.utilitaire;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Reseau {
    private static final String sep = File.separator;
    private static String PATH = "." + sep + "src" + sep + "main" + sep + "resources" + sep;
    private static String FILE = "reseauConfig";
    private static String port;
    private static String adresseip;
    
    public static String getPort() {
        init();
        return port;
    }

    public static String getAdresseip() {
        init();
        return adresseip;
    }

    public static void init (){
            Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(PATH +FILE+ ".properties");
            prop.load(input);
            port=prop.getProperty("port");
            adresseip=prop.getProperty("adresseip");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
