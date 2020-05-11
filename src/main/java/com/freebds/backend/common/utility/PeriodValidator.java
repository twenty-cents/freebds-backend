package com.freebds.backend.common.utility;

import com.freebds.backend.exception.FreeBdsApiException;
import org.apache.commons.validator.GenericValidator;

import java.sql.Date;

public class PeriodValidator {

    public void checkValidity(String pDateFrom, String pDateTo) throws FreeBdsApiException {

        // Check if none or all parameters are here
        if((pDateFrom == null && pDateTo != null) || (pDateFrom != null && pDateTo == null)) {
            throw new FreeBdsApiException(
                    "Date de début ou de fin de période invalide.",
                    "début / fin de période => " + pDateFrom + " / " + pDateTo
            );
        }

        // Check dateFrom validity
        if(pDateFrom != null) {
            if(GenericValidator.isDate(pDateFrom, "yyyy-MM-dd", true) ==false) {
                throw new FreeBdsApiException(
                        "Date de début de période invalide.",
                        "début de période => " + pDateFrom
                );
            }
        }
        Date dateFrom = Date.valueOf(pDateFrom);

        // Check dateTo validity
        if(pDateTo != null) {
            if(GenericValidator.isDate(pDateTo, "yyyy-MM-dd", true) ==false) {
                throw new FreeBdsApiException(
                        "Date de fin de période invalide.",
                        "fin de période => " + pDateTo
                );
            }
        }
        Date dateTo = Date.valueOf(pDateTo);

        // Check period validity
        if(dateFrom.toLocalDate().isAfter(dateTo.toLocalDate())) {
            throw new FreeBdsApiException(
                    "Date de début de période supérieure la date de fin de période.",
                    "début de période / fin de période => " +
                            pDateFrom + " / " + pDateTo
            );
        }

    }
}
