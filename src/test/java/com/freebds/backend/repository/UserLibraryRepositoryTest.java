package com.freebds.backend.repository;

import com.freebds.backend.common.web.library.resources.ILibraryResource;
import com.freebds.backend.common.web.library.resources.IUserLibraryStatusResource;
import com.freebds.backend.model.UserLibrary;
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
class UserLibraryRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserLibraryRepository userLibraryRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(userLibraryRepository).isNotNull();
    }

    @Test
    void findFirstByUserIdAndRoleEquals() {
        List<UserLibrary> userLibrary = this.userLibraryRepository.findUserLibrary(2L, "ADMIN");
        assertThat(userLibrary.size()).isGreaterThan(0);
    }

    @Test
    void findLibraries() {
        List<ILibraryResource> libraryResources = this.userLibraryRepository.findLibraries(2L);
        assertThat(libraryResources.size()).isGreaterThan(0);
    }

    @Test
    public void findUsersStatusOnPersonalLibrary() {
        List<IUserLibraryStatusResource> iUserLibraryStatusResources = userLibraryRepository.findUsersStatusOnPersonalLibrary(2L, 1L);
        assertThat(iUserLibraryStatusResources.size()).isGreaterThan(0);
    }

}