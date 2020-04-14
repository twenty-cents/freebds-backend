package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedAuthorRole;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerie;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerieUrl;
import com.freebds.backend.business.scrapers.bedetheque.serie.BedethequeSerieScraper;
import com.freebds.backend.common.web.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.resources.SeriesOriginCounterResource;
import com.freebds.backend.common.web.resources.SeriesStatusCounterResource;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.SerieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SerieServiceImpl implements SerieService {

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

    /**
     * Retrieve all existing series <code>origin types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series origin types, ordered by asc
     */
    @Override
    public List<String> getDistinctOrigins() {
        return this.serieRepository.findDistinctOrigins();
    }

    /**
     * Retrieve all existing series <code>status types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series status types, ordered by asc
     */
    @Override
    public List<String> getDistinctStatus() {
        return this.serieRepository.findDistinctStatus();
    }

    /**
     * Retrieve all existing series <code>category types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series category types, ordered by asc
     */
    @Override
    public List<String> getDistinctCategories() {
        return this.serieRepository.findDistinctCategories();
    }

    /**
     * Retrieve all existing series <code>language</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series language, ordered by asc
     */
    @Override
    public List<String> getDistinctLanguages() {
        return this.serieRepository.findDistinctLanguages();
    }

    /**
     * Retrieve all series, filtered by title, origin, status and category
     *
     * @param pageable the page to get
     * @param title    the title to get
     * @param origin   the origin to get
     * @param status   the status to get
     * @param category the category to get
     * @return a page of filtered series
     */
    @Override
    public Page<Serie> getFilteredSeries(Pageable pageable, String title, String origin, String status, String category) {
        return this.serieRepository.findSeriesByTitleIgnoreCaseContainingAndOriginIgnoreCaseContainingAndStatusIgnoreCaseContainingAndCategoriesIgnoreCaseContaining(
                pageable, title, origin, status, category
        );
    }

    /**
     *
     * @param pageable
     * @param serieTitle
     * @param serieExternalId
     * @param categories
     * @param status
     * @param origin
     * @param language
     * @param graphicNovelTitle
     * @param graphicNovelExternalId
     * @param publisher
     * @param collection
     * @param isbn
     * @param publicationDateFrom
     * @param publicationDateTo
     * @param lastname
     * @param firstname
     * @param nickname
     * @param authorExternalId
     * @return
     * @throws CollectionItemNotFoundException
     */
    @Override
    public Page<Serie> findBySearchFilters (
            Pageable pageable,
            // Serie filters parameters
            String serieTitle, String serieExternalId, String categories, String status, String origin, String language,
            // Graphic novel filters parameters
            String graphicNovelTitle, String graphicNovelExternalId, String publisher, String collection, String isbn, Date publicationDateFrom, Date publicationDateTo,
            // Author filters parameters
            String lastname, String firstname, String nickname, String authorExternalId ) throws CollectionItemNotFoundException {

        // Check parameters validity
        //--------------------------
        // Force null value for empty parameters
        serieTitle = (serieTitle == "" ? null : serieTitle);
        serieExternalId = (serieExternalId == "" ? null : serieExternalId);
        categories = (categories == "" ? null : categories);
        status = (status == "" ? null : status);
        origin = (origin == "" ? null : origin);
        language = (language == "" ? null : language);
        graphicNovelTitle = (graphicNovelTitle == "" ? null : graphicNovelTitle);
        graphicNovelExternalId = (graphicNovelExternalId == "" ? null : graphicNovelExternalId);
        publisher = (publisher == "" ? null : publisher);
        collection = (collection == "" ? null : collection);
        isbn = (isbn == "" ? null : isbn);
        lastname = (lastname == "" ? null : lastname);
        firstname = (firstname == "" ? null : firstname);
        nickname = (nickname == "" ? null : nickname);
        authorExternalId = (authorExternalId == "" ? null : authorExternalId);

        // Set all optional contains searches to lower case for SQL compliance research
        serieTitle = (serieTitle == null ? null : serieTitle.toLowerCase());
        graphicNovelTitle = (graphicNovelTitle == null ? null : graphicNovelTitle.toLowerCase());
        publisher = (publisher == null ? null : publisher.toLowerCase());
        collection = (collection == null ? null : collection.toLowerCase());
        lastname = (lastname == null ? null : lastname.toLowerCase());
        firstname = (firstname == null ? null : firstname.toLowerCase());
        nickname = (nickname == null ? null : nickname.toLowerCase());

        // Check if selected values from combo box lists exist in DB
        isItemExists(categories, serieRepository.findDistinctCategories(), "Categories", true);
        isItemExists(status, serieRepository.findDistinctStatus(), "Status", true);
        isItemExists(origin, serieRepository.findDistinctOrigins(), "Origin", true);
        isItemExists(language, serieRepository.findDistinctLanguages(), "Languages", true);

        return this.serieRepository
                .findBySearchFilters(
                        pageable,
                        serieTitle, serieExternalId, categories, status, origin, language,
                        graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                        lastname, firstname, nickname, authorExternalId
                );
    }

    /**
     * Retrieve a serie by id
     *
     * @param serieId the id of the serie to get
     * @return the found serie
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    @Override
    public Serie getSerieById(Long serieId) throws EntityNotFoundException {
        Optional<Serie> optionalSerie = serieRepository.findById(serieId);

        if (optionalSerie.isPresent()) {
            return optionalSerie.get();
        } else {
            throw new EntityNotFoundException(serieId, "Serie");
        }
    }

    /**
     * Scrap a serie from https://www.bedetheque.com
     *
     * @param serieUrl the serie url to scrap
     * @return the scraped serie
     * @throws IOException in case of http access error
     */
    //@PostConstruct
    @Override
    public Serie scrapSerie(String serieUrl) throws IOException {
        // Instantiate serie Scraper
        BedethequeSerieScraper bedethequeSerieScraper = new BedethequeSerieScraper();
        // Load serie
        ScrapedSerie scrapSerie = bedethequeSerieScraper.scrap(serieUrl);

        // Get Serie
        Serie serie = serieRepository.findFirstByExternalId(scrapSerie.getExternalId());
        if (serie == null) {
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
        for (ScrapedGraphicNovel sgc : scrapSerie.getScrapedGraphicNovels()) {
            GraphicNovel gc;
            try {
                gc = graphicNovelService.getGraphicNovelByExternalId(sgc.getExternalId());
            } catch (EntityNotFoundException e) {
                gc = graphicNovelService.scrap(serie, sgc);
            }

            // Authors
            for (ScrapedAuthorRole authorRole : sgc.getAuthors()) {
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
     *
     * @throws IOException in case of http access error
     */
    @Override
    public void scrapNewSeriesInDb() throws IOException {
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
            if (serie == null) {
                // Scrap serie
                serie = this.scrapSerie(scrapedSerieUrl.getUrl());
                List<GraphicNovel> graphicNovels = this.graphicNovelService.getGraphicNovels(serie.getId());
                // Save serie
                serie = serieRepository.saveAndFlush(serie);
                System.out.println(" ==> Série ajoutée=" + serie.getTitle() + " (" + graphicNovels.size() + " tomes)");
            } else {
                // TODO : Il faudra gérer la màj des séries/albums/auteurs
                //System.out.println("Author " + authorUrlDTO.getName() + " already exists in db");
            }
        }

    }

    /**
     * Find all series by author's roles
     * @param authorId the author id to get
     * @return the list of author's series
     */
    @Override
    public List<AuthorRolesBySerieResource> getSeriesByAuthorRoles(Long authorId){
        return this.serieRepository.findSeriesByAuthorRoles(authorId);
    }

    /**
     * Retrieve all series starting with the given letter
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of series
     */
    @Override
    public Page<Serie> getSeriesByTitleStartingWith(String letter, Pageable pageable) {
        if(letter.equals("0"))
            return this.serieRepository.findSeriesByTitleLessThanIgnoreCase("a", pageable);
        else
            return this.serieRepository.findSeriesByTitleStartingWithIgnoreCase(letter, pageable);
    }

    /**
     * Count series
     * @return the count
     */
    @Override
    public Long count() {
        return this.serieRepository.count();
    }

    /**
     * Count series by origin
     * @return
     */
    @Override
    public List<SeriesOriginCounterResource> countSeriesByOrigin(){
        return this.serieRepository.countSeriesByOrigin();
    }

    /**
     * Count series by status
     * @return
     */
    @Override
    public List<SeriesStatusCounterResource> countSeriesByStatus() {
        return this.serieRepository.countSeriesByStatus();
    }

    public static boolean isItemExists(String item, List<String> collection, String collectionName, boolean isThrowable) {
        // Check if selected values from combo box lists exist in DB
        boolean result = true;
        if(item != null ) {
            if(! collection.contains(item)) {
                result = false;
            }
        }

        if(isThrowable && result == false) {
            throw new CollectionItemNotFoundException(item, collectionName);
        }

        return result;
    }

}
