package com.freebds.backend.repository;

import com.freebds.backend.model.Author;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(10)
    void addAuthor(){
        Author p1 = new Author();
        p1.setId(1L);
        p1.setExternalId("33648");
        p1.setLastname("Connor");
        p1.setFirstname("Sarah");
        p1 = authorRepository.saveAndFlush(p1);

        assertThat(p1.getId()).isEqualTo(1L);
        assertThat(p1.getExternalId()).isEqualTo("33648");
        assertThat(p1.getLastname()).isEqualTo("Connor");
        assertThat(p1.getFirstname()).isEqualTo("Sarah");
    }

    @Test
    @Order(20)
    void findAll() {
        //insertDb();
        List<Author> authors = authorRepository.findAll();
        for(Author author : authors)
            System.out.println(author.getId() + ", " + author.getLastname() + " " + author.getFirstname() + ", " + author.getExternalId());

        assertThat(authors.size()).isEqualTo(3);
    }

    @Test
    @Order(30)
    void findFirstByExternalId(){
        Author author = authorRepository.findFirstByExternalId("33648");
        assertThat(author.getLastname()).isEqualTo("uderzo");
    }

    @Test
    @Order(31)
    void findAuthorByExternalId(){
        List<Author> authors = authorRepository.findAuthorByExternalId("33648");
        assertThat(authors.size()).isEqualTo(1);
    }

}