package com.freebds.backend.controller;

import com.freebds.backend.common.web.serie.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.common.web.dashboard.resources.SeriesStatusCounterResource;
import com.freebds.backend.common.web.serie.resources.*;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.SerieService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/series", produces = { MediaType.APPLICATION_JSON_VALUE })
@Slf4j
@RequiredArgsConstructor
//@CrossOrigin("*")
public class SerieController {

    private final ContextService contextService;
    private final SerieService serieService;

    /**
     * Retrieve all existing series <code>origin types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series origin types, ordered by asc
     */
    @GetMapping("/origins")
    public OriginsResource getDistinctOrigins() {
        return this.serieService.getDistinctOrigins();
    }

    /**
     * Retrieve all existing series <code>status types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series status types, ordered by asc
     */
    @GetMapping("/status")
    public StatusResource getDistinctStatus() {
        return this.serieService.getDistinctStatus();
    }

    /**
     * Retrieve all existing series <code>category types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series category types, ordered by asc
     */
    @GetMapping("/categories")
    public CategoriesResource getDistinctCategories() {
        return this.serieService.getDistinctCategories();
    }

    /**
     * Retrieve all existing series <code>languages</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series languages, ordered by asc
     */
    @GetMapping("/languages")
    public LanguageResource getDistinctLanguages() {
        return this.serieService.getDistinctLanguages();
    }

    /**
     * Get a serie by Id
     *
     * @param id the serie id to get
     * @return a serie
     * @see com.freebds.backend.exception.freebdsApiExceptionHandler#handleEntityNotFoundException(EntityNotFoundException ex) for Exception handling
     */
    @GetMapping("/{id}")
    public SerieResource getSerieById(
            @PathVariable Long id,
            @ApiParam(value = "Query param for 'context'") @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId
    ){
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        return this.serieService.getSerie(contextResource, id);
    }

    /**
     * Find all series by author's roles
     * @param id the author id to get
     * @return the list of author's series
     */
    @GetMapping("/authors/{id}")
    public List<AuthorRolesBySerieResource> getMainSeriesByAuthor(
            @PathVariable Long id,
            @ApiParam(value = "Query param for 'context'") @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId
    ){
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        return this.serieService.getSeriesByAuthorRoles(contextResource, id);
    }

    /**
     * Retrieve all series starting with the given letter
     * @param context the context to get
     * @param titleStartingWith the letter
     * @param pageable the page to get
     * @return a page of series
     */
    @GetMapping("/letter")
    Page<SerieResource> getSeriesByTitleStartingWith(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'context'") @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId,
            @ApiParam(value = "Query param for 'titleStartingWith'") @RequestParam(value = "titleStartingWith", required = false) String titleStartingWith
    ){
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        // Force all if no letter specified
        if(titleStartingWith == null)
            titleStartingWith = "";

        // Call the service
        return serieService.getSeriesByTitleStartingWith(contextResource, titleStartingWith, pageable);
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
