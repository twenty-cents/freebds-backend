package com.freebds.backend.repository;

import com.freebds.backend.common.web.resources.AuthorRoleResource;
import com.freebds.backend.model.GraphicNovel;
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
import java.util.Optional;

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

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(graphicNovelRepository).isNotNull();
    }

    @Test
    public void findById() {
        Optional<GraphicNovel> g = this.graphicNovelRepository.findById(3453L);
        if(g.isPresent()) {
            System.out.println(g);
        }
    }

    @Test
    public void findAuthorRolesById(){
        List<AuthorRoleResource> authorRoleResources = this.graphicNovelRepository.findAuthorRolesById(3453L);
        for(AuthorRoleResource a : authorRoleResources) {
            System.out.println(a.getLastname() + "," + a.getFirstname() + "," + a.getNickname() + ", role=" + a.getRole());
        }
    }
}