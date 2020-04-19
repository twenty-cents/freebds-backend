package com.freebds.backend.service;

import com.freebds.backend.model.Library;
import com.freebds.backend.model.LibrarySerieContent;
import com.freebds.backend.model.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface LibrarySerieContentService {

    boolean checkIfExist(Long serieId, Long libraryId);

    Page<Serie> getAllSeries(String titleStartingWith, Pageable pageable);

    LibrarySerieContent addLibrarySerie(Serie serie, Library library);


}
