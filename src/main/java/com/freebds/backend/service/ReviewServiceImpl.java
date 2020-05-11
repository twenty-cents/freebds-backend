package com.freebds.backend.service;

import com.freebds.backend.common.web.graphicNovel.requests.Rating;
import com.freebds.backend.common.web.graphicNovel.requests.SetReviewRequest;
import com.freebds.backend.common.web.graphicNovel.resources.ReviewResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.model.LibrarySerieContent;
import com.freebds.backend.model.Review;
import com.freebds.backend.repository.LibraryContentRepository;
import com.freebds.backend.repository.LibrarySerieContentRepository;
import com.freebds.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final LibraryContentRepository libraryContentRepository;
    private final LibrarySerieContentRepository librarySerieContentRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Set a rating for a graphic novel in a library
     * @param contextResource the context
     * @param rating the rating to set
     * @return the updated library graphic novel
     */
    @Override
    public Review setRating(ContextResource contextResource, Rating rating) {
        // Check if the library serie exists
        Optional<LibrarySerieContent> optionalLibrarySerieContent = this.librarySerieContentRepository.findById(rating.getLibrarySerieId());
        if(optionalLibrarySerieContent.isPresent() == false) {
            throw new EntityNotFoundException(rating.getLibrarySerieId(), "Library Serie ID, on Library Serie");
        }
        LibrarySerieContent librarySerieContent = optionalLibrarySerieContent.get();

        // Check if the library graphic novel exists
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findById(rating.getLibraryContentId());
        if(optionalLibraryContent.isPresent() == false) {
            throw new EntityNotFoundException(rating.getLibraryContentId(), "Library Graphic novel ID, on Library Graphic novel");
        }
        LibraryContent lc = optionalLibraryContent.get();

        if( ! (lc.getLibrarySerieContent().getId().longValue() == rating.getLibrarySerieId().longValue() && lc.getGraphicNovel().getId().longValue() == rating.getGraphicNovelId().longValue())){
            throw new FreeBdsApiException(
                    "Incohérence dans les clés encyclopédie / collection",
                    "Library serie id / graphic novel id" +
                            lc.getLibrarySerieContent().getId() + " / " + rating.getLibrarySerieId() + " , " +
                            lc.getGraphicNovel().getId() + " / " + rating.getGraphicNovelId()
            );
        }

        // Check if the review already exists
        Review review;
        if(rating.getReviewId() != null) {
            Optional<Review> optionalReview = this.reviewRepository.findById(rating.getReviewId());
            if(optionalReview.isPresent() == true) {
                review = optionalReview.get();
                // Check if the requested graphic novel keys are ok
                if(review.getLibraryContent().getId() != rating.getLibraryContentId() || review.getUser().getId() != contextResource.getUser().getId()) {
                    throw new FreeBdsApiException(
                            "Incohérence dans les clés review / user / graphic novel",
                            "Library review id / user id / graphic novel"
                    );
                }
                // Update the score
                review.setScore(rating.getRating());
                review.setLastUpdateDate(LocalDateTime.now());
                review.setLastUpdateUser(contextResource.getUser().getFirstname() + " " + contextResource.getUser().getLastname());
                this.reviewRepository.saveAndFlush(review);
            } else {
                throw new EntityNotFoundException(rating.getReviewId(), "Review ID, on Review");
            }
        } else {
            review = new Review().builder()
                    .user(contextResource.getUser())
                    .librarySerieContent(librarySerieContent)
                    .libraryContent(lc)
                    .user(contextResource.getUser())
                    .score(rating.getRating())
                    .comment(null)
                    .lastUpdateDate(LocalDateTime.now())
                    .lastUpdateUser(contextResource.getUser().getFirstname() + " " + contextResource.getUser().getLastname())
                    .build();

            // Add the score
            review.setScore(rating.getRating());
            this.reviewRepository.saveAndFlush(review);
        }

        return review;
    }

    /**
     * Get all reviews on a library content
     * @param librarySerieId
     * @param libraryContentId
     * @return
     */
    public List<ReviewResource> findReviewsByLibraryContent(Long librarySerieId, Long libraryContentId) {
        return this.reviewRepository.findReviewsByLibraryContent(librarySerieId, libraryContentId);
    }

    /**
     * Save a review
     * @param contextResource the context
     * @param setReviewRequest the review infos to save
     * @return the saved review in the library
     */
    @Override
    public Review saveReview(ContextResource contextResource, SetReviewRequest setReviewRequest) {
        // Check if the library serie exists
        Optional<LibrarySerieContent> optionalLibrarySerieContent = this.librarySerieContentRepository.findById(setReviewRequest.getLibrarySerieId());
        if(optionalLibrarySerieContent.isPresent() == false) {
            throw new EntityNotFoundException(setReviewRequest.getLibrarySerieId(), "Library Serie ID, on Library Serie");
        }
        LibrarySerieContent librarySerieContent = optionalLibrarySerieContent.get();

        // Check if the library graphic novel exists
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findById(setReviewRequest.getLibraryContentId());
        if(optionalLibraryContent.isPresent() == false) {
            throw new EntityNotFoundException(setReviewRequest.getLibraryContentId(), "Library Graphic novel ID, on Library Graphic novel");
        }
        LibraryContent lc = optionalLibraryContent.get();

        if( ! (lc.getLibrarySerieContent().getId().longValue() == setReviewRequest.getLibrarySerieId().longValue() && lc.getGraphicNovel().getId().longValue() == setReviewRequest.getGraphicNovelId().longValue())){
            throw new FreeBdsApiException(
                    "Incohérence dans les clés encyclopédie / collection",
                    "Library serie id / graphic novel id" +
                            lc.getLibrarySerieContent().getId() + " / " + setReviewRequest.getLibrarySerieId() + " , " +
                            lc.getGraphicNovel().getId() + " / " + setReviewRequest.getGraphicNovelId()
            );
        }

        // Check if the review already exists
        Review review;
        if(setReviewRequest.getReviewId() != null) {
            Optional<Review> optionalReview = this.reviewRepository.findById(setReviewRequest.getReviewId());
            if(optionalReview.isPresent() == true) {
                review = optionalReview.get();
                // Check if the requested graphic novel keys are ok
                if(review.getLibraryContent().getId() != setReviewRequest.getLibraryContentId() || review.getUser().getId() != contextResource.getUser().getId()) {
                    throw new FreeBdsApiException(
                            "Incohérence dans les clés review / user / graphic novel",
                            "Library review id / user id / graphic novel"
                    );
                }
                // Update the review
                review.setScore(setReviewRequest.getRating());
                review.setLastUpdateDate(LocalDateTime.now());
                review.setLastUpdateUser(contextResource.getUser().getFirstname() + " " + contextResource.getUser().getLastname());
                review.setComment(setReviewRequest.getComment());
                this.reviewRepository.saveAndFlush(review);
            } else {
                throw new EntityNotFoundException(setReviewRequest.getReviewId(), "Review ID, on Review");
            }
        } else {
            review = new Review().builder()
                    .user(contextResource.getUser())
                    .librarySerieContent(librarySerieContent)
                    .libraryContent(lc)
                    .user(contextResource.getUser())
                    .score(setReviewRequest.getRating())
                    .comment(setReviewRequest.getComment())
                    .lastUpdateDate(LocalDateTime.now())
                    .lastUpdateUser(contextResource.getUser().getFirstname() + " " + contextResource.getUser().getLastname())
                    .build();

            // Add the score
            review.setScore(setReviewRequest.getRating());
            this.reviewRepository.saveAndFlush(review);
        }

        return review;
    }

}
