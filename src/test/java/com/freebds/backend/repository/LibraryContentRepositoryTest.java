package com.freebds.backend.repository;

import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.LibraryContent;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class LibraryContentRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LibraryContentRepository libraryContentRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(libraryContentRepository).isNotNull();
    }

    @Test
    void findFirstByLibrary_IdAndGraphicNovel_Id() {
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findFirstByLibrary_IdAndGraphicNovel_Id(1L,16734L);
        assertThat(optionalLibraryContent.isPresent()).isTrue();
    }

    @Test
    void findGraphicNovelsFromLibraryBySerie() {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<GraphicNovel> graphicNovels = libraryContentRepository.findGraphicNovelsFromLibraryBySerie(1L, 3476L, expectedPage.getPageable());
        assertThat(graphicNovels.getSize()).isGreaterThan(0);
    }

    @Test
    void findMissingGraphicNovelsFromLibraryBySerie() {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<GraphicNovel> graphicNovels = libraryContentRepository.findMissingGraphicNovelsFromLibraryBySerie(1L, 3476L, expectedPage.getPageable());
        assertThat(graphicNovels.getSize()).isGreaterThan(0);
    }

    @Test
    void findInLibraryBySearchFilters() throws ParseException {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("0001-01-01");
        java.sql.Date publicationDateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("9999-01-01");
        java.sql.Date publicationDateTo = new java.sql.Date(parsed.getTime());

        Page<GraphicNovel> graphicNovels = libraryContentRepository.findBySearchFilters(
                expectedPage.getPageable(), 1L,null, null, null, null, null, null,
                null, null, null, null, null, publicationDateFrom, publicationDateTo,
                null, null, null, "148", null);

        assertThat(graphicNovels.getSize()).isGreaterThan(0);
    }
}