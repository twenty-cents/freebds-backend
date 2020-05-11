package com.freebds.backend.service;

import com.freebds.backend.common.web.graphicNovel.requests.AssociateToLibraryRequest;
import com.freebds.backend.common.web.libraryContent.requests.LibraryContentUpdateRequest;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.model.LibraryContent;
import org.springframework.stereotype.Service;

@Service
public interface LibraryContentService {

    LibraryContent getByGraphicNovelId(ContextResource contextResource, Long graphicNovelId);

    LibraryContent addGraphicNovelToCollection(ContextResource contextResource, AssociateToLibraryRequest associateToLibraryRequest);

    LibraryContent updateLibraryContent(ContextResource contextResource, LibraryContentUpdateRequest libraryContentUpdateRequest);

    boolean deleteLibraryContent(ContextResource contextResource, Long libraryContentId);
}
