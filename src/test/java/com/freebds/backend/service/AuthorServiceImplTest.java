package com.freebds.backend.service;

import com.freebds.backend.model.Author;
import com.freebds.backend.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    public void setUp() {
        this.authorService = new AuthorServiceImpl(authorRepository);
    }

    @Test
    void getAuthorById() {
        // Given
        Author expectedAuthor = new Author();
        expectedAuthor.setId(1L);
        Optional<Author> OpExpectedAuthor = Optional.of(expectedAuthor);

        doReturn(OpExpectedAuthor).when(authorRepository).findById(1L);

        // When
        Author actualAuthor = authorService.getAuthorById(1L);

        // Then
        assertThat(actualAuthor.getId()).isEqualTo(expectedAuthor.getId());
    }

    @Test
    void getAuthors() {
        // TODO : how to implement a test with page/pageable?
    }

    @Test
    void getAuthorByExternalId() {
        // Given
        Author expectedAuthor = new Author();
        expectedAuthor.setId(1L);
        expectedAuthor.setExternalId("44560");
        List<Author> expectedAuthors = Arrays.asList(expectedAuthor);

        doReturn(expectedAuthors).when(authorRepository).findAuthorByExternalId("44560");

        // When
        Author actualAuthor = authorService.getAuthorByExternalId("44560");

        // Then
        assertThat(actualAuthor.getExternalId()).isEqualTo(expectedAuthors.get(0).getExternalId());
    }

    @Test
    void getFilteredAuthors() {
        // TODO : how to implement a test with page/pageable?
    }
}