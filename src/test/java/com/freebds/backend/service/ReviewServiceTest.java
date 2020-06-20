package com.freebds.backend.service;

import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.common.web.graphicNovel.requests.SetReviewRequest;
import com.freebds.backend.common.web.graphicNovel.resources.ReviewResource;
import com.freebds.backend.model.*;
import com.freebds.backend.repository.LibraryContentRepository;
import com.freebds.backend.repository.LibrarySerieContentRepository;
import com.freebds.backend.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReviewServiceTest {

    @Mock
    private LibraryContentRepository libraryContentRepository;

    @Mock
    private LibrarySerieContentRepository librarySerieContentRepository;

    @Mock
    private ReviewRepository reviewRepository;

    //@InjectMocks
    //private ReviewService reviewService = new ReviewServiceImpl(libraryContentRepository, librarySerieContentRepository, reviewRepository);
    private ReviewService reviewService;

    private User user;
    private Library library;
    private ContextResource contextResource;
    private LibrarySerieContent librarySerieContent;
    private GraphicNovel graphicNovel;
    private LibraryContent libraryContent;
    private Review review;
    private SetReviewRequest setReviewRequest;

    @BeforeEach
    public void setUp() {
        reviewService = new ReviewServiceImpl(libraryContentRepository, librarySerieContentRepository, reviewRepository);
        // Given
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

        librarySerieContent = LibrarySerieContent
                .builder()
                .id(1L)
                .build();

        graphicNovel = GraphicNovel
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

        setReviewRequest = SetReviewRequest
                .builder()
                .libraryId(1L)
                .librarySerieId(1L)
                .libraryContentId(1L)
                .graphicNovelId(1L)
                .reviewId(1L)
                .rating(5)
                .comment("comment")
                .build();

    }

    @DisplayName("Find all reviews on a library content")
    @Test
    public void findReviewsByLibraryContent() {
        // Given
        List<ReviewResource> reviewResources = new ArrayList<>();
        // When - Then
        when(reviewService.findReviewsByLibraryContent(1L, 1L)).thenReturn(reviewResources);
        // Test
        assertEquals(reviewResources, reviewService.findReviewsByLibraryContent(1L, 1L));
    }

    @DisplayName("Save a review")
    @Test
    public void saveReview() {

        // Mock a library serie
        when(librarySerieContentRepository.findById(anyLong())).thenReturn(Optional.of(librarySerieContent));
        // Mock a library content
        when(libraryContentRepository.findById(anyLong())).thenReturn(Optional.of(libraryContent));
        // Mock a review
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

        // When
        when(reviewService.saveReview(contextResource, setReviewRequest)).thenReturn(review);

        // Test
        assertEquals(review, reviewService.saveReview(contextResource, setReviewRequest));

    }
}