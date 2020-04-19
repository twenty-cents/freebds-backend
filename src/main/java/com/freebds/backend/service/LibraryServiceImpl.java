package com.freebds.backend.service;

import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.Library;
import com.freebds.backend.repository.LibraryRepository;
import com.freebds.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;

    /**
     * Get current user's library
     * @return
     */
    @Override
    public Library getCurrentLibrary() {
        // TODO : to rewrite afer security implemntation
        return this.libraryRepository.findById(1L).get();
    }

    /**
     *
     * @param libraryId
     * @param userId
     * @return
     */
    @Override
    public Library getLibrary(Long libraryId, Long userId) {
        Optional<Library> optionalLibrary = libraryRepository.findLibraryByUser(libraryId, userId);
        if (optionalLibrary.isPresent()) {
            return optionalLibrary.get();
        } else {
            throw new FreeBdsApiException(
                    "Aucune collection trouvée, le compte utilisateur n'est pas habilité à cette collection",
                    "Invalid key libraryId / userId : " + libraryId + " / " + userId
            );
        }
    }

    @Override
    public String getUserRole(Long libraryId, Long userId) {
        Optional<String> optionalRole = libraryRepository.findUserRole(libraryId, userId);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        } else {
            throw new FreeBdsApiException(
                    "Aucun rôle trouvé, le compte utilisateur n'est pas habilité à cette collection",
                    "Invalid key libraryId / userId : " + libraryId + " / " + userId
            );
        }
    }

    @Override
    public List<Library> getLibrariesByUser(Long userId) {
        return this.libraryRepository.findLibrariesByUser(userId);
    }
}
