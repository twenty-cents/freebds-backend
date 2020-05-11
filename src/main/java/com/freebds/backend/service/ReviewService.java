package com.freebds.backend.service;

import com.freebds.backend.common.web.graphicNovel.requests.Rating;
import com.freebds.backend.common.web.graphicNovel.requests.SetReviewRequest;
import com.freebds.backend.common.web.graphicNovel.resources.ReviewResource;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.model.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    Review setRating(ContextResource contextResource, Rating rating);

    List<ReviewResource> findReviewsByLibraryContent(Long librarySerieId, Long libraryContentId);

    Review saveReview(ContextResource contextResource, SetReviewRequest setReviewRequest);
}
