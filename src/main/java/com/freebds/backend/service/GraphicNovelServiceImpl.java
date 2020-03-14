package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.GraphicNovelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GraphicNovelServiceImpl implements GraphicNovelService {

    private GraphicNovelRepository graphicNovelRepository;

    public GraphicNovelServiceImpl(GraphicNovelRepository graphicNovelRepository) {
        this.graphicNovelRepository = graphicNovelRepository;
    }

    /**
     * Get a graphic novel by external id
     *
     * @param externalId the graphic novel external id to get
     * @return a graphic novel
     * @throws EntityNotFoundException in case external id is not found in DB.
     */
    @Override
    public GraphicNovel getGraphicNovelByExternalId(String externalId) throws EntityNotFoundException {
        List<GraphicNovel> graphicNovels = graphicNovelRepository.findGraphicNovelsByExternalId(externalId);

        if(graphicNovels.size() == 0){
            throw new EntityNotFoundException(externalId, "Graphic Novel");
        } else {
            return graphicNovels.get(0);
        }
    }

    @Override
    public List<GraphicNovel> getGraphicNovels(Long serieId) {
        return this.graphicNovelRepository.findGraphicNovelsBySerieIdOrdeOrderByTome(serieId);
    }

    /**
     * Save a graphic novel into the db
     *
     * @param graphicNovel the graphic novel to save
     * @return the saved graphic novel
     */
    @Override
    public GraphicNovel saveAndFlush(GraphicNovel graphicNovel) {
        return graphicNovelRepository.saveAndFlush(graphicNovel);
    }

    /**
     * Save a scraped graphic novel from http://www.bedetheque.com into the db
     *
     * @param serie the serie associated with the scraped graphic novel to save
     * @param scrapedGraphicNovel the scraped graphic novel to save
     * @return the saved graphic novel
     */
    @Override
    public GraphicNovel scrap(Serie serie, ScrapedGraphicNovel scrapedGraphicNovel) {
        GraphicNovel gc = new GraphicNovel();

        gc.setSerie(serie);
        gc.setExternalId(scrapedGraphicNovel.getExternalId());
        gc.setTome(scrapedGraphicNovel.getTome());
        gc.setNumEdition(scrapedGraphicNovel.getNumEdition());
        gc.setTitle(scrapedGraphicNovel.getTitle());
        gc.setGraphicNovel_Url(scrapedGraphicNovel.getAlbumUrl());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            gc.setPublicationDate(LocalDate.parse("01/" + scrapedGraphicNovel.getPublicationDate(), formatter));
        } catch (Exception e) {
            gc.setPublicationDate(null);
        }

        try {
            gc.setReleaseDate(LocalDate.parse(scrapedGraphicNovel.getReleaseDate(), formatter));
        } catch (Exception e) {
            gc.setReleaseDate(null);
        }

        gc.setPublisher(scrapedGraphicNovel.getPublisher());
        gc.setCollection(scrapedGraphicNovel.getCollection());
        gc.setIsbn(scrapedGraphicNovel.getIsbn());
        gc.setTotalPages(scrapedGraphicNovel.getTotalPages());
        gc.setFormat(scrapedGraphicNovel.getFormat());
        gc.setIsOriginalEdition(scrapedGraphicNovel.isOriginalPublication());
        gc.setIsIntegrale(scrapedGraphicNovel.isIntegrale());
        gc.setIsBroche(scrapedGraphicNovel.isBroche());
        gc.setInfoEdition(scrapedGraphicNovel.getInfoEdition());
        gc.setReeditionUrl(scrapedGraphicNovel.getReeditionUrl());
        gc.setExternalIdOriginalPublication(scrapedGraphicNovel.getExternalIdOriginalPublication());
        gc.setCoverPictureUrl(scrapedGraphicNovel.getCoverPictureUrl());
        gc.setCoverThumbnailUrl(scrapedGraphicNovel.getCoverThumbnailUrl());
        gc.setBackCoverPictureUrl(scrapedGraphicNovel.getBackCoverPictureUrl());
        gc.setBackCoverThumbnailUrl(scrapedGraphicNovel.getBackCoverThumbnailUrl());
        gc.setPageUrl(scrapedGraphicNovel.getPagePictureUrl());
        gc.setPageThumbnailUrl(scrapedGraphicNovel.getPageThumbnailUrl());
        gc.setScrapUrl(serie.getScrapUrl());
        gc.setIsScraped(true);
        gc.setCreationDate(serie.getCreationDate());
        gc.setCreationUser(serie.getCreationUser());
        gc.setLastUpdateDate(serie.getLastUpdateDate());
        gc.setLastUpdateUser(serie.getLastUpdateUser());

        // Save graphic novel
        return graphicNovelRepository.saveAndFlush(gc);
    }
}
