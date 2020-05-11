package com.freebds.backend.service;


import com.freebds.backend.common.web.serie.resources.SerieResource;
import com.freebds.backend.mapper.SerieMapper;
import com.freebds.backend.model.Library;
import com.freebds.backend.model.Serie;
import com.freebds.backend.repository.AuthorRepository;
import com.freebds.backend.repository.LibrarySerieContentRepository;
import com.freebds.backend.repository.SerieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibrarySerieContentServiceImpl implements LibrarySerieContentService {

    private final UserService userService;
    private final LibraryService libraryService;
    private final LibrarySerieContentRepository librarySerieContentRepository;
    private final SerieRepository serieRepository;
    private final AuthorRepository authorRepository;

    /**
     * Find series within a library
     * @param titleStartingWith : the title to get
     * @param pageable : the page to get
     * @return a page of series
     */
    @Override
    public Page<SerieResource> getAllSeries(String titleStartingWith, Pageable pageable) {
        Library library = this.libraryService.getCurrentLibrary();
        if(titleStartingWith != null)
            titleStartingWith = titleStartingWith.toLowerCase();

        Page<Serie> series = this.librarySerieContentRepository.findAllSeries(library.getId(), titleStartingWith, pageable);
        return series.map(serie -> SerieMapper.INSTANCE.toResource(serie));
    }

}
