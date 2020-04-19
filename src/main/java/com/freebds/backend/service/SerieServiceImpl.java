package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.GenericAuthorUrl;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedAuthorRole;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerie;
import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedSerieUrl;
import com.freebds.backend.business.scrapers.bedetheque.serie.BedethequeSerieScraper;
import com.freebds.backend.common.web.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.common.web.resources.SeriesOriginCounterResource;
import com.freebds.backend.common.web.resources.SeriesStatusCounterResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.AuthorRepository;
import com.freebds.backend.repository.LibrarySerieContentRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SerieServiceImpl implements SerieService {

    private final SerieRepository serieRepository;
    private final LibrarySerieContentRepository librarySerieContentRepository;
    private final GraphicNovelService graphicNovelService;
    private final AuthorService authorService;
    private final GraphicNovelAuthorService graphicNovelAuthorService;
    private final AuthorRepository authorRepository;

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
     * @param contextResource the context to get
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of series
     */
    @Override
    public Page<Serie> getSeriesByTitleStartingWith(ContextResource contextResource, String letter, Pageable pageable) {
        if(contextResource.getContext().equals("referential")) {
            if(letter.equals("0"))
                return this.serieRepository.findSeriesByTitleLessThanIgnoreCase("a", pageable);
            else
                return this.serieRepository.findSeriesByTitleStartingWithIgnoreCase(letter, pageable);
        } else {
            if(letter != null)
                letter = letter.toLowerCase();
            if(letter.equals("0"))
                return this.serieRepository.findSeriesFromLibraryByTitleLessThanIgnoreCase(contextResource.getLibrary().getId(), "a", pageable);
            else
                return this.serieRepository.findSeriesFromLibraryByTitleStartingWithIgnoreCase(contextResource.getLibrary().getId(), letter, pageable);
        }

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

}
