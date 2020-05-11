package com.freebds.backend.service;

import com.freebds.backend.common.web.user.requests.ProfileUpdateRequest;
import com.freebds.backend.common.web.user.resources.ProfileUpdateResponse;
import com.freebds.backend.model.User;
import org.springframework.stereotype.Service;


@Service
public interface UserService {

    User getUser(Long userId);

    User getByUsername(String username);

    ProfileUpdateResponse updateProfile(ProfileUpdateRequest profileUpdateRequest);

}
