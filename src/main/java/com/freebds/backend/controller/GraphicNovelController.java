package com.freebds.backend.controller;

import com.freebds.backend.common.web.graphicNovel.requests.BarcodeScanRequest;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelMinimumResource;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.GraphicNovelService;
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
@RequestMapping(value = "/api/graphic-novels", produces = { MediaType.APPLICATION_JSON_VALUE })
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class GraphicNovelController {

    private final GraphicNovelService graphicNovelService;
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
    public Page<GraphicNovelResource> getGraphicNovels(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "Query param for 'serie ID'", example = "0") @RequestParam(value = "serieId", defaultValue = "0") Long serieId,
            @ApiParam(value = "Query param for 'context'") @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId
    ){
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        // Get a page of graphic novels
        return this.graphicNovelService.getGraphicNovels(contextResource, pageable, serieId);
    }

    /**
     * Add a graphic novel in the current user collection
     * @param context the context to get (REFERENTIAL/LIBRARY)
     * @param libraryId the library id to get
     * @param serieId the id serie to get
     * @return the added graphic novel in the current collection
     */
    @GetMapping("/missing")
    public List<GraphicNovelMinimumResource> getMissingGraphicNovelsFromLibraryBySerie(
        @ApiParam(value = "Query param for 'serie ID'", example = "0") @RequestParam(value = "serieId", defaultValue = "0") Long serieId,
        @ApiParam(value = "Query param for 'context'") @RequestParam(value = "context", defaultValue = "") String context,
        @ApiParam(value = "Query param for 'libraryId'", example = "0") @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId) {

        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        return this.graphicNovelService.getMissingGraphicNovelsFromLibraryBySerie(contextResource, serieId);
    }

    /**
     * Get a graphic novel by Id
     *
     * @param id the graphic novel id to get
     * @return a graphic novel
     * @see com.freebds.backend.exception.freebdsApiExceptionHandler#handleEntityNotFoundException(EntityNotFoundException ex) for Exception handling
     */
    @GetMapping("/{id}")
    public GraphicNovelResource getGraphicNovelById(
            @PathVariable Long id,
            @ApiParam(value = "Query param for 'context'") @RequestParam(value = "context", defaultValue = "") String context,
            @ApiParam(value = "Query param for 'libraryId'", example = "0") @RequestParam(value = "libraryId", defaultValue = "0") Long libraryId) {
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(context, libraryId, "USER");

        return graphicNovelService.getGraphicNovel(contextResource, id);
    }

    /**
     * Get a list of graphic novels from a barcode picture
     * @param barcodeScanRequest the barcode picture to decode
     * @return a list of graphic novels
     */
    @PostMapping("/scan")
    public List<GraphicNovelResource> scan (@RequestBody BarcodeScanRequest barcodeScanRequest) {
        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext("library", barcodeScanRequest.getLibraryId(), "ADMIN");

        return this.graphicNovelService.scan(contextResource, barcodeScanRequest);
    }

}
