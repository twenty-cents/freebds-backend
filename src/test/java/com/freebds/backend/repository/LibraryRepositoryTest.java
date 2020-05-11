package com.freebds.backend.repository;

import com.freebds.backend.model.Library;
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
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class LibraryRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LibraryRepository libraryRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(libraryRepository).isNotNull();
    }

    @Test
    void findLibraryByUser() {
        Optional<Library> optionalLibrary = libraryRepository.findLibraryByUser(1L, 2L);
        assertThat(optionalLibrary.isPresent()).isTrue();
    }

    @Test
    void findUserRole() {
        Optional<String> optionalUserRole = libraryRepository.findUserRole(1L, 2L);
        assertThat(optionalUserRole.isPresent()).isTrue();
    }

    @Test
    void findLibrariesByUser() {
        List<Library> libraries = libraryRepository.findLibrariesByUser(2L);
        assertThat(libraries.size()).isGreaterThan(0);
    }
}