package com.freebds.backend.service;

import com.freebds.backend.common.web.author.resources.AuthorRoleResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.common.web.graphicNovel.requests.BarcodeScanRequest;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelResource;
import com.freebds.backend.common.web.graphicNovel.resources.ReviewResource;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.*;
import com.freebds.backend.repository.GraphicNovelRepository;
import com.freebds.backend.repository.LibraryContentRepository;
import com.freebds.backend.repository.ReviewRepository;
import com.freebds.backend.repository.SerieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@SpringBootConfiguration
class GraphicNovelServiceImplTest {

    private User user;
    private Library library;
    private ContextResource contextResource;
    private GraphicNovel graphicNovel;
    private GraphicNovelResource graphicNovelResource;
    private LibrarySerieContent librarySerieContent;
    private LibraryContent libraryContent;
    private Review review;

    @Value("${tests.barcode.image.invalid}")
    String invalidBarcode;

    @Value("${tests.barcode.image.valid}")
    String validBarcode;

    @Mock
    private GraphicNovelRepository graphicNovelRepository;
    @Mock
    private SerieRepository serieRepository;
    @Mock
    private LibraryContentRepository libraryContentRepository;
    @Mock
    private ReviewRepository reviewRepository;

    private GraphicNovelService graphicNovelService;

    @Configuration
    static class Config {

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertiesResolver() {
            return new PropertySourcesPlaceholderConfigurer();
        }

    }

    @BeforeEach
    public void setUp() {
        graphicNovelService = new GraphicNovelServiceImpl(graphicNovelRepository, serieRepository, libraryContentRepository, reviewRepository);

        user = new User();
        user.setId(1L);

        library = Library
                .builder()
                .id(1L)
                .build();

        contextResource = ContextResource
                .builder()
                .user(user)
                .library(library)
                .userRole("ADMIN")
                .isValid(true)
                .build();

        graphicNovel = GraphicNovel
                .builder()
                .id(1L)
                .isbn("9782864970118")
                .build();

        librarySerieContent = LibrarySerieContent
                .builder()
                .id(1L)
                .build();

        libraryContent = LibraryContent
                .builder()
                .id(1L)
                .librarySerieContent(librarySerieContent)
                .graphicNovel(graphicNovel)
                .build();

        review = Review
                .builder()
                .id(1L)
                .libraryContent(libraryContent)
                .user(user)
                .score(5)
                .comment("comment")
                .build();

        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);
        libraryContent.setReviews(reviews);

        List<ReviewResource> reviewResources = new ArrayList<ReviewResource>();
        graphicNovelResource = GraphicNovelResource
                .builder()
                .id(1L)
                .isbn("9782864970118")
                .build();
    }

    @DisplayName("Scan should fail - No image uploaded.")
    @Test
    void scan1() {
        // Given
        BarcodeScanRequest barcodeScanRequest = BarcodeScanRequest
                .builder()
                .libraryId(1L)
                .barcode("")
                .build();
        // Test
        FreeBdsApiException thrown = assertThrows(FreeBdsApiException.class, () -> {
            graphicNovelService.scan(contextResource, barcodeScanRequest);
        });
        assertTrue(thrown.getApiMessage().contains("Aucune image reçue."));
    }

    @DisplayName("Scan should fail - No barcode found in the uploaded image.")
    @Test
    void scan2() {
        // Given
        BarcodeScanRequest barcodeScanRequest = BarcodeScanRequest
                .builder()
                .libraryId(1L)
                .barcode(invalidBarcode)
                .build();

        // Test
        FreeBdsApiException thrown = assertThrows(FreeBdsApiException.class, () -> {
            graphicNovelService.scan(contextResource, barcodeScanRequest);
        });
        assertTrue(thrown.getApiMessage().contains("Aucun code-barre n'a pu être extrait de l'image."));
    }

    @DisplayName("Scan should be ok - No graphic novel found.")
    @Test
    void scan3() {
        // Given
        BarcodeScanRequest barcodeScanRequest = BarcodeScanRequest
                .builder()
                .libraryId(1L)
                .barcode(validBarcode)
                .build();

        List<GraphicNovel> graphicNovels = new ArrayList<>();
        graphicNovels.add(graphicNovel);
        // When - Mock repositories
        when(graphicNovelRepository.findGraphicNovelsByISBN(anyString())).thenReturn(new ArrayList<GraphicNovel>());
        when(graphicNovelRepository.findAuthorRolesById(anyLong())).thenReturn(new ArrayList<AuthorRoleResource>());
        when(reviewRepository.findReviewsByLibraryContent(anyLong(), anyLong())).thenReturn(new ArrayList<ReviewResource>());
        when(libraryContentRepository.findFirstByLibrary_IdAndGraphicNovel_Id(anyLong(), anyLong())).thenReturn(Optional.empty());
        // Test
        FreeBdsApiException thrown = assertThrows(FreeBdsApiException.class, () -> {
            graphicNovelService.scan(contextResource, barcodeScanRequest);
        });
        assertTrue(thrown.getApiMessage().contains("Aucune correspondance pour le code-barre 9782864970118 dans l'encyclopédie BD."));
    }

    @DisplayName("Scan should be ok - One graphic novel found.")
    @Test
    void scan4() {
        // Given
        BarcodeScanRequest barcodeScanRequest = BarcodeScanRequest
                .builder()
                .libraryId(1L)
                .barcode(validBarcode)
                .build();

        List<GraphicNovel> graphicNovels = new ArrayList<>();
        graphicNovels.add(graphicNovel);
        // When - Mock repositories
        when(graphicNovelRepository.findGraphicNovelsByISBN(anyString())).thenReturn(graphicNovels);
        when(graphicNovelRepository.findAuthorRolesById(anyLong())).thenReturn(new ArrayList<AuthorRoleResource>());
        when(reviewRepository.findReviewsByLibraryContent(anyLong(), anyLong())).thenReturn(new ArrayList<ReviewResource>());
        when(libraryContentRepository.findFirstByLibrary_IdAndGraphicNovel_Id(anyLong(), anyLong())).thenReturn(Optional.empty());
        // Execute the action to test
        List<GraphicNovelResource> expectedGraphicNovelResources = graphicNovelService.scan(contextResource, barcodeScanRequest);
        // Test
        // Almost one graphic novel found
        assertTrue(expectedGraphicNovelResources.size() > 0);
        // Correct ISBN found
        assertEquals(graphicNovelResource.getIsbn(), expectedGraphicNovelResources.get(0).getIsbn());
    }

}