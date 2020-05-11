package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.common.web.graphicNovel.requests.BarcodeScanRequest;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelMinimumResource;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    GraphicNovelResource getGraphicNovel(ContextResource contextResource, Long id) throws EntityNotFoundException;

    GraphicNovel getGraphicNovelByExternalId(String externalId) throws EntityNotFoundException;

    List<GraphicNovel> getGraphicNovels(Long serieId);

    Page<GraphicNovel> getGraphicNovels(Pageable pageable, Serie serie);

    Page<GraphicNovelResource> getGraphicNovels(ContextResource contextResource, Pageable pageable, Long serieId);

    GraphicNovelResource addGraphicNovelResourceDependencies(ContextResource contextResource, GraphicNovel graphicNovel);

    GraphicNovel saveAndFlush(GraphicNovel graphicNovel);

    GraphicNovel scrap(Serie serie, ScrapedGraphicNovel scrapedGraphicNovel);

    /**
     * Find all graphic novels from a serie within a library
     * @param serieId the id serie to get
     * @param pageable : the page to get
     * @return a page of graphic novels
     */
    Page<GraphicNovel> getGraphicNovelsFromLibraryBySerie(Long serieId, Pageable pageable);

    List<GraphicNovelMinimumResource> getMissingGraphicNovelsFromLibraryBySerie(ContextResource contextResource, Long serieId);

    List<GraphicNovelResource> scan(ContextResource contextResource, BarcodeScanRequest barcodeScanRequest) throws FreeBdsApiException;

}
