package com.freebds.backend.service;

import com.freebds.backend.common.web.freeSearch.resources.FreeSearchResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.FreeBdsApiException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public interface FreeSearchService {

    FreeSearchResource search (
            // Common parameters
            Pageable pageable, ContextResource contextResource, String freeSearchType, Long libraryId,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId, String nationality ) throws CollectionItemNotFoundException, FreeBdsApiException;

}
