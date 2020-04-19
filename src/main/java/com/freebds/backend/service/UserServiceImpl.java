package com.freebds.backend.service;

import com.freebds.backend.exception.EntityNotFoundException;
import com.freebds.backend.model.User;
import com.freebds.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Get Authenticated user
     * @return the user
     */
    @Override
    public User getCurrentUser() {
        // TODO: To rewrite after security implementation
        return  this.userRepository.findById(1L).get();
    }

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

}
