package com.freebds.backend.repository;

import com.freebds.backend.common.web.author.resources.AuthorRoleResource;
import com.freebds.backend.common.web.dashboard.resources.PeriodicityCountResource;
import com.freebds.backend.common.web.dashboard.resources.PublicationsByMonthGroupByOriginResource;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelMinimumResource;
import com.freebds.backend.model.Author;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Serie;
import org.junit.jupiter.api.DisplayName;
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
class GraphicNovelRepositoryTest {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GraphicNovelRepository graphicNovelRepository;

    @Autowired
    private SerieRepository serieRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(graphicNovelRepository).isNotNull();
        assertThat(serieRepository).isNotNull();
    }

    @Test
    public void findGraphicNovelsByExternalId() {
        List<GraphicNovel> g = this.graphicNovelRepository.findGraphicNovelsByExternalId("22957");
        assertThat(g.size()).isGreaterThan(0);
    }

    @Test
    public void findAuthorRolesById(){
        List<AuthorRoleResource> authorRoleResources = this.graphicNovelRepository.findAuthorRolesById(22957L);
        assertThat(authorRoleResources.size()).isGreaterThan(0);
    }

    @Test
    public void findGraphicNovelsBySerieIdOrderByTome(){
        List<GraphicNovel> graphicNovels = this.graphicNovelRepository.findGraphicNovelsBySerieIdOrderByTome(3476L);
        assertThat(graphicNovels.size()).isGreaterThan(0);
    }

    @Test
    void findGraphicNovelsBySerieEquals() {
        // Get a serie for the use case
        Serie serie = serieRepository.findById(3476L).get();

        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<GraphicNovel> graphicNovels = graphicNovelRepository.findGraphicNovelsBySerieEquals(expectedPage.getPageable(), serie);

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

        Page<GraphicNovel> graphicNovels = graphicNovelRepository.findBySearchFilters(
                expectedPage.getPageable(), null, null, null, null, null, null,
                null, null, null, null, null, publicationDateFrom, publicationDateTo,
                null, null, null, "148", null);

        assertThat(graphicNovels.getContent().size()).isGreaterThan(0);
    }

    @Test
    void findGraphicNovelsFromLibraryBySerie() {
        Page expectedPage = new PageImpl(new ArrayList<Author>());
        Page<GraphicNovel> graphicNovels = graphicNovelRepository.findGraphicNovelsFromLibraryBySerie(1L, 3476L, expectedPage.getPageable());

        assertThat(graphicNovels.getSize()).isGreaterThan(0);
    }

    @Test
    public void findMissingGraphicNovelsFromLibraryBySerie() {
        List<GraphicNovelMinimumResource> serieMinimumResources = this.graphicNovelRepository.findMissingGraphicNovelsFromLibraryBySerie(1L, 1084L);
        assertThat(serieMinimumResources.size()).isGreaterThan(0);
    }

    @DisplayName("Find a graphic novel by a ISBN/EAN13 code")
    @Test
    public void findGraphicNovelsByISBN() {
        // Find a graphic novel with the repository
        List<GraphicNovel> graphicNovels = this.graphicNovelRepository.findGraphicNovelsByISBN("9782864970118");
        // Test if the returned graphic novels list is not empty
        assertThat(graphicNovels.size()).isGreaterThan(0);
        // Test if the first returned graphic novel has the wanted ISBN/EAN13
        assertThat(graphicNovels.get(0).getIsbn()).isEqualTo("978-2-86497-011-8");
    }

    @Test
    public void finPublicationsByMonthGroupByOrigin() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("0001-01-01");
        java.sql.Date dateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("9999-01-01");
        java.sql.Date dateTo = new java.sql.Date(parsed.getTime());

        List<PublicationsByMonthGroupByOriginResource> publicationsByMonthGroupByOriginResources =
                this.graphicNovelRepository.findPublicationsByMonthGroupByOrigin(dateFrom, dateTo);

        assertThat(publicationsByMonthGroupByOriginResources.size()).isGreaterThan(0);
    }

    @Test
    public void countPublicationsByMonth() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse("0001-01-01");
        java.sql.Date dateFrom = new java.sql.Date(parsed.getTime());
        parsed = format.parse("9999-01-01");
        java.sql.Date dateTo = new java.sql.Date(parsed.getTime());

        List<PeriodicityCountResource> periodicityCountResources =
                this.graphicNovelRepository.countPublicationsByMonth(dateFrom, dateTo);

        assertThat(periodicityCountResources.size()).isGreaterThan(0);
    }
}