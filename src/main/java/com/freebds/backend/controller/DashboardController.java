package com.freebds.backend.controller;

import com.freebds.backend.common.web.dashboard.resources.GlobalCountsResource;
import com.freebds.backend.common.web.dashboard.resources.PublicationsByOriginResource;
import com.freebds.backend.common.web.dashboard.resources.PublicationsGrowthResource;
import com.freebds.backend.common.web.dashboard.resources.SeriesOriginCounterResource;
import com.freebds.backend.service.*;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dashboard", produces = { MediaType.APPLICATION_JSON_VALUE })
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final ContextService contextService;
    private final GraphicNovelService graphicNovelService;
    private final SerieService serieService;
    private final AuthorService authorService;

    /**
     * Get publications count by origin by month
     * @param pDateFrom the start date
     * @param pDateTo the end date
     * @return the statistics result resource
     */
    @GetMapping("/pub-by-month")
    public PublicationsByOriginResource getPublicationsByMonthGroupByOrigin(
            @ApiParam(value = "Query param for 'pDateFrom'", example = "yyyy-MM-dd") @RequestParam(value = "pDateFrom", required = false) String pDateFrom,
            @ApiParam(value = "Query param for 'pDateTo'", example = "yyyy-MM-dd") @RequestParam(value = "pDateTo", required = false) String pDateTo) {

        return this.dashboardService.getPublicationsByMonthGroupByOrigin(pDateFrom, pDateTo);
    }

    /**
     * Get publications growth by month
     * @param pDateFrom the start date
     * @param pDateTo the end date
     * @return the statistics result resource
     */
    @GetMapping("/pub-growth")
    public PublicationsGrowthResource getPublicationsGrowth(
            @ApiParam(value = "Query param for 'pDateFrom'", example = "yyyy-MM-dd") @RequestParam(value = "pDateFrom", required = false) String pDateFrom,
            @ApiParam(value = "Query param for 'pDateTo'", example = "yyyy-MM-dd") @RequestParam(value = "pDateTo", required = false) String pDateTo) {

        return this.dashboardService.getPublicationsGrowth(pDateFrom, pDateTo);
    }

    /**
     * Count series by origin
     * @return the counts
     */
    @GetMapping("/counts/origin")
    public List<SeriesOriginCounterResource> countByOrigin() {
        return this.dashboardService.countSeriesByOrigin();
    }

    /**
     * Counts existing series, graphic novels, authors in the referential
     * @return the statistics result resource
     */
    @GetMapping("/counts")
    public GlobalCountsResource getGlobalCounts() {
        return this.dashboardService.getGlobalCounts();
    }
}
