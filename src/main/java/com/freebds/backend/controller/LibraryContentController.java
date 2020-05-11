package com.freebds.backend.controller;

import com.freebds.backend.common.web.graphicNovel.requests.AssociateToLibraryRequest;
import com.freebds.backend.common.web.graphicNovel.requests.Rating;
import com.freebds.backend.common.web.libraryContent.requests.LibraryContentUpdateRequest;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.model.Review;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.LibraryContentService;
import com.freebds.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/library-content", produces = { MediaType.APPLICATION_JSON_VALUE })
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class LibraryContentController {

    private final ContextService contextService;
    private final LibraryContentService libraryContentService;
    private final ReviewService reviewService;

    /**
     * Add a graphic novel in a library
     * @param graphicNovelId the graphic novel id to add
     * @param associateToLibraryRequest the graphic novel to associate
     * @return the library graphic novel added in the library
     */
    @PostMapping("/{graphicNovelId}")
    public LibraryContent addToLibrary(
            @PathVariable Long graphicNovelId,
            @RequestBody AssociateToLibraryRequest associateToLibraryRequest) {

        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(associateToLibraryRequest.getContext(), associateToLibraryRequest.getLibraryId(), "USER");

        return this.libraryContentService.addGraphicNovelToCollection(contextResource, associateToLibraryRequest);
    }

    /**
     * Update a library content
     * @param libraryContentId the library content to update
     * @param libraryContentUpdateRequest the library content infos to update
     * @return the library graphic novel added in the library
     */
    @PutMapping("/{libraryContentId}")
    public LibraryContent updateLibraryContent(
            @PathVariable Long libraryContentId,
            @RequestBody LibraryContentUpdateRequest libraryContentUpdateRequest) {

        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext("library", libraryContentUpdateRequest.getLibraryId(), "ADMIN");

        return this.libraryContentService.updateLibraryContent(contextResource, libraryContentUpdateRequest);
    }

    /**
     * Delete a library content
     * @param libraryContentId the library content to update
     * @param libraryId the library content infos to update
     * @return the library graphic novel added in the library
     */
    @DeleteMapping("/{libraryContentId}/library/{libraryId}")
    public boolean addToLibrary(
            @PathVariable Long libraryContentId,
            @PathVariable Long libraryId) {

        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext("library", libraryId, "ADMIN");

        return this.libraryContentService.deleteLibraryContent(contextResource, libraryContentId);
    }

    @PostMapping("/ratings/{graphicNovelId}")
    public Review addRating (
            @PathVariable Long graphicNovelId,
            @RequestBody Rating rating) {

        // Get context (throw an exception if incorrect)
        ContextResource contextResource = this.contextService.getContext(rating.getContext(), rating.getLibraryId(), "USER");

        return this.reviewService.setRating(contextResource, rating);
    }
}
