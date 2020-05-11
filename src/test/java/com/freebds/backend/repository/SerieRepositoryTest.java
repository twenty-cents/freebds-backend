package com.freebds.backend.repository;

import com.freebds.backend.common.web.dashboard.resources.SeriesOriginCounterResource;
import com.freebds.backend.common.web.dashboard.resources.SeriesStatusCounterResource;
import com.freebds.backend.common.web.serie.resources.AuthorRolesBySerieResource;
import com.freebds.backend.model.Serie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class SerieRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SerieRepository serieRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(serieRepository).isNotNull();
    }

    @Test
    void findBySearchFilters() throws ParseException {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("0001-01-01");
        java.sql.Date publicationDateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("9999-01-01");
        java.sql.Date publicationDateTo = new java.sql.Date(parsed.getTime());

        Page<Serie> series = serieRepository.findBySearchFilters(
                expectedPage.getPageable(), null, null, null, null, null, null,
                null, null, null, null, null, publicationDateFrom, publicationDateTo,
                null, null, null, "148", null);

        assertThat(series.getContent().size()).isGreaterThan(1);
    }

    @Test
    void findSeriesByAuthorRoles() {
        List<AuthorRolesBySerieResource> series = this.serieRepository.findSeriesByAuthorRoles(13276L);
        assertThat(series.size()).isGreaterThan(0);

//        System.out.println(series.size());
//        for(AuthorRolesBySerieResource author : series) {
//            System.out.println(author.getTitle() + " : " + author.getRole() + " => " + author.getPublicationDateFrom() + " to " + author.getPublicationDateTo());
//        }
    }

    @Test
    void findSeriesFromLibraryByAuthorRoles() {
        List<AuthorRolesBySerieResource> series = this.serieRepository.findSeriesFromLibraryByAuthorRoles(1L,13276L);
        assertThat(series.size()).isGreaterThan(0);

//        System.out.println(series.size());
//        for(AuthorRolesBySerieResource author : series) {
//            System.out.println(author.getTitle() + " : " + author.getRole() + " => " + author.getPublicationDateFrom() + " to " + author.getPublicationDateTo());
//        }
    }

    @Test
    void findSeriesByTitleStartingWithIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        Page<Serie> series = serieRepository.findSeriesByTitleStartingWithIgnoreCase("A", expectedPage.getPageable());
        assertThat(series.getSize()).isGreaterThan(0);
    }

    @Test
    void findSeriesByTitleLessThanIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        Page<Serie> series = serieRepository.findSeriesByTitleLessThanIgnoreCase("a", expectedPage.getPageable());
        assertThat(series.getSize()).isGreaterThan(0);
    }


    @Test
    public void countByOrigin() {
        List<SeriesOriginCounterResource> seriesOriginCounterResources = this.serieRepository.countSeriesByOrigin();
        assertThat(seriesOriginCounterResources.size()).isGreaterThan(0);
    }

    @Test
    public void countSeriesByStatus() {
        List<SeriesStatusCounterResource> seriesStatusCounterResources = this.serieRepository.countSeriesByStatus();
        assertThat(seriesStatusCounterResources.size()).isGreaterThan(0);
    }

    @Test
    public void findSeriesFromLibraryByTitleStartingWithIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        Page<Serie> series = serieRepository.findSeriesFromLibraryByTitleStartingWithIgnoreCase(1L, "a", expectedPage.getPageable());
        assertThat(series.getContent().size()).isGreaterThan(0);
    }

    @Test
    public void findSeriesFromLibraryByTitleLessThanIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        Page<Serie> series = serieRepository.findSeriesFromLibraryByTitleLessThanIgnoreCase(1L, "a", expectedPage.getPageable());
        assertThat(series.getContent().size()).isGreaterThan(0);
    }
}