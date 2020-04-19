package com.freebds.backend.service;

import com.freebds.backend.model.Library;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LibraryService {

    Library getCurrentLibrary();

    Library getLibrary(Long libraryId, Long userId);

    String getUserRole(Long libraryId, Long userId);

    List<Library> getLibrariesByUser(Long userId);
}
