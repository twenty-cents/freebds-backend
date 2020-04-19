package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.dto.GraphicNovelDTO;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.GraphicNovelMapper;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Library;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.GraphicNovelRepository;
import com.freebds.backend.repository.LibraryContentRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GraphicNovelServiceImpl implements GraphicNovelService {

    private final GraphicNovelRepository graphicNovelRepository;
    private final SerieRepository serieRepository;
    private final LibraryService libraryService;
    private final LibraryContentRepository libraryContentRepository;

//    public GraphicNovelServiceImpl(GraphicNovelRepository graphicNovelRepository) {
//        this.graphicNovelRepository = graphicNovelRepository;
//    }

    /**
     * Retrieve a graphic novel by id
     *
     * @param id the id of the graphic novel to get
     * @return the graphic novel
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    @Override
    public GraphicNovel getGraphicNovelById(Long id) throws EntityNotFoundException {
        Optional<GraphicNovel> optionalGraphicNovel = graphicNovelRepository.findById(id);

        if(optionalGraphicNovel.isPresent()){
            return optionalGraphicNovel.get();
        } else{
            throw new EntityNotFoundException(id, "Graphic Novel");
        }
    }

    /**
     * Retrieve a graphic novel by id
     *
     * @param id the id of the graphic novel to get
     * @return the graphic novel
     * @throws EntityNotFoundException in case ID is not found in DB.
     */
    @Override
    public GraphicNovelDTO getGraphicNovelByIdWithAuthorRoles(Long id) throws EntityNotFoundException {
        Optional<GraphicNovel> optionalGraphicNovel = graphicNovelRepository.findById(id);

        if(optionalGraphicNovel.isPresent()){
            GraphicNovelDTO graphicNovelDTO = GraphicNovelMapper.INSTANCE.toDTO(optionalGraphicNovel.get());
            // Get author roles
            graphicNovelDTO.setAuthorRoles(this.graphicNovelRepository.findAuthorRolesById(graphicNovelDTO.getId()));
            // Get optional collection infos
            Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findFirstByGraphicNovel_Id(graphicNovelDTO.getId());
            if(optionalLibraryContent.isPresent()){
                graphicNovelDTO.setLibraryContent(optionalLibraryContent.get());
            }
            return graphicNovelDTO;
        } else{
            throw new EntityNotFoundException(id, "Graphic Novel");
        }
    }

    @Override
    public GraphicNovelDTO addGraphicNovelDependencies(GraphicNovelDTO graphicNovelDTO) {
        // Get author roles
        graphicNovelDTO.setAuthorRoles(this.graphicNovelRepository.findAuthorRolesById(graphicNovelDTO.getId()));
        // Get optional collection infos
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findFirstByGraphicNovel_Id(graphicNovelDTO.getId());
        if(optionalLibraryContent.isPresent()){
            graphicNovelDTO.setLibraryContent(optionalLibraryContent.get());
        }
        return graphicNovelDTO;
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
        return this.graphicNovelRepository.findGraphicNovelsBySerieIdOrderByTome(serieId);
    }

    /**
     * Retrieve all graphic novels on a serie
     *
     * @param serie the serie to get
     * @return the graphic novels collection
     */
    @Override
    public Page<GraphicNovel> getGraphicNovels(Pageable pageable, Serie serie) {
        return graphicNovelRepository.findGraphicNovelsBySerieEquals(pageable, serie);
    }

    /**
     * Retrieve all graphic novels on a serie
     * @param contextResource the user context
     * @param pageable the page to get
     * @param serie the serie to get
     * @return the graphic novels collection
     */
    @Override
    public Page<GraphicNovelDTO> getGraphicNovels(ContextResource contextResource, Pageable pageable, Serie serie) {
        // Get a page of graphic novels from the request serie
        Page<GraphicNovel> graphicNovels = this.graphicNovelRepository.findGraphicNovelsBySerieEquals(pageable, serie);
        // Transform to DTO
        Page<GraphicNovelDTO> graphicNovelDTOs = graphicNovels.map(graphicNovel -> GraphicNovelMapper.INSTANCE.toDTO(graphicNovel));
        // Add library infos
        if(contextResource.getContext().equals("LIBRARY")) {
            graphicNovelDTOs.map(graphicNovelDTO -> addGraphicNovelDependencies(graphicNovelDTO));
        }
        return graphicNovelDTOs;
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
            //gc.setPublicationDate(LocalDate.parse("01/" + scrapedGraphicNovel.getPublicationDate(), formatter));
            //gc.setPublicationDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/" + scrapedGraphicNovel.getPublicationDate()));
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse("01/" + scrapedGraphicNovel.getPublicationDate());
            gc.setPublicationDate(new java.sql.Date(date.getTime()));
        } catch (Exception e) {
            gc.setPublicationDate(null);
        }

        try {
            //gc.setReleaseDate(LocalDate.parse(scrapedGraphicNovel.getReleaseDate(), formatter));
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(scrapedGraphicNovel.getReleaseDate());
            gc.setPublicationDate(new java.sql.Date(date.getTime()));
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
    @Override
    public Page<GraphicNovel> findBySearchFilters(
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
        String nationality = "";
        return this.graphicNovelRepository
                .findBySearchFilters(
                        pageable,
                        serieTitle, serieExternalId, categories, status, origin, language,
                        graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                        lastname, firstname, nickname, authorExternalId, nationality
                );

    }

    /**
     * Count graphic novels
     * @return the count
     */
    @Override
    public Long count() {
        return this.graphicNovelRepository.count();
    }

    /**
     * Find all graphic novels from a serie within a library
     * @param serieId the id serie to get
     * @param pageable : the page to get
     * @return a page of graphic novels
     */
    @Override
    public Page<GraphicNovel> getGraphicNovelsFromLibraryBySerie(Long serieId, Pageable pageable) {
        Library library = this.libraryService.getCurrentLibrary();

        return this.graphicNovelRepository.findGraphicNovelsFromLibraryBySerie(library.getId(), serieId, pageable);
    }

    public static boolean isItemExists(String item, List<String> collection, String collectionName, boolean isThrowable) {
        // Check if selected values from combo box lists exist in DB
        boolean result = true;
        if(item != null ) {
            if(! collection.contains(item.toLowerCase())) {
                result = false;
            }
        }

        if(isThrowable && result == false) {
            throw new CollectionItemNotFoundException(item, collectionName);
        }

        return result;
    }

}
