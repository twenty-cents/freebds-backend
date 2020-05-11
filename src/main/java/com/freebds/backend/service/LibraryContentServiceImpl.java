package com.freebds.backend.service;

import com.freebds.backend.common.web.graphicNovel.requests.AssociateToLibraryRequest;
import com.freebds.backend.common.web.libraryContent.requests.LibraryContentUpdateRequest;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.model.LibrarySerieContent;
import com.freebds.backend.model.Review;
import com.freebds.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibraryContentServiceImpl implements LibraryContentService {

    private final LibraryContentRepository libraryContentRepository;
    private final LibrarySerieContentRepository librarySerieContentRepository;
    private final GraphicNovelRepository graphicNovelRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public LibraryContent getByGraphicNovelId(ContextResource contextResource, Long graphicNovelId) {
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findFirstByLibrary_IdAndGraphicNovel_Id(contextResource.getLibrary().getId(), graphicNovelId);

        if(optionalLibraryContent.isPresent()){
            return optionalLibraryContent.get();
        } else{
            throw new EntityNotFoundException(graphicNovelId, "Library content, on Graphic Novel");
        }
    }

    /**
     * Add a graphic novel in a library
     * @param contextResource the context
     * @param associateToLibraryRequest the graphic novel to associate
     * @return the library graphic novel added in the library
     */
    @Override
    public LibraryContent addGraphicNovelToCollection(ContextResource contextResource, AssociateToLibraryRequest associateToLibraryRequest) {
        //-----------------------------------------------------------------------------
        // Check input parameters
        //-----------------------------------------------------------------------------
        // Check if the graphic novel to add exists
        Optional<GraphicNovel> optionalGraphicNovel = this.graphicNovelRepository.findById(associateToLibraryRequest.getGraphicNovelId());
        GraphicNovel graphicNovel;
        if(optionalGraphicNovel.isPresent()) {
            graphicNovel = optionalGraphicNovel.get();
        } else {
            throw new EntityNotFoundException(associateToLibraryRequest.getGraphicNovelId(), "Graphic novel ID, on Graphic novel");
        }

        // Check if the graphic novel is already associated to the library
        Optional<LibraryContent> optionalLibraryContent =
                this.libraryContentRepository.findFirstByLibrary_IdAndGraphicNovel_Id(
                        associateToLibraryRequest.getLibraryId(),
                        associateToLibraryRequest.getGraphicNovelId()
                );
        if(optionalLibraryContent.isPresent()) {
            throw new FreeBdsApiException(
                    "Cet album est déjà associé à la collection!",
                    "library id / graphic novel id => " +
                            associateToLibraryRequest.getLibraryId() + " / " +
                            associateToLibraryRequest.getGraphicNovelId()
            );
        }

        // Get the library serie if already exists
        LibrarySerieContent librarySerieContent;
        List<LibrarySerieContent> librarySerieContents = this.librarySerieContentRepository.findLibrarySerie(
                associateToLibraryRequest.getLibraryId(),
                graphicNovel.getSerie().getId()
        );
        // Multiple records => Error in db
        if(librarySerieContents.size() > 1) {
            throw new FreeBdsApiException(
                    "Occurrences multiples dans la collection séries pour une série donnée.",
                    "library id / serie id => " +
                            associateToLibraryRequest.getLibraryId() + " / " +
                            graphicNovel.getSerie().getId()
            );
        }
        // Get the library serie id if already exists ...
        if(librarySerieContents.size() == 1) {
            librarySerieContent = librarySerieContents.get(0);
        } else {
            // ... Or create one if doesn't exists yet
            librarySerieContent = new LibrarySerieContent().builder()
                    .library(contextResource.getLibrary())
                    .serie(graphicNovel.getSerie())
                    .isFavorite(false)
                    .libraryContents(null)
                    .reviews(null)
                    .creationDate(LocalDateTime.now())
                    .creationUser(contextResource.getUser().getUsername())
                    .lastUpdateDate(LocalDateTime.now())
                    .lastUpdateUser(contextResource.getUser().getUsername())
                    .build();
            // Save in db
            this.librarySerieContentRepository.saveAndFlush(librarySerieContent);
        }

        //-----------------------------------------------------------------------------
        // Add the graphic novel to the library
        //-----------------------------------------------------------------------------
        LibraryContent libraryContent = LibraryContent.builder()
                .library(contextResource.getLibrary())
                .graphicNovel(graphicNovel)
                .librarySerieContent(librarySerieContent)
                .isFavorite(false)
                .isNumeric(false)
                .isPhysical(false)
                .isWishlist(false)
                .localStorage(null)
                .creationDate(LocalDateTime.now())
                .creationUser(contextResource.getUser().getUsername())
                .lastUpdateDate(LocalDateTime.now())
                .lastUpdateUser(contextResource.getUser().getUsername())
                .build();

        switch (associateToLibraryRequest.getFormat()) {
            case 2:
                libraryContent.setIsPhysical(true);
                break;
            case 3:
                libraryContent.setIsWishlist(true);
                break;
            default:
                libraryContent.setIsNumeric((true));
        }

        return this.libraryContentRepository.saveAndFlush(libraryContent);
    }

    /**
     * Update a library content
     * @param contextResource the context
     * @param libraryContentUpdateRequest the library content infos to update
     * @return the library graphic novel added in the library
     */
    @Override
    public LibraryContent updateLibraryContent(ContextResource contextResource, LibraryContentUpdateRequest libraryContentUpdateRequest) {
        // Check if the requested library content exists
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findById(libraryContentUpdateRequest.getLibraryContentId());
        if(! optionalLibraryContent.isPresent()) {
            throw new EntityNotFoundException(libraryContentUpdateRequest.getLibraryContentId(), "Library content Id, on LibraryContent");
        }
        LibraryContent lc = optionalLibraryContent.get();

        // Check if the associate keys are valid
        if(lc.getLibrary().getId() != libraryContentUpdateRequest.getLibraryId()) {
            throw new FreeBdsApiException(
                    "Incohérence dans la clé library id",
                    "Library id / requested library id" +
                            lc.getLibrary().getId() + " / " + libraryContentUpdateRequest.getLibraryId()
            );
        }

        // Update the requested library content
        lc.setIsNumeric(libraryContentUpdateRequest.isNumeric());
        lc.setIsPhysical(libraryContentUpdateRequest.isPaper());
        lc.setIsWishlist(libraryContentUpdateRequest.isWishlist());
        lc.setLastUpdateDate(LocalDateTime.now());
        lc.setLastUpdateUser(contextResource.getUser().getUsername());
        lc = this.libraryContentRepository.saveAndFlush(lc);

        return lc;
    }

    @Override
    @Transactional
    public boolean deleteLibraryContent(ContextResource contextResource, Long libraryContentId) {
        // Check if the requested library content exists
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findById(libraryContentId);
        if(! optionalLibraryContent.isPresent()) {
            throw new EntityNotFoundException(libraryContentId, "Library content Id, on LibraryContent");
        }
        LibraryContent lc = optionalLibraryContent.get();

        // Delete associated reviews
        // TODO : change Set<Review> on delete cascade.ALL
        for(Review r : lc.getReviews()) {
            this.reviewRepository.delete(r);
        }
        // Delete library content
        this.libraryContentRepository.deleteById(libraryContentId);

        return true;
    }
}
