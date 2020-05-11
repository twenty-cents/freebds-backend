package com.freebds.backend.service;

import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContextServiceImpl implements ContextService {

    private final UserService userService;
    private final LibraryService libraryService;

    /**
     * Get current user
     * @return the current user
     */
    @Override
    public User getContext() {
        // Retrieve current user
        String username ="";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return this.userService.getByUsername(username);
    }

    /**
     * Get the context for a role
     * @param contextRequest : the context to get
     * @param roleRequest : the role to check
     * @return the user context
     * @throws com.freebds.backend.exception.FreeBdsApiException if user or library doesn't exist or role is not valid
     */
    @Override
    public ContextResource getContext(String contextRequest, Long libraryId, String roleRequest) {
        // Retrieve current user
        User user = getContext();

        // Check context
        String authorizedContext = "undefined";
        if(contextRequest.equals("referential") || contextRequest.equals("library")){
            authorizedContext = contextRequest;
        }
        if(authorizedContext.equals("undefined")) {
            throw new FreeBdsApiException(
                    "Contexte invalide",
                    "Invalid context " + contextRequest
            );
        }

        // Check credentials
        String userRole = this.libraryService.getUserRole(libraryId, user.getId());
        boolean isRoleRequestValid = false;
        if(roleRequest.equals("USER") && (userRole.equals("USER") || userRole.equals("ADMIN"))) {
            isRoleRequestValid = true;
        }
        if(roleRequest.equals("ADMIN") && userRole.equals("ADMIN")) {
            isRoleRequestValid = true;
        }
        if(! isRoleRequestValid){
            throw new FreeBdsApiException(
                   "Habilitation insuffisante pour l'action demandée.",
                   "Invalid credentials for requests user / library / role " + user.getId() + " / " + libraryId + " / " + roleRequest
            );
        }

        return new ContextResource().builder()
                .user(user)
                .library(this.libraryService.getLibrary(libraryId, user.getId()))
                .context(authorizedContext)
                .userRole(userRole)
                .isValid(true)
                .build();
    }

}
