package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
public interface AuthorService {

    Author getAuthorById(Long id);

    Page getAuthors(Pageable page);

    Page<GraphicNovel> getGraphicNovelsByAuthorId(Pageable page, Long authorId);

    Author getAuthorByExternalId(String externalId) throws EntityNotFoundException;

    /**
     * Retrieve all existing nationalities <code>nationality</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct author's nationalities, ordered by asc
     */
    List<String> getDistinctNationalities();

    Page<Author> getFilteredAuthors(Pageable pageable, String lastname, String firstname, String nickname, String nationality);

    Author scrapAuthor(GenericAuthorUrl authorUrl) throws IOException;

    void scrapNewAuthorsInDb() throws IOException, InterruptedException;

    /**
     * Retrieve all existing authors by multiple criteria
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
     * @return a page of filtered authors
     * @throws CollectionItemNotFoundException in case of invalid selected value request
     */
    Page<Author> findBySearchFilters(
            Pageable pageable,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId ) throws CollectionItemNotFoundException;

    /**
     * Retrieve all authors starting with the given letter
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of authors
     */
    Page<Author> getAuthorsByLastnameStartingWith(@Param("letter") String letter, Pageable pageable);

    /**
     * Count authors
     * @return the count
     */
    Long count();

}
