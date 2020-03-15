package com.freebds.backend.controller;

import com.freebds.backend.dto.GraphicNovelDTO;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.mapper.GraphicNovelMapper;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import com.freebds.backend.service.GraphicNovelService;
import com.freebds.backend.service.SerieService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/graphic-novels", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GraphicNovelController {

    private GraphicNovelService graphicNovelService;
    private SerieService serieService;

    public GraphicNovelController(SerieService serieService, GraphicNovelService graphicNovelService) {
        this.serieService = serieService;
        this.graphicNovelService = graphicNovelService;
    }

    /**
     * Retrieve all graphic novels from a serie, ordered by tome
     *
     * @param pageable the page to get
     * @param serieId the ID serie to get
     * @return a page of graphic novels from a serie
     */
    @GetMapping("")
    @PageableAsQueryParam
    public Page<GraphicNovelDTO> getGraphicNovels(
            @PageableDefault(size=25, page = 0, direction = Sort.Direction.ASC) @Parameter(hidden=true) Pageable pageable,
            @ApiParam(value = "Query param for 'serie ID'") @Valid @RequestParam(value = "serieId", defaultValue = "0") Long serieId
    ){
        // Get the associate serie
        Serie serie = this.serieService.getSerieById(serieId);
        // Get a page of graphic novels
        Page<GraphicNovel> graphicNovels = graphicNovelService.getGraphicNovels(pageable, serie);
        return graphicNovels.map(graphicNovel -> GraphicNovelMapper.INSTANCE.toDTO(graphicNovel));
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
}
