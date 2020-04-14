package com.freebds.backend.service;

import com.freebds.backend.common.web.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.resources.SeriesOriginCounterResource;
import com.freebds.backend.common.web.resources.SeriesStatusCounterResource;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
public interface SerieService {

    /**
     * Retrieve all existing series <code>origin types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series origin types, ordered by asc
     */
    List<String> getDistinctOrigins();

    /**
     * Retrieve all existing series <code>status types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series status types, ordered by asc
     */
    List<String> getDistinctStatus();

    /**
     * Retrieve all existing series <code>category types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series category types, ordered by asc
     */
    List<String> getDistinctCategories();

    /**
     * Retrieve all existing series <code>language</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series language, ordered by asc
     */
    List<String> getDistinctLanguages();

    /**
     * Retrieve all series, filtered by title, origin, status and category
     *
     * @param pageable the page to get
     * @param title the title to get
     * @param origin the origin to get
     * @param status the status to get
     * @param category the category to get
     * @return a page of filtered series
     */
    Page<Serie> getFilteredSeries(Pageable pageable, String title, String origin, String status, String category);

    /**
     * Retrieve all existing series by multiple criteria
     * @param pageable the page to get
     * @param serieTitle the serie title to get
     * @param serieExternalId the serie external id to get
     * @param categories the serie category to get
     * @param status the serie status to get
     * @param origin the serie origin to get
     * @param language the serie language to get
     * @param graphicNovelTitle the graphic novel title to get
     * @param graphicNovelExternalId the graphic novel external id to get
     * @param publisher the graphic novel publisher to get
     * @param collection the graphic novel collection to get
     * @param isbn the graphic novel ISBN to get
     * @param publicationDateFrom the graphic novel publication date from to get
     * @param publicationDateTo the graphic novel publication date to to get
     * @param lastname the author lastname to get
     * @param firstname the author firstname to get
     * @param nickname the author nickname to get
     * @param authorExternalId the author external id to get
     * @return a page of filtered series
     * @throws CollectionItemNotFoundException in case of invalid selected value request
     */
    Page<Serie> findBySearchFilters(
         Pageable pageable,
         // Serie filters parameters
         String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
         // Graphic novel filters parameters
         String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
         // Author filters parameters
         String lastname, String firstname, String nickname, String authorExternalId ) throws CollectionItemNotFoundException;

    /**
     * Retrieve a serie by id
     *
     * @param serieId the id of the serie to get
     * @return the found serie
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    Serie getSerieById(Long serieId) throws EntityNotFoundException;

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
    List<AuthorRolesBySerieResource> getSeriesByAuthorRoles(Long authorId);

    /**
     * Retrieve all series starting with the given letter
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of series
     */
    Page<Serie> getSeriesByTitleStartingWith(@Param("letter") String letter, Pageable pageable);

    /**
     * Count series
     * @return the count
     */
    Long count();

    /**
     * Count series by origin
     * @return
     */
    List<SeriesOriginCounterResource> countSeriesByOrigin();


    /**
     * Count series by status
     * @return
     */
    List<SeriesStatusCounterResource> countSeriesByStatus();
}
