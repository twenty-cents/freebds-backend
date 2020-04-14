package com.freebds.backend.common.utility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Utility class for convert util.Date <--> LocalDate when writing or reading the database
 * Change the autoApply property to activate / deactivate the convert process.
 */
@Converter(autoApply = false)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate locDate) {
        //default time zone
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return locDate == null ? null : Date.from(locDate.atStartOfDay(defaultZoneId).toInstant());
        //return locDate == null ? null : Date.valueOf(locDate);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
        //default time zone
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return sqlDate == null ? null : sqlDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //return sqlDate == null ? null : sqlDate.toLocalDate();
    }
}