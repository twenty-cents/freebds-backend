package com.freebds.backend.service;

import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public interface FreeSearchService {

    Page<Serie> searchSeries (
            // Common parameters
            Pageable pageable, ContextResource contextResource, Long libraryId,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId, String nationality ) throws CollectionItemNotFoundException;

    Page<GraphicNovel> searchGraphicNovels (
            // Common parameters
            Pageable pageable, ContextResource contextResource, Long libraryId,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId, String nationality ) throws CollectionItemNotFoundException;

    Page<Author> searchAuthors (
            // Common parameters
            Pageable pageable, ContextResource contextResource, Long libraryId,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId, String nationality ) throws CollectionItemNotFoundException;
}
