package com.freebds.backend.service;

import com.freebds.backend.common.web.serie.resources.SerieResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface LibrarySerieContentService {

    Page<SerieResource> getAllSeries(String titleStartingWith, Pageable pageable);


}
