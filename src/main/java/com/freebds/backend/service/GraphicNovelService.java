package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.exception.EntityNotFoundException;
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

    GraphicNovel getGraphicNovelByExternalId(String externalId) throws EntityNotFoundException;

    List<GraphicNovel> getGraphicNovels(Long serieId);

    Page<GraphicNovel> getGraphicNovels(Pageable pageable, Serie serie);

    GraphicNovel saveAndFlush(GraphicNovel graphicNovel);

    GraphicNovel scrap(Serie serie, ScrapedGraphicNovel scrapedGraphicNovel);
}
