package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedAuthorRole;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerie;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerieUrl;
import com.freebds.backend.business.scrapers.bedetheque.serie.BedethequeSerieScraper;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.SerieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class SerieServiceImpl {

    private SerieRepository serieRepository;
    private GraphicNovelService graphicNovelService;
    private AuthorService authorService;
    private GraphicNovelAuthorService graphicNovelAuthorService;

    public SerieServiceImpl(SerieRepository serieRepository,
                            GraphicNovelService graphicNovelService,
                            GraphicNovelAuthorService graphicNovelAuthorService,
                            AuthorService authorService) {
        this.serieRepository = serieRepository;
        this.graphicNovelService = graphicNovelService;
        this.graphicNovelAuthorService = graphicNovelAuthorService;
        this.authorService = authorService;
    }

    public List<String> getDistinctOrigins() {
        return this.serieRepository.findDistinctOrigins();
    }

    public List<String> getDistinctStatus() {
        return this.serieRepository.findDistinctStatus();
    }

    public List<String> getDistinctCategories() {
        return this.serieRepository.findDistinctCategories();
    }

    public Page<Serie> getFilteredSeries(Pageable pageable, String title, String origin, String status, String category) {
        return this.serieRepository.findSeriesByTitleIgnoreCaseContainingAndOriginIgnoreCaseContainingAndStatusIgnoreCaseContainingAndCategoriesIgnoreCaseContaining(
                pageable, title, origin, status, category
        );
    }

    //@PostConstruct
//    public void testdb(){
//        GraphicNovel gc = graphicNovelRepository.findById(169L).get();
//
//        System.out.println(gc.getTitle());
//        System.out.println("size=" + gc.getGraphicNovelAuthors().size());
//        for(GraphicNovelAuthor graphicNovelAuthor : gc.getGraphicNovelAuthors()){
//            System.out.println(graphicNovelAuthor.getRole() + " = " + graphicNovelAuthor.getAuthor().toString());
//        }
//        //System.out.println(gc.getGraphicNovelAuthors().toString());
//
//        Author author = authorRepository.findById(33648L).get();
//        System.out.println(author.getLastname());
//        System.out.println(author.getGraphicNovelAuthors().size());
//        for(GraphicNovelAuthor graphicNovelAuthor : author.getGraphicNovelAuthors()){
//            System.out.println(graphicNovelAuthor.getRole() + " = " + graphicNovelAuthor.getGraphicNovel().getTome() + ". " + graphicNovelAuthor.getGraphicNovel().getTitle());
//        }
//
//    }

    /**
     * Scrap a serie from https://www.bedetheque.com
     * @param serieUrl
     * @return
     * @throws IOException
     */
    //@PostConstruct
    public Serie scrapSerie(String serieUrl) throws IOException {
        //String serieUrl = "https://www.bedetheque.com/serie-59-BD-Asterix__10000.html";
        // Instantiate serie Scraper
        BedethequeSerieScraper bedethequeSerieScraper = new BedethequeSerieScraper();
        // Load serie
        ScrapedSerie scrapSerie = bedethequeSerieScraper.scrap(serieUrl);

        //System.out.println("Scrap = " + scrapSerie.toString());

        // Get Serie
        Serie serie = serieRepository.findFirstByExternalId(scrapSerie.getExternalId());
        if(serie == null){
            serie = new Serie();
        }
        serie.setExternalId(scrapSerie.getExternalId());
        serie.setTitle(scrapSerie.getTitle());
        serie.setLangage(scrapSerie.getLangage());
        serie.setStatus(scrapSerie.getStatus());
        serie.setOrigin(scrapSerie.getOrigin());
        serie.setCategories(scrapSerie.getCategory());
        serie.setSynopsys(scrapSerie.getSynopsys());
        serie.setPageUrl(scrapSerie.getPictureUrl());
        serie.setPageThumbnailUrl(scrapSerie.getPictureThbUrl());
        serie.setScrapUrl(scrapSerie.getScrapUrl());
        serie.setIsScraped(true);
        serie.setCreationDate(scrapSerie.getCreationDate());
        serie.setCreationUser(scrapSerie.getCreationUser());
        serie.setLastUpdateDate(scrapSerie.getLastUpdateDate());
        serie.setLastUpdateUser(scrapSerie.getLastUpdateUser());

        // Save serie
        serie = serieRepository.saveAndFlush(serie);

        // Graphic novels
        for(ScrapedGraphicNovel sgc : scrapSerie.getScrapedGraphicNovels()) {
            GraphicNovel gc;
            try {
                gc = graphicNovelService.getGraphicNovelByExternalId(sgc.getExternalId());
            } catch (EntityNotFoundException e) {
                gc = graphicNovelService.scrap(serie, sgc);
            };

            // Authors
            for(ScrapedAuthorRole authorRole : sgc.getAuthors()) {
                Author author;
                try {
                    author = authorService.getAuthorByExternalId(authorRole.getExternalId());
                } catch (EntityNotFoundException e) {
                    // New author, so scrap it
                    GenericAuthorUrl authorUrl = new GenericAuthorUrl();
                    authorUrl.setName(authorRole.getName());
                    authorUrl.setUrl(authorRole.getAuthorUrl());
                    // Scrap and save author
                    author = authorService.scrapAuthor(authorUrl);
                }

                // Save author role
                graphicNovelAuthorService.associate(gc, author, authorRole.getRole());
            }


        }

        return serie;
    }

    /**
     * Scrap all new series from https://wwww.bedetheque.com
     * and add them into db
     * @throws IOException
     * @throws InterruptedException
     */
    @PostConstruct
    public void scrapNewSeriesInDb() throws IOException, InterruptedException {
        // Instantiate serie Scraper
        BedethequeSerieScraper bedethequeSerieScraper = new BedethequeSerieScraper();

        // Step 1 - Get all existing authors urls from http://www.bedetheque.com
        List<ScrapedSerieUrl> scrapedSerieUrls = bedethequeSerieScraper.listAll();

        // Step 2 - Parse series and add the unknown ones into the db
        for (ScrapedSerieUrl scrapedSerieUrl : scrapedSerieUrls) {

            // Extract URL and serie external id
            String externalId = scrapedSerieUrl.getUrl().split("-")[1];

            // Existing serie in db?
            Serie serie = serieRepository.findFirstByExternalId(externalId);
            if(serie == null){
                  // Scrap serie
                serie = this.scrapSerie(scrapedSerieUrl.getUrl());
                List<GraphicNovel> graphicNovels = this.graphicNovelService.getGraphicNovels(serie.getId());
                // Save serie
                serie = serieRepository.saveAndFlush(serie);
                System.out.println(" ==> Série ajoutée=" + serie.getTitle() + " (" + graphicNovels.size() + " tomes)");
                Thread.sleep(1000);
            } else {
                //System.out.println("Author " + authorUrlDTO.getName() + " already exists in db");
            }
        }

    }

}
