package com.freebds.backend.controller;

import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.dto.AuthorDTO;
import com.freebds.backend.dto.GraphicNovelDTO;
import com.freebds.backend.dto.SerieDTO;
import com.freebds.backend.mapper.AuthorMapper;
import com.freebds.backend.mapper.GraphicNovelMapper;
import com.freebds.backend.mapper.SerieMapper;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.FreeSearchService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;

@RestController
@RequestMapping(value = "/api/search", produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
@Validated
@Slf4j
@CrossOrigin
public class FreeSearchController {

    private final ContextService contextService;
    private final FreeSearchService freeSearchService;

    /**
     * Retrieve all existing series by multiple criteria
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
     * @param authorNationality the author nationality to get
     * @return a page of filtered series
     */
    @GetMapping("/series")
    public Page<SerieDTO> searchSeries(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'context'") @Valid @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @Valid @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId,
            @ApiParam(value = "Query param for 'serietitle'") @Valid @RequestParam(value = "serietitle", required = false) String serieTitle,
            @ApiParam(value = "Query param for 'serieexternalId'") @Valid @RequestParam(value = "serieexternalId", required = false) String serieExternalId,
            @ApiParam(value = "Query param for 'origin'") @Valid @RequestParam(value = "origin", required = false) String origin,
            @ApiParam(value = "Query param for 'status'") @Valid @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Query param for 'categories'") @Valid @RequestParam(value = "categories", required = false) String categories,
            @ApiParam(value = "Query param for 'language'") @Valid @RequestParam(value = "language", required = false) String language,
            @ApiParam(value = "Query param for 'graphicnoveltitle'") @Valid @RequestParam(value = "graphicnoveltitle", required = false) String graphicNovelTitle,
            @ApiParam(value = "Query param for 'graphicnovelexternalid'") @Valid @RequestParam(value = "graphicnovelexternalid", required = false) String graphicNovelExternalId,
            @ApiParam(value = "Query param for 'publisher'") @Valid @RequestParam(value = "publisher", required = false) String publisher,
            @ApiParam(value = "Query param for 'collection'") @Valid @RequestParam(value = "collection", required = false) String collection,
            @ApiParam(value = "Query param for 'isbn'") @Valid @RequestParam(value = "isbn", required = false) String isbn,
            @ApiParam(value = "Query param for 'publicationdatefrom'") @Valid @RequestParam(value = "publicationdatefrom", required = false, defaultValue = "0001-01-01") Date publicationDateFrom,
            @ApiParam(value = "Query param for 'publicationdateto'") @Valid @RequestParam(value = "publicationdateto", required = false, defaultValue = "9999-01-01") Date publicationDateTo,
            @ApiParam(value = "Query param for 'lastname'") @Valid @RequestParam(value = "lastname", required = false) String lastname,
            @ApiParam(value = "Query param for 'firstname'") @Valid @RequestParam(value = "firstname", required = false) String firstname,
            @ApiParam(value = "Query param for 'nickname'") @Valid @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam(value = "Query param for 'authorexternalid'") @Valid @RequestParam(value = "authorexternalid", required = false) String authorExternalId,
            @ApiParam(value = "Query param for 'authorNationality'") @Valid @RequestParam(value = "authorNationality", required = false) String authorNationality
    ) {
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        // Call the associated service
        Page<Serie> series = this.freeSearchService.searchSeries(
                pageable, contextResource, libraryId,
                serieTitle, serieExternalId, categories, status, origin, language,
                graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                lastname, firstname, nickname, authorExternalId, authorNationality
        );
        // Convert authors in serieDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return series.map(serie -> SerieMapper.INSTANCE.toDTO(serie));
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
     * @param authorNationality the author nationality to get
     * @return a page of filtered graphic novels
     */
    @GetMapping("/graphic-novels")
    public Page<GraphicNovelDTO> searchGraphicNovels (
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'context'") @Valid @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @Valid @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId,
            @ApiParam(value = "Query param for 'serietitle'") @Valid @RequestParam(value = "serietitle", required = false) String serieTitle,
            @ApiParam(value = "Query param for 'serieexternalId'") @Valid @RequestParam(value = "serieexternalId", required = false) String serieExternalId,
            @ApiParam(value = "Query param for 'origin'") @Valid @RequestParam(value = "origin", required = false) String origin,
            @ApiParam(value = "Query param for 'status'") @Valid @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Query param for 'categories'") @Valid @RequestParam(value = "categories", required = false) String categories,
            @ApiParam(value = "Query param for 'language'") @Valid @RequestParam(value = "language", required = false) String language,
            @ApiParam(value = "Query param for 'graphicnoveltitle'") @Valid @RequestParam(value = "graphicnoveltitle", required = false) String graphicNovelTitle,
            @ApiParam(value = "Query param for 'graphicnovelexternalid'") @Valid @RequestParam(value = "graphicnovelexternalid", required = false) String graphicNovelExternalId,
            @ApiParam(value = "Query param for 'publisher'") @Valid @RequestParam(value = "publisher", required = false) String publisher,
            @ApiParam(value = "Query param for 'collection'") @Valid @RequestParam(value = "collection", required = false) String collection,
            @ApiParam(value = "Query param for 'isbn'") @Valid @RequestParam(value = "isbn", required = false) String isbn,
            @ApiParam(value = "Query param for 'publicationdatefrom'") @Valid @RequestParam(value = "publicationdatefrom", required = false, defaultValue = "0001-01-01") Date publicationDateFrom,
            @ApiParam(value = "Query param for 'publicationdateto'") @Valid @RequestParam(value = "publicationdateto", required = false, defaultValue = "9999-01-01") Date publicationDateTo,
            @ApiParam(value = "Query param for 'lastname'") @Valid @RequestParam(value = "lastname", required = false) String lastname,
            @ApiParam(value = "Query param for 'firstname'") @Valid @RequestParam(value = "firstname", required = false) String firstname,
            @ApiParam(value = "Query param for 'nickname'") @Valid @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam(value = "Query param for 'authorexternalid'") @Valid @RequestParam(value = "authorexternalid", required = false) String authorExternalId,
            @ApiParam(value = "Query param for 'authorNationality'") @Valid @RequestParam(value = "authorNationality", required = false) String authorNationality
    ) {
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        // Call the associated service
        Page<GraphicNovel> graphicNovels = this.freeSearchService.searchGraphicNovels(
                pageable, contextResource, libraryId,
                serieTitle, serieExternalId, categories, status, origin, language,
                graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                lastname, firstname, nickname, authorExternalId, authorNationality
        );
        // Convert authors in serieDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return graphicNovels.map(graphicNovel -> GraphicNovelMapper.INSTANCE.toDTO(graphicNovel));
    }

    /**
     * Retrieve all existing authors by multiple criteria
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
     * @param authorNationality the author nationality to get
     * @return a page of filtered authors
     */
    @GetMapping("/authors")
    public Page<AuthorDTO> searchAuthors(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'context'") @Valid @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @Valid @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId,
            @ApiParam(value = "Query param for 'serietitle'") @Valid @RequestParam(value = "serietitle", required = false) String serieTitle,
            @ApiParam(value = "Query param for 'serieexternalId'") @Valid @RequestParam(value = "serieexternalId", required = false) String serieExternalId,
            @ApiParam(value = "Query param for 'origin'") @Valid @RequestParam(value = "origin", required = false) String origin,
            @ApiParam(value = "Query param for 'status'") @Valid @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Query param for 'categories'") @Valid @RequestParam(value = "categories", required = false) String categories,
            @ApiParam(value = "Query param for 'language'") @Valid @RequestParam(value = "language", required = false) String language,
            @ApiParam(value = "Query param for 'graphicnoveltitle'") @Valid @RequestParam(value = "graphicnoveltitle", required = false) String graphicNovelTitle,
            @ApiParam(value = "Query param for 'graphicnovelexternalid'") @Valid @RequestParam(value = "graphicnovelexternalid", required = false) String graphicNovelExternalId,
            @ApiParam(value = "Query param for 'publisher'") @Valid @RequestParam(value = "publisher", required = false) String publisher,
            @ApiParam(value = "Query param for 'collection'") @Valid @RequestParam(value = "collection", required = false) String collection,
            @ApiParam(value = "Query param for 'isbn'") @Valid @RequestParam(value = "isbn", required = false) String isbn,
            @ApiParam(value = "Query param for 'publicationdatefrom'") @Valid @RequestParam(value = "publicationdatefrom", required = false, defaultValue = "0001-01-01") Date publicationDateFrom,
            @ApiParam(value = "Query param for 'publicationdateto'") @Valid @RequestParam(value = "publicationdateto", required = false, defaultValue = "9999-01-01") Date publicationDateTo,
            @ApiParam(value = "Query param for 'lastname'") @Valid @RequestParam(value = "lastname", required = false) String lastname,
            @ApiParam(value = "Query param for 'firstname'") @Valid @RequestParam(value = "firstname", required = false) String firstname,
            @ApiParam(value = "Query param for 'nickname'") @Valid @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam(value = "Query param for 'authorexternalid'") @Valid @RequestParam(value = "authorexternalid", required = false) String authorExternalId,
            @ApiParam(value = "Query param for 'authorNationality'") @Valid @RequestParam(value = "authorNationality", required = false) String authorNationality
    ) {
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        // Call the associated service
        Page<Author> authors = this.freeSearchService.searchAuthors(
                pageable, contextResource, libraryId,
                serieTitle, serieExternalId, categories, status, origin, language,
                graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                lastname, firstname, nickname, authorExternalId, authorNationality
        );
        return authors.map(author -> AuthorMapper.INSTANCE.toDTO(author));
    }

}
