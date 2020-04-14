package com.freebds.backend.repository;

import com.freebds.backend.common.web.resources.AuthorRolesBySerieResource;
import com.freebds.backend.common.web.resources.SeriesOriginCounterResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
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
    void findSeriesByAuthorRoles() {
        List<AuthorRolesBySerieResource> series = this.serieRepository.findSeriesByAuthorRoles(13276L);
        System.out.println(series.size());
        for(AuthorRolesBySerieResource author : series) {
            System.out.println(author.getTitle() + " : " + author.getRole() + " => " + author.getPublicationDateFrom() + " to " + author.getPublicationDateTo());
        }
    }

    @Test
    public void count() {
        long count = this.serieRepository.count();
        System.out.println(count);
    }

    @Test
    public void countByOrigin() {
        List<SeriesOriginCounterResource> seriesOriginCounterResources = this.serieRepository.countSeriesByOrigin();
        for(SeriesOriginCounterResource s : seriesOriginCounterResources)
            System.out.println(s.getOrigin() + "=" + s.getCount());
    }
}