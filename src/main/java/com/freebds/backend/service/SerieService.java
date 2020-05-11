package com.freebds.backend.service;

import com.freebds.backend.common.web.serie.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.common.web.dashboard.resources.SeriesStatusCounterResource;
import com.freebds.backend.common.web.serie.resources.*;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface SerieService {

    /**
     * Retrieve all existing series <code>origin types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series origin types, ordered by asc
     */
    OriginsResource getDistinctOrigins();

    /**
     * Retrieve all existing series <code>status types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series status types, ordered by asc
     */
    StatusResource getDistinctStatus();

    /**
     * Retrieve all existing series <code>category types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series category types, ordered by asc
     */
    CategoriesResource getDistinctCategories();

    /**
     * Retrieve all existing series <code>language</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series language, ordered by asc
     */
    LanguageResource getDistinctLanguages();

    /**
     * Retrieve a serie by id
     *
     * @param serieId the id of the serie to get
     * @return the found serie
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    Serie getSerieById(Long serieId) throws EntityNotFoundException;

    /**
     * Retrieve a serie by id
     *
     * @param contextResource the context to get
     * @param serieId the id of the serie to get
     * @return the found serie
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    SerieResource getSerie(ContextResource contextResource, Long serieId) throws EntityNotFoundException;

    /**
     * Scrap a serie from https://www.bedetheque.com
     *
     * @param serieUrl the serie url to scrap
     * @return the scraped serie
     * @throws IOException in case of http access error
     */
    Serie scrapSerie(String serieUrl) throws IOException;

    /**
     * Scrap all new series from https://wwww.bedetheque.com
     * and add them into db
     *
     * @throws IOException in case of http access error
     */
    void scrapNewSeriesInDb() throws IOException;

    /**
     * Find all series by author's roles
     * @param authorId the author id to get
     * @return the list of author's series
     */
    List<AuthorRolesBySerieResource> getSeriesByAuthorRoles(ContextResource contextResource, Long authorId);

    /**
     * Retrieve all series starting with the given letter
     * @param contextResource the context to get
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of series
     */
    Page<SerieResource> getSeriesByTitleStartingWith(ContextResource contextResource, String letter, Pageable pageable);

    /**
     * Count series by status
     * @return
     */
    List<SeriesStatusCounterResource> countSeriesByStatus();
}
