package com.freebds.backend.service;

import com.freebds.backend.common.web.dashboard.resources.GlobalCountsResource;
import com.freebds.backend.common.web.dashboard.resources.PublicationsByOriginResource;
import com.freebds.backend.common.web.dashboard.resources.PublicationsGrowthResource;
import com.freebds.backend.common.web.dashboard.resources.SeriesOriginCounterResource;
import com.freebds.backend.exception.FreeBdsApiException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DashboardService {

    /**
     * Get publications count by origin by month
     * @param pDateFrom the start date
     * @param pDateTo the end date
     * @return the statistics result resource
     */
    PublicationsByOriginResource getPublicationsByMonthGroupByOrigin(String pDateFrom, String pDateTo) throws FreeBdsApiException;

    /**
     * Get publications growth by month
     * @param pDateFrom the start date
     * @param pDateTo the end date
     * @return the statistics result resource
     */
    PublicationsGrowthResource getPublicationsGrowth(String pDateFrom, String pDateTo) throws FreeBdsApiException;

    /**
     * Count series by origin
     * @return the statistics result resource
     */
    List<SeriesOriginCounterResource> countSeriesByOrigin();

    /**
     * Counts existing series, graphic novels, authors in the referential
     * @return the statistics result resource
     */
    GlobalCountsResource getGlobalCounts();
}
