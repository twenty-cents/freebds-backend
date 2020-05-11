package com.freebds.backend.controller;

import com.freebds.backend.common.web.graphicNovel.requests.SetReviewRequest;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.model.Review;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/graphic-novels/library/review", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReviewController {

    private final ContextService contextService;
    private final ReviewService reviewService;

    /**
     * Add a review
     * @param setReviewRequest the review infos to add
     * @return the added review in the library
     */
    @PostMapping("")
    public Review addReview(
            @RequestBody SetReviewRequest setReviewRequest) {

        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(setReviewRequest.getContext(), setReviewRequest.getLibraryId(), "ADMIN");

        return this.reviewService.saveReview(contextResource, setReviewRequest);
    }

    /**
     * Update a review
     * @param reviewId the review to update
     * @param setReviewRequest the review infos to update
     * @return the updated review in the library
     */
    @PutMapping("/{reviewId}")
    public Review updateReview(
            @PathVariable Long reviewId,
            @RequestBody SetReviewRequest setReviewRequest) {

        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(setReviewRequest.getContext(), setReviewRequest.getLibraryId(), "ADMIN");

        return this.reviewService.saveReview(contextResource, setReviewRequest);
    }

}
