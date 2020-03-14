package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GraphicNovelService {

    GraphicNovel getGraphicNovelByExternalId(String externalId) throws EntityNotFoundException;

    List<GraphicNovel> getGraphicNovels(Long serieId);

    GraphicNovel saveAndFlush(GraphicNovel graphicNovel);

    GraphicNovel scrap(Serie serie, ScrapedGraphicNovel scrapedGraphicNovel);
}
