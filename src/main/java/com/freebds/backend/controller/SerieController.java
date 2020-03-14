package com.freebds.backend.controller;

import com.freebds.backend.dto.SerieDTO;
import com.freebds.backend.mapper.SerieMapper;
import com.freebds.backend.model.Serie;
import com.freebds.backend.service.SerieServiceImpl;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/series", produces = { MediaType.APPLICATION_JSON_VALUE })
public class SerieController {

    private SerieServiceImpl serieService;

    public SerieController(SerieServiceImpl serieService) {
        this.serieService = serieService;
    }

    /**
     * Retrieve all existing series <code>origin types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series origin types, ordered by asc
     */
    @GetMapping("/origins")
    public List<String> getDistinctOrigins() {
        return this.serieService.getDistinctOrigins();
    }

    /**
     * Retrieve all existing series <code>status types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series status types, ordered by asc
     */
    @GetMapping("/status")
    public List<String> getDistinctStatus() {
        return this.serieService.getDistinctStatus();
    }

    /**
     * Retrieve all existing series <code>category types</code> defined by http://wwww.bedetehque.com
     *
     * @return the list of distinct series category types, ordered by asc
     */
    @GetMapping("/categories")
    public List<String> getDistinctCategories() {
        return this.serieService.getDistinctCategories();
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
    @PageableAsQueryParam
    public Page<SerieDTO> getFilteredSeries(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) @Parameter(hidden=true) Pageable pageable,
            @ApiParam(value = "Query param for 'title'") @Valid @RequestParam(value = "title", defaultValue = "") String title,
            @ApiParam(value = "Query param for 'origin'") @Valid @RequestParam(value = "origin", defaultValue = "") String origin,
            @ApiParam(value = "Query param for 'status'") @Valid @RequestParam(value = "status", defaultValue = "") String status,
            @ApiParam(value = "Query param for 'category'") @Valid @RequestParam(value = "category", defaultValue = "") String category
    ){
        Page<Serie> series = serieService.getFilteredSeries(pageable, title, origin, status, category);
        // Convert authors in serieDTO and return the result to the front
        // google : spring how to convert pageable to dto
        // https://www.programcreek.com/java-api-examples/?class=org.springframework.data.domain.Page&method=map
        return series.map(serie -> SerieMapper.INSTANCE.toDto(serie));
    }

}
