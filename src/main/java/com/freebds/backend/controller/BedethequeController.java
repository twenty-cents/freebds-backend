package com.freebds.backend.controller;

import com.freebds.backend.business.scrapers.bedetheque.filters.GlobalFilteredObject;
import com.freebds.backend.business.scrapers.bedetheque.filters.GraphicNovelsFilteredObject;
import com.freebds.backend.service.BedethequeService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/bedetheque", produces = { MediaType.APPLICATION_JSON_VALUE })
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class BedethequeController {

    private final BedethequeService bedethequeService;

    @GetMapping("/globalFilter")
    public GlobalFilteredObject filter(
            @ApiParam(value = "Query param for 'term'") @RequestParam(value = "term", defaultValue = "") String term) {

        return bedethequeService.filter(term);
    }

    @GetMapping("/graphicNovelsFilter")
    public GraphicNovelsFilteredObject filterGraphicNovels(
            @ApiParam(value = "Query param for 'rechIdSerie'") @RequestParam(value = "rechIdSerie", defaultValue = "") String rechIdSerie,
            @ApiParam(value = "Query param for 'rechIdAuteur'") @RequestParam(value = "rechIdAuteur", defaultValue = "") String rechIdAuteur,
            @ApiParam(value = "Query param for 'rechSerie'") @RequestParam(value = "rechSerie", defaultValue = "") String rechSerie,
            @ApiParam(value = "Query param for 'rechTitre'") @RequestParam(value = "rechTitre", defaultValue = "") String rechTitre,
            @ApiParam(value = "Query param for 'rechEditeur'") @RequestParam(value = "rechEditeur", defaultValue = "") String rechEditeur,
            @ApiParam(value = "Query param for 'rechCollection'") @RequestParam(value = "rechCollection", defaultValue = "") String rechCollection,
            @ApiParam(value = "Query param for 'rechStyle'") @RequestParam(value = "rechStyle", defaultValue = "") String rechStyle,
            @ApiParam(value = "Query param for 'rechAuteur'") @RequestParam(value = "rechAuteur", defaultValue = "") String rechAuteur,
            @ApiParam(value = "Query param for 'rechISBN'") @RequestParam(value = "rechISBN", defaultValue = "") String rechISBN,
            @ApiParam(value = "Query param for 'rechParution'") @RequestParam(value = "rechParution", defaultValue = "") String rechParution,
            @ApiParam(value = "Query param for 'rechOrigine'") @RequestParam(value = "rechOrigine", defaultValue = "") String rechOrigine,
            @ApiParam(value = "Query param for 'rechLangue'") @RequestParam(value = "rechLangue", defaultValue = "") String rechLangue,
            @ApiParam(value = "Query param for 'rechMotCle'") @RequestParam(value = "rechMotCle", defaultValue = "") String rechMotCle,
            @ApiParam(value = "Query param for 'rechDLDeb'") @RequestParam(value = "rechDLDeb", defaultValue = "") String rechDLDeb,
            @ApiParam(value = "Query param for 'rechDLFin'") @RequestParam(value = "rechDLFin", defaultValue = "") String rechDLFin,
            @ApiParam(value = "Query param for 'rechCoteMin'") @RequestParam(value = "rechCoteMin", defaultValue = "") String rechCoteMin,
            @ApiParam(value = "Query param for 'rechCoteMax'") @RequestParam(value = "rechCoteMax", defaultValue = "") String rechCoteMax,
            @ApiParam(value = "Query param for 'rechEO'") @RequestParam(value = "rechEO", defaultValue = "") String rechEO
            ) {

        return bedethequeService.filterGraphicNovels(
                rechIdSerie, rechIdAuteur, rechSerie, rechTitre,
                rechEditeur, rechCollection, rechStyle, rechAuteur,
                rechISBN, rechParution, rechOrigine, rechLangue,
                rechMotCle, rechDLDeb, rechDLFin, rechCoteMin,
                rechCoteMax, rechEO);
    }

    @GetMapping("/autocompleteSeries")
    public String autocompleteSeries(
            @ApiParam(value = "Query param for 'term'") @RequestParam(value = "term", defaultValue = "") String term) {

        return bedethequeService.autocompleteSeries(term);
    }

    @GetMapping("/autocompletePublishers")
    public String autocompletePublishers(
            @ApiParam(value = "Query param for 'term'") @RequestParam(value = "term", defaultValue = "") String term) {

        return bedethequeService.autocompletePublishers(term);
    }

    @GetMapping("/autocompleteCollections")
    public String autocompleteCollections(
            @ApiParam(value = "Query param for 'term'") @RequestParam(value = "term", defaultValue = "") String term) {

        return bedethequeService.autocompleteCollections(term);
    }

    @GetMapping("/autocompleteCategories")
    public String autocompleteCategories(
            @ApiParam(value = "Query param for 'term'") @RequestParam(value = "term", defaultValue = "") String term) {

        return bedethequeService.autocompleteCategories(term);
    }

    @GetMapping("/autocompleteAuthors")
    public String autocompleteAuthors(
            @ApiParam(value = "Query param for 'term'") @RequestParam(value = "term", defaultValue = "") String term) {

        return bedethequeService.autocompleteAuthors(term);
    }


}
