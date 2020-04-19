package com.freebds.backend.controller;

import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.dto.GraphicNovelDTO;
import com.freebds.backend.exception.CollectionItemNotFoundException;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.GraphicNovelMapper;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.GraphicNovelService;
import com.freebds.backend.service.SerieService;
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
@RequestMapping(value = "/api/graphic-novels", produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
@Validated
@Slf4j
@CrossOrigin("*")
public class GraphicNovelController {

    private final GraphicNovelService graphicNovelService;
    private final SerieService serieService;
    private final ContextService contextService;

    /**
     * Retrieve all graphic novels from a serie, ordered by tome
     *
     * @param pageable the page to get
     * @param serieId the ID serie to get
     * @param context the context to get (REFERENTIAL/LIBRARY)
     * @param libraryId the library id to get
     * @return a page of graphic novels from a serie
     */
    @GetMapping("")
    public Page<GraphicNovelDTO> getGraphicNovels(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'serie ID'", example = "0") @Valid @RequestParam(value = "serieId", defaultValue = "0") Long serieId,
            @ApiParam(value = "Query param for 'context'") @Valid @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @Valid @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId
    ){
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        // Get the serie (throw an exception if incorrect)
        Serie serie = this.serieService.getSerieById(serieId);

        // Get a page of graphic novels
        return this.graphicNovelService.getGraphicNovels(contextResource, pageable, serie);
    }

    /**
     * Retrieve all graphic novels from a serie, ordered by tome
     *
     * @param pageable the page to get
     * @param serieId the ID serie to get
     * @return a page of graphic novels from a serie
     */
    @GetMapping("/library")
    public Page<GraphicNovelDTO> getGraphicNovelsFromLibraryBySerie(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'serie Id'") @Valid @RequestParam(value = "serieId", defaultValue = "0") Long serieId
    ){
        // Get the associate serie
        Serie serie = this.serieService.getSerieById(serieId);
        // Get a page of graphic novels
        Page<GraphicNovel> graphicNovels = graphicNovelService.getGraphicNovelsFromLibraryBySerie(serieId, pageable);
        // Transform to DTO
        Page<GraphicNovelDTO> graphicNovelDTOs = graphicNovels.map(graphicNovel -> GraphicNovelMapper.INSTANCE.toDTO(graphicNovel));
        // Add author roles and library infos
        return graphicNovelDTOs.map(graphicNovelDTO -> this.graphicNovelService.addGraphicNovelDependencies(graphicNovelDTO));
    }

    /**
     * Get a graphic novel by Id
     *
     * @param id the graphic novel id to get
     * @return a graphic novel
     * @see com.freebds.backend.exception.freebdsApiExceptionHandler#handleEntityNotFoundException(EntityNotFoundException ex) for Exception handling
     */
    @GetMapping("/{id}")
    public GraphicNovelDTO getGraphicNovelById(@PathVariable Long id){
        return GraphicNovelMapper.INSTANCE.toDTO(graphicNovelService.getGraphicNovelById(id));
    }

    /**
     * Get a graphic novel by Id with all authors roles
     *
     * @param id the graphic novel id to get
     * @return a graphic novel
     * @see com.freebds.backend.exception.freebdsApiExceptionHandler#handleEntityNotFoundException(EntityNotFoundException ex) for Exception handling
     */
    @GetMapping("/with-roles/{id}")
    public GraphicNovelDTO getGraphicNovelByIdWithAuthorRoles(@PathVariable Long id){
        return graphicNovelService.getGraphicNovelByIdWithAuthorRoles(id);
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
    @GetMapping("/search")
    public Page<GraphicNovelDTO> getFilteredGraphicNovels (
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
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
            @ApiParam(value = "Query param for 'authorexternalid'") @Valid @RequestParam(value = "authorexternalid", required = false) String authorExternalId
    ) {
        // Call the associated service
        Page<GraphicNovel> graphicNovels = graphicNovelService.findBySearchFilters(
                pageable,
                serieTitle, serieExternalId, categories, status, origin, language,
                graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                lastname, firstname, nickname, authorExternalId
        );

        return graphicNovels.map(graphicNovel -> GraphicNovelMapper.INSTANCE.toDTO(graphicNovel));
    }

    /**
     * Count graphic novels
     * @return the count
     */
    @GetMapping("/count")
    public Long count() {
        return this.graphicNovelService.count();
    }

}
