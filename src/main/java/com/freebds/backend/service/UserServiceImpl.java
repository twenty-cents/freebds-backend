package com.freebds.backend.service;

import com.freebds.backend.common.web.user.requests.ProfileUpdateRequest;
import com.freebds.backend.common.web.user.resources.ProfileUpdateResponse;
import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.exception.FreeBdsApiException;
import com.freebds.backend.model.User;
import com.freebds.backend.repository.UserLibraryRepository;
import com.freebds.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserLibraryRepository userLibraryRepository;

    /**
     * Get a user
     * @return the user
     */
    @Override
    public User getUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new EntityNotFoundException(userId, "User");
        }
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new EntityNotFoundException(username, "User");
        }
    }

    /**
     * Update user's profile
     * @param profileUpdateRequest the user profile to update
     * @return the user's profile updated
     */
    @Override
    public ProfileUpdateResponse updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        // Retrieve current user
        String username ="";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = this.getByUsername(username);

        // Check if requested user is the current authenticated user
        if(user.getId() != profileUpdateRequest.getId()) {
            throw new FreeBdsApiException(
                    "Contexte invalide",
                     "user id / requested user id " + user.getId() + " / " + profileUpdateRequest.getId()
            );
        }

        // Check for email, mandatory zone
        if(profileUpdateRequest.getEmail().equals("")){
            throw new FreeBdsApiException(
                    "Aucune adresse email renseignée",
                    ""
            );
        }

        // No check for avatar value, since it's not a mandatory info

        // Save updated user
        user.setAvatar(profileUpdateRequest.getAvatar());
        user = this.userRepository.saveAndFlush(user);

        ProfileUpdateResponse profileUpdateResponse = new ProfileUpdateResponse().builder()
                .id(user.getId())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .firstname(user.getFirstname())
                .lastname((user.getLastname()))
                .build();

        return profileUpdateResponse;
    }

}
