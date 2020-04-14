package com.freebds.backend.mapper;

import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class DateMapper {

    public static String asString(Date date) {
        return date != null ? new SimpleDateFormat( "yyyy-MM-dd")
                .format( date ) : null;
    }

    public static Date asDate(String date) {
        try {
            return date != null ? new java.sql.Date(new SimpleDateFormat( "yyyy-MM-dd" )
                    .parse( date ).getTime()) : null;
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
    }
}
