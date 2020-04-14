package com.freebds.backend.repository;

import com.freebds.backend.model.GraphicNovel;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class SerieSpecificationRepositoryTest {

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
/**
    @Test
    void filterByTitle() {
        List<Serie> actualSeries = serieRepository.filterByTitle("gaston");
        for(Serie s : actualSeries) {
            System.out.println(s.toString());
            // System.out.println(s.getExternalId() + " - " + s.getTitle() + " - " + s.getCategories());
        }
    }

    @Test
    void findByFilters() {
        LocalDate publicationDateFrom = LocalDate.parse("1960-01-01");
        LocalDate publicationDateTo = LocalDate.parse("2020-01-01");

        List<Serie> actualSeries = serieRepository.findByFilters(
                "gaston", "31", "humour", "Série finie", "Europe", "Français",
                null, null, "Dupuis", null, "2-8001-0091-5", publicationDateFrom, publicationDateTo, true,
                "franquin");
        for(Serie s : actualSeries) {
            System.out.println(s.toString());
        }
    }
*/
    @Test
    void findByCustomFilters() throws ParseException {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        //Date publicationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("1960-01-01");
        //Date publicationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01");
        //LocalDate publicationDateFrom = LocalDate.parse("1960-01-01");
        //LocalDate publicationDateTo = LocalDate.parse("2020-01-01");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("1960-01-01");
        java.sql.Date publicationDateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("1970-01-01");
        java.sql.Date publicationDateTo = new java.sql.Date(parsed.getTime());
        System.out.println(publicationDateFrom);

        Page<Serie> series = serieRepository.findBySearchFilters( expectedPage.getPageable(),
                "gaston", null, null, null, null, null,
                 null, null, null, null, null, publicationDateFrom, publicationDateTo,
                 null, null, null, null);

        System.out.println(series.toList().size());
        for(Serie s : series.toList()) {
            System.out.println(s.toString());
        }
    }

    @Test
    void findByPublicationDates() throws ParseException {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        //Date publicationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("1960-01-01");
        //Date publicationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("1970-01-01");
        //LocalDate publicationDateFrom = LocalDate.parse("1960-01-01");
        //LocalDate publicationDateTo = LocalDate.parse("2020-01-01");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("1960-01-01");
        java.sql.Date publicationDateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("1961-03-01");
        java.sql.Date publicationDateTo = new java.sql.Date(parsed.getTime());

        Page<GraphicNovel> series = serieRepository.findByPublicationDates( expectedPage.getPageable(), publicationDateFrom, publicationDateTo);

        System.out.println(series.toList().size());

    }

}