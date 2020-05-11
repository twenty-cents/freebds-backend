package com.freebds.backend.service;

import com.freebds.backend.common.web.library.requests.UserLibraryStatusRequest;
import com.freebds.backend.common.web.library.resources.ILibraryResource;
import com.freebds.backend.common.web.library.resources.IUserLibraryStatusResource;
import com.freebds.backend.common.web.library.resources.LibraryResource;
import com.freebds.backend.common.web.library.resources.UserLibraryStatusResource;
import com.freebds.backend.model.Library;
import com.freebds.backend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LibraryService {

    LibraryResource getUserLibrary(User user);

    LibraryResource setUserLibrary(User user, Long libraryId);

    List<ILibraryResource> getLibraries(User user);

    List<IUserLibraryStatusResource> getUsersStatusOnPersonalLibrary(User user);

    UserLibraryStatusResource dissociateUserStatusOnPersonalLibrary(User user, UserLibraryStatusRequest userLibraryStatusRequest);

    UserLibraryStatusResource associateUserStatusOnPersonalLibrary(User user, UserLibraryStatusRequest userLibraryStatusRequest);

    Library getCurrentLibrary();

    Library getLibrary(Long libraryId, Long userId);

    String getUserRole(Long libraryId, Long userId);

    List<Library> getLibrariesByUser(Long userId);
}
