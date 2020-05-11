package com.freebds.backend.repository;

import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
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
class AuthorRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(authorRepository).isNotNull();
    }

    @Test
    void findFirstByExternalId() {
        Author author = authorRepository.findFirstByExternalId("148");
        assertThat(author.getLastname()).isEqualTo("Uderzo");
    }

    @Test
    void findAuthorsByExternalId() {
        List<Author> authors = authorRepository.findAuthorsByExternalId("148");
        assertThat(authors.size()).isEqualTo(1);
    }

    @Test
    void findDistinctNationalities() {
        List<String> nationalities = authorRepository.findDistinctNationalities();
        assertThat(nationalities.size()).isGreaterThan(0);
    }

    @Test
    void findGraphicNovelAuthorByAuthor() {
        Page expectedPage = new PageImpl(new ArrayList<GraphicNovel>());
        Page<GraphicNovel> graphicNovels = authorRepository.findGraphicNovelAuthorByAuthor(expectedPage.getPageable(), 33648L);
        assertThat(graphicNovels.getSize()).isGreaterThan(0);
    }

    @Test
    void findBySearchFilters() throws ParseException {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("0001-01-01");
        java.sql.Date publicationDateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("9999-01-01");
        java.sql.Date publicationDateTo = new java.sql.Date(parsed.getTime());

        Page<Author> authors = authorRepository.findBySearchFilters(
                expectedPage.getPageable(), null, null, null, null, null, null,
                null, null, null, null, null, publicationDateFrom, publicationDateTo,
                null, null, null, "148", null);

        assertThat(authors.getContent().size()).isEqualTo(1);
    }

    @Test
    void findAuthorsByLastnameStartingWithIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<Author> authors = authorRepository.findAuthorsByLastnameStartingWithIgnoreCase("A", expectedPage.getPageable());
        assertThat(authors.getSize()).isGreaterThan(0);
    }

    @Test
    void findAuthorsByLastnameLessThanIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<Author> authors = authorRepository.findAuthorsByLastnameLessThanIgnoreCase("a", expectedPage.getPageable());
        assertThat(authors.getSize()).isGreaterThan(0);
    }

    @Test
    void findAuthorsFromLibraryByLastnameStartingWithIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<Author> authors = authorRepository.findAuthorsFromLibraryByLastnameStartingWithIgnoreCase(1L,"a", expectedPage.getPageable());
        assertThat(authors.getSize()).isGreaterThan(0);
    }

    @Test
    void findAuthorsFromLibraryByLastnameLessThanIgnoreCase() {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<Author> authors = authorRepository.findAuthorsFromLibraryByLastnameLessThanIgnoreCase(1L,"a", expectedPage.getPageable());
        assertThat(authors.getSize()).isGreaterThan(0);
    }

    @Test
    void findInLibraryBySearchFilters() throws ParseException {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("0001-01-01");
        java.sql.Date publicationDateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("9999-01-01");
        java.sql.Date publicationDateTo = new java.sql.Date(parsed.getTime());

        Page<Author> authors = authorRepository.findBySearchFilters(
                expectedPage.getPageable(), null, null, null, null, null, null,
                null, null, null, null, null, publicationDateFrom, publicationDateTo,
                null, null, null, "148", null);

        assertThat(authors.getContent().size()).isEqualTo(1);
    }

}