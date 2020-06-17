package com.freebds.backend.controller;


import com.freebds.backend.common.web.payload.requests.LoginRequest;
import com.freebds.backend.common.web.payload.requests.SignupRequest;
import com.freebds.backend.common.web.payload.resources.JwtResponse;
import com.freebds.backend.common.web.payload.resources.MessageResponse;
import com.freebds.backend.model.*;
import com.freebds.backend.repository.LibraryRepository;
import com.freebds.backend.repository.RoleRepository;
import com.freebds.backend.repository.UserLibraryRepository;
import com.freebds.backend.repository.UserRepository;
import com.freebds.backend.security.jwt.JwtUtils;
import com.freebds.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    UserLibraryRepository userLibraryRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAvatar(),
                userDetails.getFirstname(),
                userDetails.getLastname(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setFirstname(signUpRequest.getFirstname());
        user.setLastname(signUpRequest.getLastname());
        user = userRepository.save(user);

        // Create private library
        Library library = new Library();
        library.setName("Collection de " + user.getFirstname());
        library.setDescription("Collection de " + user.getFirstname());
        library = libraryRepository.save(library);

        // Associate private library to user
        UserLibrary userLibrary = new UserLibrary();
        userLibrary.setId(new UserLibraryId(user.getId(), library.getId(), "ADMIN"));
        userLibrary.setUser(user);
        userLibrary.setLibrary(library);
        userLibrary.setRole("ADMIN");
        userLibrary.setActive(true);
        userLibrary = userLibraryRepository.saveAndFlush(userLibrary);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
