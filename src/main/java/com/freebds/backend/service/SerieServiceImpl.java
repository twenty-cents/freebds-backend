package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.bedetheque.dto.*;
import com.freebds.backend.business.scrapers.bedetheque.serie.BedethequeSerieScraper;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.GraphicNovelAuthor;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.AuthorRepository;
import com.freebds.backend.repository.GraphicNovelAuthorRepository;
import com.freebds.backend.repository.GraphicNovelRepository;
import com.freebds.backend.repository.SerieRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SerieServiceImpl {

    private AuthorService authorService;
    private SerieRepository serieRepository;
    private GraphicNovelRepository graphicNovelRepository;
    private AuthorRepository authorRepository;
    private GraphicNovelAuthorRepository graphicNovelAuthorRepository;

    public SerieServiceImpl(SerieRepository serieRepository,
                            GraphicNovelRepository graphicNovelRepository,
                            AuthorRepository authorRepository,
                            GraphicNovelAuthorRepository graphicNovelAuthorRepository,
                            AuthorService authorService) {
        this.serieRepository = serieRepository;
        this.graphicNovelRepository = graphicNovelRepository;
        this.authorRepository = authorRepository;
        this.graphicNovelAuthorRepository = graphicNovelAuthorRepository;
        this.authorService = authorService;
    }

    //@PostConstruct
    public void testdb(){
        GraphicNovel gc = graphicNovelRepository.findById(169L).get();

        System.out.println(gc.getTitle());
        System.out.println("size=" + gc.getGraphicNovelAuthors().size());
        for(GraphicNovelAuthor graphicNovelAuthor : gc.getGraphicNovelAuthors()){
            System.out.println(graphicNovelAuthor.getRole() + " = " + graphicNovelAuthor.getAuthor().toString());
        }
        //System.out.println(gc.getGraphicNovelAuthors().toString());

        Author author = authorRepository.findById(33648L).get();
        System.out.println(author.getLastname());
        System.out.println(author.getGraphicNovelAuthors().size());
        for(GraphicNovelAuthor graphicNovelAuthor : author.getGraphicNovelAuthors()){
            System.out.println(graphicNovelAuthor.getRole() + " = " + graphicNovelAuthor.getGraphicNovel().getTome() + ". " + graphicNovelAuthor.getGraphicNovel().getTitle());
        }

    }

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

            GraphicNovel gc = graphicNovelRepository.findFirstByExternalId(sgc.getExternalId());
            if(gc == null){
                gc = new GraphicNovel();
            }

            gc.setSerie(serie);
            gc.setExternalId(sgc.getExternalId());
            gc.setTome(sgc.getTome());
            gc.setNumEdition(sgc.getNumEdition());
            gc.setTitle(sgc.getTitle());
            gc.setGraphicNovel_Url(sgc.getAlbumUrl());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                gc.setPublicationDate(LocalDate.parse("01/" + sgc.getPublicationDate(), formatter));
            } catch (Exception e) {
                gc.setPublicationDate(null);
            }

            try {
                gc.setReleaseDate(LocalDate.parse(sgc.getReleaseDate(), formatter));
            } catch (Exception e) {
                gc.setReleaseDate(null);
            }

            gc.setPublisher(sgc.getPublisher());
            gc.setCollection(sgc.getCollection());
            gc.setIsbn(sgc.getIsbn());
            gc.setTotalPages(sgc.getTotalPages());
            gc.setFormat(sgc.getFormat());
            gc.setIsOriginalEdition(sgc.isOriginalPublication());
            gc.setIsIntegrale(sgc.isIntegrale());
            gc.setIsBroche(sgc.isBroche());
            gc.setInfoEdition(sgc.getInfoEdition());
            gc.setReeditionUrl(sgc.getReeditionUrl());
            gc.setExternalIdOriginalPublication(sgc.getExternalIdOriginalPublication());
            gc.setCoverPictureUrl(sgc.getCoverPictureUrl());
            gc.setCoverThumbnailUrl(sgc.getCoverThumbnailUrl());
            gc.setBackCoverPictureUrl(sgc.getBackCoverPictureUrl());
            gc.setBackCoverThumbnailUrl(sgc.getBackCoverThumbnailUrl());
            gc.setPageUrl(sgc.getPagePictureUrl());
            gc.setPageThumbnailUrl(sgc.getPageThumbnailUrl());
            gc.setScrapUrl(scrapSerie.getScrapUrl());
            gc.setIsScraped(true);
            gc.setCreationDate(scrapSerie.getCreationDate());
            gc.setCreationUser(scrapSerie.getCreationUser());
            gc.setLastUpdateDate(scrapSerie.getLastUpdateDate());
            gc.setLastUpdateUser(scrapSerie.getLastUpdateUser());

            // Save graphic novel
            gc = graphicNovelRepository.saveAndFlush(gc);
            //System.out.println("Album=" + gc.getTitle());

            // Authors
            for(ScrapedAuthorRole authorRole : sgc.getAuthors()) {
                Author author = authorRepository.findFirstByExternalId(authorRole.getExternalId());
                if(author == null) {
                    // TODO : auteur à créer
                    GenericAuthorUrl authorUrl = new GenericAuthorUrl();
                    authorUrl.setName(authorRole.getName());
                    authorUrl.setUrl(authorRole.getAuthorUrl());
                    author = authorService.scrapAuthor(authorUrl);
                    // Save author
                    author = authorRepository.saveAndFlush(author);
                    System.out.println("Author unknown = " + authorRole.getExternalId() + " , " + authorRole.getName());
                    System.out.println("Author unknown = " + author.getExternalId() + " , " + author.getId());
                } else {

                }

                // Save author role
                List<GraphicNovelAuthor> graphicNovelAuthors = graphicNovelAuthorRepository.isRoleExist(gc.getId(), author.getId(), authorRole.getRole());
                if(graphicNovelAuthors.size() == 0) {
                    //System.out.println("new one=" +  authorRole.toString());
                    GraphicNovelAuthor graphicNovelAuthor = new GraphicNovelAuthor();
                    graphicNovelAuthor.setGraphicNovel(gc);
                    graphicNovelAuthor.setAuthor(author);
                    graphicNovelAuthor.setRole(authorRole.getRole());
                    graphicNovelAuthor = graphicNovelAuthorRepository.saveAndFlush(graphicNovelAuthor);
                    //System.out.println("Role=" + graphicNovelAuthor.toString());
                }
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
    //@PostConstruct
    public void scrapNewSeriesInDb() throws IOException, InterruptedException {
        // Instantiate serie Scraper
        BedethequeSerieScraper bedethequeSerieScraper = new BedethequeSerieScraper();

        // Step 1 - Get all existing authors urls from http://www.bedetheque.com
        List<ScrapedSerieUrl> scrapedSerieUrls = new ArrayList<>();
        String[] letters = new String("0-A-B-C-D-E-F-G-H-I-J-K-L-M-N-O-P-Q-R-S-T-U-V-W-X-Y-Z").split("-");
        for(String letter : letters){
            List<ScrapedSerieUrl> sc = bedethequeSerieScraper.listByLetter(letter);
            scrapedSerieUrls.addAll(sc);
        }

        // Step 2 - Parse series and add the unknown ones into the db
        for (ScrapedSerieUrl scrapedSerieUrl : scrapedSerieUrls) {

            // Extract URL and serie external id
            String externalId = scrapedSerieUrl.getUrl().split("-")[1];

            // Existing serie in db?
            Serie serie = serieRepository.findFirstByExternalId(externalId);
            if(serie == null){
                // Scrap serie
                serie = this.scrapSerie(scrapedSerieUrl.getUrl());
                // Save serie
                //serie = serieRepository.saveAndFlush(serie);
                //System.out.println("Série ajoutée=" + serie.getTitle() + " (" + serie.getGraphicNovels().size() + " tomes)");
                Thread.sleep(1000);
            } else {
                //System.out.println("Author " + authorUrlDTO.getName() + " already exists in db");
            }
        }

    }

}
