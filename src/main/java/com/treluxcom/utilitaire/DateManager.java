/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author ADA-MALICK
 */
public class DateManager {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getDate() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return now.format(DateTimeFormatter.ISO_DATE).substring(0, 10);
    }
}
