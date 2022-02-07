package com.helpfinder.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//This class is used to convert date in system defined format and timezone
public class DateFormatter {
	  // this formatter is used to keep the format in which date is stored in file
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");
    private static SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSS");
    public static String convertToSystemDateString(Date date) {
    	return dateFormatter.format(Instant.ofEpochMilli(date.getTime())
            .atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
    
    public static String convertToSystemDateNowString() {
    	return dateFormatter.format(Instant.ofEpochMilli((new Date()).getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());    
    }
    
    public static Date convertToSystemDate(String date) throws ParseException{    	
    	return simpleFormatter.parse(date);    	
    }


}
