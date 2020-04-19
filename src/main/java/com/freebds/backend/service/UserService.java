package com.freebds.backend.service;

import com.freebds.backend.model.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    /**
     * Get Authenticated user
     * @return the user
     */
    User getCurrentUser();

    User getUser(Long userId);

    User getByUsername(String username);

}
