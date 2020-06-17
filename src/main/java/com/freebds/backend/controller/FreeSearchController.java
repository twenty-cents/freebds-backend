package com.freebds.backend.controller;

import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.common.web.freeSearch.resources.FreeSearchResource;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.FreeSearchService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@RestController
@RequestMapping(value = "/api/search", produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class FreeSearchController {

    private final ContextService contextService;
    private final FreeSearchService freeSearchService;

    /**
     * Retrieve all existing series / graphic novels / authors by multiple criteria
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
     * @return a page of filtered series / graphic novels / authors
     */
    @GetMapping("")
    public FreeSearchResource search(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'context'") @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId,
            @ApiParam(value = "Query param for 'type'") @RequestParam(value = "type", defaultValue = "") String type,
            @ApiParam(value = "Query param for 'serietitle'") @RequestParam(value = "serietitle", required = false) String serieTitle,
            @ApiParam(value = "Query param for 'serieexternalId'") @RequestParam(value = "serieexternalId", required = false) String serieExternalId,
            @ApiParam(value = "Query param for 'origin'") @RequestParam(value = "origin", required = false) String origin,
            @ApiParam(value = "Query param for 'status'") @RequestParam(value = "status", required = false) String status,
            @ApiParam(value = "Query param for 'categories'") @RequestParam(value = "categories", required = false) String categories,
            @ApiParam(value = "Query param for 'language'") @RequestParam(value = "language", required = false) String language,
            @ApiParam(value = "Query param for 'graphicnoveltitle'") @RequestParam(value = "graphicnoveltitle", required = false) String graphicNovelTitle,
            @ApiParam(value = "Query param for 'graphicnovelexternalid'") @RequestParam(value = "graphicnovelexternalid", required = false) String graphicNovelExternalId,
            @ApiParam(value = "Query param for 'publisher'") @RequestParam(value = "publisher", required = false) String publisher,
            @ApiParam(value = "Query param for 'collection'") @RequestParam(value = "collection", required = false) String collection,
            @ApiParam(value = "Query param for 'isbn'") @RequestParam(value = "isbn", required = false) String isbn,
            @ApiParam(value = "Query param for 'publicationdatefrom'") @RequestParam(value = "publicationdatefrom", required = false, defaultValue = "0001-01-01") Date publicationDateFrom,
            @ApiParam(value = "Query param for 'publicationdateto'") @RequestParam(value = "publicationdateto", required = false, defaultValue = "9999-01-01") Date publicationDateTo,
            @ApiParam(value = "Query param for 'lastname'") @RequestParam(value = "lastname", required = false) String lastname,
            @ApiParam(value = "Query param for 'firstname'") @RequestParam(value = "firstname", required = false) String firstname,
            @ApiParam(value = "Query param for 'nickname'") @RequestParam(value = "nickname", required = false) String nickname,
            @ApiParam(value = "Query param for 'authorexternalid'") @RequestParam(value = "authorexternalid", required = false) String authorExternalId,
            @ApiParam(value = "Query param for 'authorNationality'") @RequestParam(value = "authorNationality", required = false) String authorNationality
    ) {
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        return this.freeSearchService.search(
                pageable, contextResource, type, libraryId,
                serieTitle, serieExternalId, categories, status, origin, language,
                graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                lastname, firstname, nickname, authorExternalId, authorNationality
        );
    }

}
