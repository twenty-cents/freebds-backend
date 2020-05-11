package com.freebds.backend.controller;

import com.freebds.backend.common.web.user.requests.ProfileUpdateRequest;
import com.freebds.backend.common.web.user.resources.ProfileUpdateResponse;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/users", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final ContextService contextService;
    private final UserService userService;

    @PutMapping("{userId}")
    public ProfileUpdateResponse updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateRequest profileUpdateRequest) {

        return this.userService.updateProfile(profileUpdateRequest);
    }

}
