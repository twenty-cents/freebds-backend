package com.freebds.backend.service;


import com.freebds.backend.model.Library;
import com.freebds.backend.model.LibrarySerieContent;
import com.freebds.backend.model.Serie;
import com.freebds.backend.model.User;
import com.freebds.backend.repository.AuthorRepository;
import com.freebds.backend.repository.LibrarySerieContentRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibrarySerieContentServiceImpl implements LibrarySerieContentService {

    private final UserService userService;
    private final LibraryService libraryService;
    private final LibrarySerieContentRepository librarySerieContentRepository;
    private final SerieRepository serieRepository;
    private final AuthorRepository authorRepository;

    @Override
    public boolean checkIfExist(Long serieId, Long libraryId) {
        Long count = this.librarySerieContentRepository.checkIfExist(serieId, libraryId);
        if(count == 0)
            return false;
        else
            return true;
    }

    /**
     * Find series within a library
     * @param titleStartingWith : the title to get
     * @param pageable : the page to get
     * @return a page of series
     */
    @Override
    public Page<Serie> getAllSeries(String titleStartingWith, Pageable pageable) {
        Library library = this.libraryService.getCurrentLibrary();
        if(titleStartingWith != null)
            titleStartingWith = titleStartingWith.toLowerCase();
        return this.librarySerieContentRepository.findAllSeriesGag(library.getId(), titleStartingWith, pageable);
    }

    @Override
    public LibrarySerieContent addLibrarySerie(Serie serie, Library library) {
        User user = this.userService.getCurrentUser();

        LibrarySerieContent librarySerieContent = new LibrarySerieContent()
                .builder()
                .library(library)
                .serie(serie)
                .isFavorite(false)
                .creationDate(LocalDateTime.now())
                .creationUser(user.getUsername())
                .lastUpdateDate(LocalDateTime.now())
                .lastUpdateUser(user.getUsername())
                .build();

        return this.librarySerieContentRepository.saveAndFlush(librarySerieContent);
    }

}
