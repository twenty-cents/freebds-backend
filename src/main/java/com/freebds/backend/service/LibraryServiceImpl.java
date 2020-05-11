package com.freebds.backend.service;

import com.freebds.backend.common.web.library.requests.UserLibraryStatusRequest;
import com.freebds.backend.common.web.library.resources.ILibraryResource;
import com.freebds.backend.common.web.library.resources.IUserLibraryStatusResource;
import com.freebds.backend.common.web.library.resources.LibraryResource;
import com.freebds.backend.common.web.library.resources.UserLibraryStatusResource;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.Library;
import com.freebds.backend.model.User;
import com.freebds.backend.model.UserLibrary;
import com.freebds.backend.model.UserLibraryId;
import com.freebds.backend.repository.LibraryRepository;
import com.freebds.backend.repository.UserLibraryRepository;
import com.freebds.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final UserLibraryRepository userLibraryRepository;

    /**
     * Get the default library associated to a user
     * @param user the user
     * @return the library resource
     * @throws FreeBdsApiException
     */
    @Override
    public LibraryResource getUserLibrary(User user) throws FreeBdsApiException {
        List<UserLibrary> userLibraries = this.userLibraryRepository.findUserLibrary(user.getId(), "ADMIN");
        // Check validity
        if(userLibraries.size() == 0) {
            throw new FreeBdsApiException(
                    "Aucun rôle ADMIN associé à l'utilisateur " + user.getFirstname() + " " + user.getLastname(),
                    ""
            );
        }
        // Build result resource
        LibraryResource libraryResource = new LibraryResource().builder()
                .libraryId(userLibraries.get(0).getLibrary().getId())
                .name(userLibraries.get(0).getLibrary().getName())
                .description(userLibraries.get(0).getLibrary().getDescription())
                .userRole(userLibraries.get(0).getRole())
                .build();

        return libraryResource;
    }

    /**
     * Set the active library associated to a user
     * @param user the user
     * @param libraryId the library to activate
     * @return the library activated
     */
    @Override
    @Transactional
    public LibraryResource setUserLibrary(User user, Long libraryId) {
        // Get actual active library
        Optional<UserLibrary> optionalUserLibrary = this.userLibraryRepository.findFirstByUser_IdAndIsActive(user.getId(), true);
        if(optionalUserLibrary.isPresent() == false){
            throw new FreeBdsApiException(
                    "Aucune collection active pour l'utilisateur " + user.getFirstname() + " " + user.getLastname(),
                    ""
            );
        }
        UserLibrary userLibraryActive = optionalUserLibrary.get();

        // Get user library to update
        optionalUserLibrary = this.userLibraryRepository.findFirstByUser_IdAndLibrary_Id(user.getId(), libraryId);
        if(optionalUserLibrary.isPresent() == false){
            throw new FreeBdsApiException(
                    "Collection invalide pour l'utilisateur " + user.getFirstname() + " " + user.getLastname(),
                    ""
            );
        }
        UserLibrary userLibraryToUpdate = optionalUserLibrary.get();

        // Get the new default library
        Optional<Library> optionalLibrary = this.libraryRepository.findById(libraryId);
        if(optionalLibrary.isPresent() == false){
            throw new EntityNotFoundException(libraryId, "library");
        }
        Library library = optionalLibrary.get();

        // Set inactive the actual user library
        userLibraryActive.setActive(false);
        this.userLibraryRepository.save(userLibraryActive);

        // Set the new active user library
        userLibraryToUpdate.setLibrary(library);
        userLibraryToUpdate.setActive(true);
        userLibraryToUpdate = this.userLibraryRepository.saveAndFlush(userLibraryToUpdate);

        // Build result resource
        LibraryResource libraryResource = new LibraryResource().builder()
                .libraryId(userLibraryToUpdate.getLibrary().getId())
                .name(userLibraryToUpdate.getLibrary().getName())
                .description(userLibraryToUpdate.getLibrary().getDescription())
                .userRole(userLibraryToUpdate.getRole())
                .isActive(userLibraryToUpdate.getActive())
                .build();
        return libraryResource;
    }

    /**
     * Get libraries associated to a user
     * @param user the user to get
     * @return a list of libraries
     */
    @Override
    public List<ILibraryResource> getLibraries(User user) {
        return this.userLibraryRepository.findLibraries((user.getId()));
    }

    /**
     * Get users status on the personal library
     * @param user the user
     * @return
     */
    @Override
    public List<IUserLibraryStatusResource> getUsersStatusOnPersonalLibrary(User user) {
        // Retrieve personal library
        List<UserLibrary> userLibraries = this.userLibraryRepository.findUserLibrary(user.getId(), "ADMIN");
        // Check validity (shouldn't append!)
        if(userLibraries.size() == 0) {
            throw new FreeBdsApiException(
                    "Aucun rôle ADMIN associé à l'utilisateur " + user.getFirstname() + " " + user.getLastname(),
                    ""
            );
        }

        return this.userLibraryRepository.findUsersStatusOnPersonalLibrary(user.getId(), userLibraries.get(0).getLibrary().getId());
    }

    /**
     * Dissociate a user from the current user personal library
     * @param user the current user
     * @param userLibraryStatusRequest the user to dissociate
     * @return the requested user to dissociate
     */
    @Override
    @Transactional
    public UserLibraryStatusResource dissociateUserStatusOnPersonalLibrary(User user, UserLibraryStatusRequest userLibraryStatusRequest) {
        // Get the personal library
        LibraryResource userLibraryResource = this.getUserLibrary(user);

        // Get the user to dissociate
        Optional<UserLibrary> optionalUserLibraryToDissociate = this.userLibraryRepository.findFirstByUser_IdAndLibrary_Id(userLibraryStatusRequest.getUserId(), userLibraryResource.getLibraryId());
        if(optionalUserLibraryToDissociate.isPresent()) {
            UserLibrary userLibraryToDissociate = optionalUserLibraryToDissociate.get();
            // Set default library to user personal library if active
            if (userLibraryToDissociate.getActive() == true) {
                // Find user to dissociate personal library
                List<UserLibrary> dissociatedUserLibraries = this.userLibraryRepository.findUserLibrary(userLibraryToDissociate.getUser().getId(), "ADMIN");
                UserLibrary dissociatedUserLibrary = dissociatedUserLibraries.get(0);
                // Set active personal library
                dissociatedUserLibrary.setActive(true);
                // Save dissociate user
                this.userLibraryRepository.saveAndFlush(dissociatedUserLibrary);
            }
            // Dissociate the user from the current user personal library
            this.userLibraryRepository.delete(userLibraryToDissociate);
        }

        UserLibraryStatusResource userLibraryStatusResource = new UserLibraryStatusResource().builder()
                .userId(userLibraryStatusRequest.getUserId())
                .firstname(userLibraryStatusRequest.getFirstname())
                .lastname(userLibraryStatusRequest.getLastname())
                .status("0")
                .build();
        return userLibraryStatusResource;
    }

    /**
     * Associate a user from the current user personal library
     * @param user the current user
     * @param userLibraryStatusRequest the user to dissociate
     * @return the requested user to dissociate
     */
    @Override
    @Transactional
    public UserLibraryStatusResource associateUserStatusOnPersonalLibrary(User user, UserLibraryStatusRequest userLibraryStatusRequest) {
        // Get the personal library
        LibraryResource userLibraryResource = this.getUserLibrary(user);

        // Get the library to associate
        Optional<Library> optionalLibrary = this.libraryRepository.findById(userLibraryResource.getLibraryId());
        if(optionalLibrary.isPresent() == false){
            throw new EntityNotFoundException(userLibraryResource.getLibraryId(), "library");
        }
        Library library = optionalLibrary.get();

        // Get the use to associate
        Optional<User> optionalUserToAssociate = this.userRepository.findById(userLibraryStatusRequest.getUserId());
        if(optionalUserToAssociate.isPresent() == false) {
            throw new EntityNotFoundException(userLibraryStatusRequest.getUserId(), "user");
        }
        User userToAssociate = optionalUserToAssociate.get();

        UserLibrary userLibrary = new UserLibrary();
        userLibrary.setId(new UserLibraryId(user.getId(), library.getId(), "USER"));
        userLibrary.setUser(userToAssociate);
        userLibrary.setLibrary(library);
        userLibrary.setRole("USER");
        userLibrary.setActive(false);
        userLibrary = this.userLibraryRepository.saveAndFlush(userLibrary);

        UserLibraryStatusResource userLibraryStatusResource = new UserLibraryStatusResource().builder()
                .userId(userToAssociate.getId())
                .firstname(userToAssociate.getFirstname())
                .lastname(userToAssociate.getLastname())
                .status("1")
                .build();
        return userLibraryStatusResource;
    }

    /**
     * Get current user's library
     * @return
     */
    @Override
    public Library getCurrentLibrary() {
        // TODO : to rewrite after security implementation
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
