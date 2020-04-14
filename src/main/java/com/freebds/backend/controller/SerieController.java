package com.freebds.backend.controller;

import com.freebds.backend.common.web.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.resources.SeriesOriginCounterResource;
import com.freebds.backend.common.web.resources.SeriesStatusCounterResource;
import com.freebds.backend.dto.*;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.CategoriesMapper;
import com.freebds.backend.mapper.LanguageMapper;
import com.freebds.backend.mapper.SerieMapper;
import com.freebds.backend.mapper.StatusMapper;
import com.freebds.backend.model.Serie;
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
import java.util.List;

@RestController
@RequestMapping(value = "/api/series", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class SerieController {

    private final SerieService serieService;

    /**
     * Retrieve all existing series <code>origin types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series origin types, ordered by asc
     */
    @GetMapping("/origins")
    public OriginDTO getDistinctOrigins() {
        OriginDTO originDTO = new OriginDTO();
        originDTO.setOrigins(this.serieService.getDistinctOrigins());
        return originDTO;
    }

    /**
     * Retrieve all existing series <code>status types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series status types, ordered by asc
     */
    @GetMapping("/status")
    public StatusDTO getDistinctStatus() {
        return StatusMapper.toDTO(this.serieService.getDistinctStatus());
    }

    /**
     * Retrieve all existing series <code>category types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series category types, ordered by asc
     */
    @GetMapping("/categories")
    public CategoriesDTO getDistinctCategories() {
        return CategoriesMapper.toDTO(this.serieService.getDistinctCategories());
    }

    /**
     * Retrieve all existing series <code>languages</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series languages, ordered by asc
     */
    @GetMapping("/languages")
    public LanguageDTO getDistinctLanguages() {
        return LanguageMapper.toDTO(this.serieService.getDistinctLanguages());
    }

    /**
     * Retrieve all series, filtered by title, origin, status and category
     *
     * @param pageable the page to get
     * @param title the title to get
     * @param origin the origin to get
     * @param status the status to get
     * @param category the category to get
     * @return a page of filtered series
     */
    @GetMapping("/filter")
    public Page<SerieDTO> getFilteredSeries(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'title'") @Valid @RequestParam(value = "title", defaultValue = "") String title,
            @ApiParam(value = "Query param for 'origin'") @Valid @RequestParam(value = "origin", defaultValue = "") String origin,
            @ApiParam(value = "Query param for 'status'") @Valid @RequestParam(value = "status", defaultValue = "") String status,
            @ApiParam(value = "Query param for 'category'") @Valid @RequestParam(value = "category", defaultValue = "") String category
    ){
        Page<Serie> series = serieService.getFilteredSeries(pageable, title, origin, status, category);
        // Convert authors in serieDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return series.map(serie -> SerieMapper.INSTANCE.toDTO(serie));
    }

    /**
     *
     * @param pageable
     * @param serieTitle
     * @param serieExternalId
     * @param origin
     * @param status
     * @param categories
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
     */
    @GetMapping("/search")
    public Page<SerieDTO> getFilteredSeries2(
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
        Page<Serie> series = serieService.findBySearchFilters(
                pageable,
                serieTitle, serieExternalId, categories, status, origin, language,
                graphicNovelTitle, graphicNovelExternalId, publisher, collection, isbn, publicationDateFrom, publicationDateTo,
                lastname, firstname, nickname, authorExternalId
        );
        // Convert authors in serieDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return series.map(serie -> SerieMapper.INSTANCE.toDTO(serie));
    }

    /**
     * Get a serie by Id
     *
     * @param id the serie id to get
     * @return a serie
     * @see com.freebds.backend.exception.freebdsApiExceptionHandler#handleEntityNotFoundException(EntityNotFoundException ex) for Exception handling
     */
    @GetMapping("/{id}")
    public SerieDTO getSerieById(@PathVariable Long id){
        return SerieMapper.INSTANCE.toDTO(serieService.getSerieById(id));
    }

    /**
     * Find all series by author's roles
     * @param id the author id to get
     * @return the list of author's series
     */
    @GetMapping("/authors/{id}")
    public List<AuthorRolesBySerieResource> getMainSeriesByAuthor(@PathVariable Long id){
        return this.serieService.getSeriesByAuthorRoles(id);
    }

    /**
     * Retrieve all series starting with the given letter
     * @param letter the letter
     * @param pageable the page to get
     * @return a page of series
     */
    @GetMapping("/letters/{letter}")
    Page<SerieDTO> getSeriesByTitleStartingWith(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable String letter
    ){
        Page<Serie> series = serieService.getSeriesByTitleStartingWith(letter, pageable);
        return series.map(serie -> SerieMapper.INSTANCE.toDTO(serie));
    }

    /**
     * Count series
     * @return the count
     */
    @GetMapping("/count")
    public Long count() {
        return this.serieService.count();
    }

    /**
     * Count series by origin
     * @return the counts
     */
    @GetMapping("/counts/origin")
    public List<SeriesOriginCounterResource> countByOrigin() {
        return this.serieService.countSeriesByOrigin();
    }

    /**
     * Count series by status
     * @return the counts
     */
    @GetMapping("/counts/status")
    public List<SeriesStatusCounterResource> countByStatus() {
        return this.serieService.countSeriesByStatus();
    }

}
