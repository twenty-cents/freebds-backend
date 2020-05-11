package com.freebds.backend.service;

import com.freebds.backend.common.web.author.resources.AuthorResource;
import com.freebds.backend.model.Author;
import com.freebds.backend.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;


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
        // Correct??
        // Given
        Pageable pAuthors = new PageImpl(new ArrayList<Author>()).getPageable();
        Page<Author> expectedPage = new PageImpl(new ArrayList<Author>());
        given(authorRepository.findAll(pAuthors)).willReturn(expectedPage);

        // When
        Page<AuthorResource> actualAuthors = authorService.getAuthors(pAuthors);

        // Then
        assertThat(actualAuthors.getSize()).isEqualTo(expectedPage.getSize());
    }

    @Test
    void getAuthorByExternalId() {
        // Given
        Author expectedAuthor = new Author();
        expectedAuthor.setId(1L);
        expectedAuthor.setExternalId("44560");
        List<Author> expectedAuthors = Arrays.asList(expectedAuthor);

        doReturn(expectedAuthors).when(authorRepository).findAuthorsByExternalId("44560");

        // When
        Author actualAuthor = authorService.getAuthorByExternalId("44560");

        // Then
        assertThat(actualAuthor.getExternalId()).isEqualTo(expectedAuthors.get(0).getExternalId());
    }

    @Test
    void getFilteredAuthors() {
        // TODO : how to implement a test with page/pageable?
        // Correct??
        // Given
        Pageable pAuthors = new PageImpl(new ArrayList<Author>()).getPageable();
        Page<Author> expectedPage = new PageImpl(new ArrayList<Author>());
        given(authorRepository.findAuthorsByLastnameIgnoreCaseContainingAndFirstnameIgnoreCaseContainingAndNicknameIgnoreCaseContainingAndNationalityIgnoreCaseContaining(
                pAuthors, "", "", "", "")).willReturn(expectedPage);

        // When
        Page<Author> actualAuthors = authorService.getFilteredAuthors(pAuthors, "", "", "", "");

        // Then
        assertThat(actualAuthors.getSize()).isEqualTo(expectedPage.getSize());
    }
}