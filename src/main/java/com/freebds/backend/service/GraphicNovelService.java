package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.dto.GraphicNovelDTO;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public interface GraphicNovelService {

    /**
     * Retrieve a graphic novel by id
     *
     * @param id the id of the graphic novel to get
     * @return the graphic novel
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    GraphicNovel getGraphicNovelById(Long id) throws EntityNotFoundException;

    GraphicNovelDTO getGraphicNovelByIdWithAuthorRoles(Long id) throws EntityNotFoundException;

    GraphicNovel getGraphicNovelByExternalId(String externalId) throws EntityNotFoundException;

    List<GraphicNovel> getGraphicNovels(Long serieId);

    Page<GraphicNovel> getGraphicNovels(Pageable pageable, Serie serie);

    Page<GraphicNovelDTO> getGraphicNovels(ContextResource contextResource, Pageable pageable, Serie serie);

    GraphicNovelDTO addGraphicNovelDependencies(GraphicNovelDTO graphicNovelDTO);

    GraphicNovel saveAndFlush(GraphicNovel graphicNovel);

    GraphicNovel scrap(Serie serie, ScrapedGraphicNovel scrapedGraphicNovel);

    /**
     * Retrieve all existing graphic novels by multiple criteria
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
     * @return a page of filtered graphic novels
     * @throws CollectionItemNotFoundException in case of invalid selected value request
     */
    Page<GraphicNovel> findBySearchFilters(
            Pageable pageable,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId ) throws CollectionItemNotFoundException;

    /**
     * Count graphic novels
     * @return
     */
    Long count();

    /**
     * Find all graphic novels from a serie within a library
     * @param serieId the id serie to get
     * @param pageable : the page to get
     * @return a page of graphic novels
     */
    Page<GraphicNovel> getGraphicNovelsFromLibraryBySerie(@Param("serieId") Long serieId, Pageable pageable);

}
