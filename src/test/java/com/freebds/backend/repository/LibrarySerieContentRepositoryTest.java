package com.freebds.backend.repository;

import com.freebds.backend.model.Author;
import com.freebds.backend.model.LibrarySerieContent;
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
class LibrarySerieContentRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LibrarySerieContentRepository librarySerieContentRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(librarySerieContentRepository).isNotNull();
    }

    @Test
    public void checkIfExist() {
        Long serieId = librarySerieContentRepository.checkIfExist(3476L, 1L);
        assertThat(serieId).isEqualTo(1L);
    }

    @Test
    public void findLibrarySerie(){
        List<LibrarySerieContent> librarySerieContents = librarySerieContentRepository.findLibrarySerie(1L, 3476L);
        assertThat(librarySerieContents.size()).isEqualTo(1L);
    }

    @Test
    void findAllSeries() {
        Page expectedPage = new PageImpl(new ArrayList<Serie>());
        Page<Serie> series = this.librarySerieContentRepository.findAllSeries(1L, "a", expectedPage.getPageable());
        assertThat(series.getContent().size()).isGreaterThan(0);
    }

    @Test
    void findBySearchFilters() throws ParseException {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("0001-01-01");
        java.sql.Date publicationDateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("9999-01-01");
        java.sql.Date publicationDateTo = new java.sql.Date(parsed.getTime());

        Page<Serie> series = librarySerieContentRepository.findBySearchFilters(
                expectedPage.getPageable(), 1L,null, null, null, null, null, null,
                null, null, null, null, null, publicationDateFrom, publicationDateTo,
                null, null, null, "148", null);

        assertThat(series.getContent().size()).isGreaterThan(0);
    }

}