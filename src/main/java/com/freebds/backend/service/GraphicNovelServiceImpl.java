package com.freebds.backend.service;

import com.freebds.backend.business.scrapers.bedetheque.dto.ScrapedGraphicNovel;
import com.freebds.backend.common.utility.BarcodeImageDecoder;
import com.freebds.backend.common.web.graphicNovel.requests.BarcodeScanRequest;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelMinimumResource;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.mapper.GraphicNovelMapper;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Library;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.GraphicNovelRepository;
import com.freebds.backend.repository.LibraryContentRepository;
import com.freebds.backend.repository.ReviewRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GraphicNovelServiceImpl implements GraphicNovelService {

    private final GraphicNovelRepository graphicNovelRepository;
    private final SerieRepository serieRepository;
    private final LibraryService libraryService;
    private final LibraryContentRepository libraryContentRepository;
    private final ReviewRepository reviewRepository;

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
    public GraphicNovelResource getGraphicNovel(ContextResource contextResource, Long id) throws EntityNotFoundException {
        Optional<GraphicNovel> optionalGraphicNovel = graphicNovelRepository.findById(id);

        if(optionalGraphicNovel.isPresent()){
            return this.addGraphicNovelResourceDependencies(contextResource, optionalGraphicNovel.get());
        } else{
            throw new EntityNotFoundException(id, "Graphic Novel");
        }
    }

    /**
     * Add graphic novel dependencies
     * - Authors roles
     * - Library infos
     * @param contextResource
     * @param graphicNovel
     * @return
     */
    public GraphicNovelResource addGraphicNovelResourceDependencies(ContextResource contextResource, GraphicNovel graphicNovel) {
        // Build resource
        GraphicNovelResource graphicNovelResource = GraphicNovelMapper.INSTANCE.toResource(graphicNovel);

        // Get author roles
        graphicNovelResource.setAuthorRoles(this.graphicNovelRepository.findAuthorRolesById(graphicNovelResource.getId()));
        // Get optional collection infos
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findFirstByLibrary_IdAndGraphicNovel_Id(contextResource.getLibrary().getId(), graphicNovelResource.getId());
        if(optionalLibraryContent.isPresent()){
            graphicNovelResource.setLibraryContent(optionalLibraryContent.get());
            // Add reviews
            graphicNovelResource.setReviews(this.reviewRepository.findReviewsByLibraryContent(
                    optionalLibraryContent.get().getLibrarySerieContent().getId(),
                    optionalLibraryContent.get().getId()));
        }
        return graphicNovelResource;
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
     * @param serieId the serie id to get
     * @return the graphic novels collection
     */
    @Override
    public Page<GraphicNovelResource> getGraphicNovels(ContextResource contextResource, Pageable pageable, Long serieId) throws EntityNotFoundException {
        // Check if the requested serie exists
        Serie serie;
        Optional<Serie> optionalSerie = this.serieRepository.findById(serieId);
        if(optionalSerie.isPresent()) {
            serie = optionalSerie.get();
        } else {
            throw new EntityNotFoundException(serieId, "Serie");
        }

        if(contextResource.getContext().equals("referential")) {
            // Get a page of graphic novels from the requests serie
            Page<GraphicNovel> graphicNovels = this.graphicNovelRepository.findGraphicNovelsBySerieEquals(pageable, serie);
            // Transform to resource and add library infos
            return graphicNovels.map(graphicNovel -> addGraphicNovelResourceDependencies(contextResource, graphicNovel));
        } else {
            // Get a page of graphic novels from the requests serie
            Page<GraphicNovel> graphicNovels = this.graphicNovelRepository.findGraphicNovelsFromLibraryBySerie(contextResource.getLibrary().getId(), serie.getId(), pageable);
            // Transform to resource and add library infos
            return graphicNovels.map(graphicNovel -> addGraphicNovelResourceDependencies(contextResource, graphicNovel));
        }
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

    /**
     * Add a graphic novel in the current user collection
     * @param contextResource the context
     * @param serieId the id serie to get
     * @return the added graphic novel in the current collection
     */
    @Override
    public List<GraphicNovelMinimumResource> getMissingGraphicNovelsFromLibraryBySerie(ContextResource contextResource, Long serieId) {
        return this.graphicNovelRepository.findMissingGraphicNovelsFromLibraryBySerie(contextResource.getLibrary().getId(), serieId);
    }

    /**
     * Scan a barcode image of a graphic novel
     * @param contextResource the context
     * @param barcodeScanRequest the barcode to scan
     * @return a list of associate graphic novels
     */
    @Override
    public List<GraphicNovelResource> scan(ContextResource contextResource, BarcodeScanRequest barcodeScanRequest) throws FreeBdsApiException {

        // Decode base64 image
        byte[] decodedBytes = Base64.getDecoder().decode(barcodeScanRequest.getBarcode());
        String barcodeFormat = "";
        String barcodeValue = "";
        try {
            FileUtils.writeByteArrayToFile(new File("c://home/ean13-webcam.jpg"), decodedBytes);
            InputStream inputstream = new FileInputStream("c://home/ean13-webcam.jpg");
            BarcodeImageDecoder barcodeImageDecoder = new BarcodeImageDecoder();
            // https://stackoverflow.com/questions/19874557/read-barcode-from-an-image-in-java
            // https://stackoverflow.com/questions/10583622/com-google-zxing-notfoundexception-exception-comes-when-core-java-program-execut
            BarcodeImageDecoder.BarcodeInfo barcodeInfo = barcodeImageDecoder.decodeImage(inputstream);
            barcodeFormat = barcodeInfo.getFormat().trim();
            barcodeValue = barcodeInfo.getText().trim();
        } catch ( IOException e1 ) {
            throw new FreeBdsApiException("Erreur I/O", "");
        } catch ( BarcodeImageDecoder.BarcodeDecodingException e2) {
            throw new FreeBdsApiException("Erreur de reconnaissance du CB", "");
        }

        // Code-barre EAN13
        String barcode = "";
        if(barcodeValue.length() == 13) {
            barcode = barcodeValue.substring(0,3) + "-" + barcodeValue.substring(3,4) + "-"
                    + barcodeValue.substring(4,9) + "-" + barcodeValue.substring(9,12) + "-"
                    + barcodeValue.substring(12,13);
            System.out.println(barcode);
        }

        List<GraphicNovel> graphicNovels = this.graphicNovelRepository.findGraphicNovelsByISBN(barcode);
        // Add library infos
        return graphicNovels.stream().map(graphicNovel -> addGraphicNovelResourceDependencies(contextResource, graphicNovel)).collect(Collectors.toList());
    }

}
