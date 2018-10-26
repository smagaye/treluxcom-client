/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.StringTokenizer;

public class GenerateCode {

    public static String clefUTC(String prefixe) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        StringTokenizer dateSale = new StringTokenizer(now.toString(), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ:.,/!?\\-_");
        String dateUTC = prefixe;
        while (dateSale.hasMoreTokens()) {
            dateUTC += dateSale.nextToken();
        }
        return (dateUTC + "000").substring(0, 20);
    }

}
