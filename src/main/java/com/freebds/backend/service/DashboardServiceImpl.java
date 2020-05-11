package com.freebds.backend.service;

import com.freebds.backend.common.utility.PeriodValidator;
import com.freebds.backend.common.web.dashboard.resources.*;
import com.freebds.backend.common.web.dashboard.resources.SeriesOriginCounterResource;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.repository.AuthorRepository;
import com.freebds.backend.repository.GraphicNovelRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService {

    private final SerieRepository serieRepository;
    private final GraphicNovelRepository graphicNovelRepository;
    private final AuthorRepository authorRepository;

    /**
     * Get publications count by origin by month
     * @param pDateFrom the start date
     * @param pDateTo the end date
     * @return the statistics result resource
     */
    @Override
    public PublicationsByOriginResource getPublicationsByMonthGroupByOrigin(String pDateFrom, String pDateTo) throws FreeBdsApiException {

        // Set default date values if no date received
        if(pDateFrom == null && pDateTo == null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //Java calendar in default timezone and default locale
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            cal.add(Calendar.MONTH, -1);
            pDateTo = df.format(new Date(cal.getTime().getTime()));
            cal.add(Calendar.MONTH, -12);
            pDateFrom = df.format(new Date(cal.getTime().getTime()));
        }

        // Check period validity (throws FreeBdsApiException)
        PeriodValidator periodValidator = new PeriodValidator();
        periodValidator.checkValidity(pDateFrom, pDateTo);

        // Get the statistics
        Date dateFrom = Date.valueOf(pDateFrom);
        Date dateTo = Date.valueOf(pDateTo);
        List<PublicationsByMonthGroupByOriginResource> publicationsByMonthGroupByOriginResources
                = this.graphicNovelRepository.findPublicationsByMonthGroupByOrigin(dateFrom, dateTo);

        // Build the result resource
        PublicationsByOriginResource publicationsByOriginResource = new PublicationsByOriginResource();

        List<String> periods = new ArrayList<>();
        List<Long> europa = new ArrayList<>();
        List<Long> usa = new ArrayList<>();
        List<Long> asia = new ArrayList<>();
        List<Long> other = new ArrayList<>();
        boolean hasEuropa = false;
        boolean hasUsa = false;
        boolean hasAsia = false;
        boolean hasOther = false;
        String currentMonth = "";
        for(PublicationsByMonthGroupByOriginResource p : publicationsByMonthGroupByOriginResources) {
            if(currentMonth.equals("")) {
                periods.add(p.getMonth());
                currentMonth = p.getMonth();
            }

            if(! p.getMonth().equals(currentMonth)) {
                currentMonth = p.getMonth();
                // Set count = 0 for unfounded origins
                if(hasEuropa == false) {
                    europa.add(0L);
                }
                if(hasUsa == false) {
                    usa.add(0L);
                }
                if(hasAsia == false) {
                    asia.add(0L);
                }
                if(hasOther == false) {
                    other.add(0L);
                }

                // Change period
                periods.add(p.getMonth());
                hasEuropa = false;
                hasUsa = false;
                hasAsia = false;
                hasOther = false;
            }

            if(p.getOrigin().equals("Europe")){
                europa.add(p.getCount());
                hasEuropa = true;
            }
            if(p.getOrigin().equals("USA")){
                usa.add(p.getCount());
                hasUsa = true;
            }
            if(p.getOrigin().equals("Asie")){
                asia.add(p.getCount());
                hasAsia = true;
            }
            if(p.getOrigin().equals("Autre")){
                other.add(p.getCount());
                hasOther = true;
            }
        }

        // Set count = 0 for last month  unfounded origins
        if(hasEuropa == false) {
            europa.add(0L);
        }
        if(hasUsa == false) {
            usa.add(0L);
        }
        if(hasAsia == false) {
            asia.add(0L);
        }
        if(hasOther == false) {
            other.add(0L);
        }

        publicationsByOriginResource.setPeriods(periods);
        publicationsByOriginResource.setEuropa(europa);
        publicationsByOriginResource.setUsa(usa);
        publicationsByOriginResource.setAsia(asia);
        publicationsByOriginResource.setOther(other);

        return publicationsByOriginResource;
    }

    /**
     * Get publications growth by month
     * @param pDateFrom the start date
     * @param pDateTo the end date
     * @return the statistics result resource
     */
    @Override
    public PublicationsGrowthResource getPublicationsGrowth(String pDateFrom, String pDateTo) throws FreeBdsApiException {

        // Set default date values if no date received
        if(pDateFrom == null && pDateTo == null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            //Java calendar in default timezone and default locale
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            cal.add(Calendar.MONTH, -1);
            pDateTo = df.format(new Date(cal.getTime().getTime()));
            cal.add(Calendar.MONTH, -12);
            pDateFrom = df.format(new Date(cal.getTime().getTime()));
        }

        // Check period validity (throws FreeBdsApiException)
        PeriodValidator periodValidator = new PeriodValidator();
        periodValidator.checkValidity(pDateFrom, pDateTo);

        // Get the statistics
        Date dateFrom = Date.valueOf(pDateFrom);
        Date dateTo = Date.valueOf(pDateTo);
        List<PeriodicityCountResource> periodicityCountResources = this.graphicNovelRepository.countPublicationsByMonth(dateFrom, dateTo);

        // Build the result resource
        PublicationsGrowthResource publicationsGrowthResource = new PublicationsGrowthResource();
        long lastCount = 0;

        List<String> periods = new ArrayList<>();
        List<Long> growth = new ArrayList<>();
        for(PeriodicityCountResource p : periodicityCountResources) {
            // Get growth from start to the last 13 months
            if(p.getPeriodicity().equals("0001-01")) {
                lastCount = p.getCount();
            } else {
                periods.add(p.getPeriodicity());
                lastCount += p.getCount();
                growth.add(lastCount);
            }
        }
        publicationsGrowthResource.setPeriods(periods);
        publicationsGrowthResource.setGraphicNovels(growth);

        return publicationsGrowthResource;
    }

    /**
     * Count series by origin
     * @return
     */
    @Override
    public List<SeriesOriginCounterResource> countSeriesByOrigin(){
        return this.serieRepository.countSeriesByOrigin();
    }

    /**
     * Counts existing series, graphic novels, authors in the referential
     * @return the statistics result resource
     */
    @Override
    public GlobalCountsResource getGlobalCounts() {
        GlobalCountsResource globalCountsResource = new GlobalCountsResource();
        // Get the statistics
        globalCountsResource.setReferentialSeriesCount(this.serieRepository.count());
        globalCountsResource.setReferentialGraphicNovelsCount(this.graphicNovelRepository.count());
        globalCountsResource.setReferentialAuthorsCount(this.authorRepository.count());

        return globalCountsResource;
    }

}
