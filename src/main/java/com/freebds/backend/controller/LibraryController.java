package com.freebds.backend.controller;

import com.freebds.backend.common.web.library.requests.UserLibraryStatusRequest;
import com.freebds.backend.common.web.library.resources.ILibraryResource;
import com.freebds.backend.common.web.library.resources.LibraryResource;
import com.freebds.backend.common.web.library.resources.IUserLibraryStatusResource;
import com.freebds.backend.common.web.library.resources.UserLibraryStatusResource;
import com.freebds.backend.model.User;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/library", produces = { MediaType.APPLICATION_JSON_VALUE })
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class LibraryController {

    private final ContextService contextService;
    private final LibraryService libraryService;

    /**
     * Get the default library associated to a user
     * @return the library resource
     */
    @GetMapping("/main")
    public LibraryResource getUserLibrary() {
        // Get context
        User user = this.contextService.getContext();

        return this.libraryService.getUserLibrary(user);
    }

    /**
     * Set the active library associated to a user
     * @param libraryId the library to activate
     * @return the library activated
     */
    @PostMapping("/active")
    public LibraryResource setActiveLibrary(@RequestBody Long libraryId) {
        // Get context
        User user = this.contextService.getContext();

        return this.libraryService.setUserLibrary(user, libraryId);
    }


    /**
     * Get libraries associated to a user
     * @return a list of libraries
     */
    @GetMapping("")
    public List<ILibraryResource> getLibraries() {
        // Get context
        User user = this.contextService.getContext();

        return this.libraryService.getLibraries(user);
    }

    /**
     * Get users status on the personal library
     * @return a list of users status on the personal library
     */
    @GetMapping("/status")
    public List<IUserLibraryStatusResource> getUsersStatusOnPersonalLibrary() {
        User user = this.contextService.getContext();

        return this.libraryService.getUsersStatusOnPersonalLibrary(user);
    }

    /**
     * Set user status on the personal library
     * @return a user status on the personal library
     */
    @PostMapping("/status/active")
    public UserLibraryStatusResource associateUserStatusOnPersonalLibrary(@RequestBody UserLibraryStatusRequest userLibraryStatusRequest) {
        // Get context
        User user = this.contextService.getContext();

        return this.libraryService.associateUserStatusOnPersonalLibrary(user, userLibraryStatusRequest);
    }

    /**
     * Set user status on the personal library
     * @return a user status on the personal library
     */
    @PostMapping("/status/deactive")
    public UserLibraryStatusResource dissociateUserStatusOnPersonalLibrary(@RequestBody UserLibraryStatusRequest userLibraryStatusRequest) {
        // Get context
        User user = this.contextService.getContext();

        return this.libraryService.dissociateUserStatusOnPersonalLibrary(user, userLibraryStatusRequest);
    }
}
