package com.alicja.ISS.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTimeConvertedToTimeStamp {

    public static Timestamp convertStringIntoTimeStamp(String dateTime) throws ParseException {
        Timestamp timestamp;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh");
            Date parsedDate = dateFormat.parse(dateTime);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

            return timestamp;
    }

}
