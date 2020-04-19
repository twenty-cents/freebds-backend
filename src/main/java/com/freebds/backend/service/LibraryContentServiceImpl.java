package com.freebds.backend.service;

import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.GraphicNovel;
import com.freebds.backend.model.Library;
import com.freebds.backend.model.LibraryContent;
import com.freebds.backend.model.User;
import com.freebds.backend.repository.AuthorRepository;
import com.freebds.backend.repository.LibraryContentRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibraryContentServiceImpl implements LibraryContentService {

    private final LibraryContentRepository libraryContentRepository;

    private final UserService userService;
    private final LibraryService libraryService;
    private final LibrarySerieContentService librarySerieContentService;
    private final GraphicNovelService graphicNovelService;
    private final SerieRepository serieRepository;
    private final AuthorRepository authorRepository;


    /**
     * Add a graphic novel in the current user collection
     * @param graphicNovelId the graohic novel id to add
     * @param format the specific format of the graphic novel into the collection
     * @return the added graphic novel in the current collection
     */
    @Override
    public LibraryContent addGraphicNovelToCollection(Long graphicNovelId, int format) {
        User user = this.userService.getCurrentUser();
        Library library = this.libraryService.getCurrentLibrary();
        GraphicNovel graphicNovel = this.graphicNovelService.getGraphicNovelById(graphicNovelId);

        // Add the serie in the collection if needed
        if(this.librarySerieContentService.checkIfExist(graphicNovel.getSerie().getId(), library.getId()) == false) {
            this.librarySerieContentService.addLibrarySerie(graphicNovel.getSerie(), library);
        }

        // Add the graphic novel in the collection
        LibraryContent libraryContent = LibraryContent.builder()
            .library(library)
                .graphicNovel(graphicNovel)
                .isFavorite(false)
                .isNumeric(false)
                .isPhysical(false)
                .isWishlist(false)
                .localStorage(null)
                .creationDate(LocalDateTime.now())
                .creationUser(user.getUsername())
                .lastUpdateDate(LocalDateTime.now())
                .lastUpdateUser(user.getUsername())
                .build();

        switch (format) {
            case 0:
            case 1:
                libraryContent.setIsNumeric(true);
                break;
            case 2:
                libraryContent.setIsPhysical(true);
                break;
            case 3:
                libraryContent.setIsWishlist(true);
                break;
            default:
                libraryContent.setIsNumeric((true));
        }

        return this.libraryContentRepository.save(libraryContent);
    }

    @Override
    public LibraryContent getByGraphicNovelId(Long graphicNovelId) {
        Optional<LibraryContent> optionalLibraryContent = this.libraryContentRepository.findFirstByGraphicNovel_Id(graphicNovelId);

        if(optionalLibraryContent.isPresent()){
            return optionalLibraryContent.get();
        } else{
            throw new EntityNotFoundException(graphicNovelId, "Library content, on Graphic Novel");
        }
    }

}
