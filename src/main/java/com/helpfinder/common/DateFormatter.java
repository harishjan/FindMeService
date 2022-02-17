package com.helpfinder.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//This class is used to convert date in system defined format and timezone
public class DateFormatter {
         // this formatter is used to keep the format in which date is stored in file
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");
    private static SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSS");
    public static String convertToSystemDateString(Date date) {
           return dateFormatter.format(Instant.ofEpochMilli(date.getTime())
            .atZone(ZoneOffset.UTC));
    }
    
    public static String convertToSystemDateNowString() {
           return dateFormatter.format(Instant.ofEpochMilli((new Date()).getTime())
                .atZone(ZoneOffset.UTC));    
    }
    
    public static Date convertToSystemDate(String date) throws ParseException{           
           return simpleFormatter.parse(date);           
    }

    public static Date getDateNow(){           
        return  Date.from(Instant.ofEpochMilli((new Date()).getTime())
                .atZone(ZoneOffset.UTC).toInstant());       
 }


}
