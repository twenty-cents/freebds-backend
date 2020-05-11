package com.freebds.backend.controller;

import com.freebds.backend.common.web.serie.resources.SerieResource;
import com.freebds.backend.service.LibrarySerieContentService;
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

@RestController
@RequestMapping(value = "/api/library-series", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class LibrarySerieController {

    private final LibrarySerieContentService librarySerieContentService;

    /**
     * Find series within a library
     * @param titleStartingWith : the title to get
     * @param pageable : the page to get
     * @return a page of series
     */
    @GetMapping("/letters")
    public Page<SerieResource> getAllSeries(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'titleStartingWith'") @Valid @RequestParam(value = "titleStartingWith", required = false) String titleStartingWith
    ){
        if(titleStartingWith == null)
            titleStartingWith = "";

        return librarySerieContentService.getAllSeries(titleStartingWith, pageable);
    }


}
