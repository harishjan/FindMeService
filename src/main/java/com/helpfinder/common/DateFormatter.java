package com.helpfinder.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//This class is used to convert date in system defined format and timezone
public class DateFormatter {
         // this formatter is used to keep the format in which date is stored in file
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");
    private static SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSS");
    private static SimpleDateFormat onlyDateformatter = new SimpleDateFormat("dd/MM/yyyy");

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
    
    //compares two date and return 0 if same date without time
    public static int compareDatePart(Date date1, Date date2) {
        
        Date dateSecond = null;
        Date dateFrist = null;
        try {
            dateSecond = onlyDateformatter.parse(onlyDateformatter.format(date2));
            dateFrist = onlyDateformatter.parse(onlyDateformatter.format(date1));            
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(dateFrist.equals(dateSecond))  
            return 0;
        else if(dateFrist.before(dateSecond))
            return -1;
        else
            return 1;
         
        
    }


}
