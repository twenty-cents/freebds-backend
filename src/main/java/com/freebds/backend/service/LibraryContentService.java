package com.freebds.backend.service;

import com.freebds.backend.model.LibraryContent;
import org.springframework.stereotype.Service;

@Service
public interface LibraryContentService {

    LibraryContent addGraphicNovelToCollection(Long graphicNovelId, int format);

    LibraryContent getByGraphicNovelId(Long graphicNovelId);
}
