/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author apple
 */
public class PropertiesManager {

    private static final String sep = File.separator;
    private static String PATH = "." + sep + "src" + sep + "main" + sep + "resources" + sep;
    private static String FILE = "reseauConfig";

    public static void createPropertiesFile(String FILE) {

        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(PATH + FILE + ".properties");
            System.out.println(prop.setProperty("port", "2000"));
            System.out.println(prop.setProperty("adresseip", "127.0.0.1"));
            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void createPropertiesFile(String adresseIp , int port) {

        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(PATH + FILE + ".properties");
            System.out.println(prop.setProperty("port", String.valueOf(port)));
            System.out.println(prop.setProperty("adresseip", adresseIp));
            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
