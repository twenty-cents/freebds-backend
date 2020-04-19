package com.freebds.backend.controller;

import com.freebds.backend.common.web.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.resources.ContextResource;
import com.freebds.backend.common.web.resources.SeriesOriginCounterResource;
import com.freebds.backend.common.web.resources.SeriesStatusCounterResource;
import com.freebds.backend.dto.*;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.CategoriesMapper;
import com.freebds.backend.mapper.LanguageMapper;
import com.freebds.backend.mapper.SerieMapper;
import com.freebds.backend.mapper.StatusMapper;
import com.freebds.backend.model.Serie;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/series", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class SerieController {

    private final ContextService contextService;
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
     * @param context the context to get
     * @param titleStartingWith the letter
     * @param pageable the page to get
     * @return a page of series
     */
    @GetMapping("/letter")
    Page<SerieDTO> getSeriesByTitleStartingWith(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'context'") @Valid @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @Valid @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId,
            @ApiParam(value = "Query param for 'titleStartingWith'") @Valid @RequestParam(value = "titleStartingWith", required = false) String titleStartingWith
    ){
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        // Force all if no letter specified
        if(titleStartingWith == null)
            titleStartingWith = "";

        // Call the service
        Page<Serie> series = serieService.getSeriesByTitleStartingWith(contextResource, titleStartingWith, pageable);
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
