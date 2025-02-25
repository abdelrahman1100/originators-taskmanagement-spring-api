package com.example.to_do_list.service;

import com.example.to_do_list.DTO.JWTDto;
import com.example.to_do_list.DTO.UserDto;
import com.example.to_do_list.model.User;
import com.example.to_do_list.model.UserDetailsImpl;
import com.example.to_do_list.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> register(UserDto request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        if (request.getUsername().isEmpty() || request.getPassword().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password is empty");
        if (request.getUsername().length() < 4 || request.getPassword().length() < 6)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or password is too short");

        User user = new User();
        user.setUserName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new JWTDto(token, userDetails.getId(), userDetails.getUsername()));
    }

    public ResponseEntity<?> authenticate(UserDto request) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = UserDetailsImpl.buildFromDto(request);

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new JWTDto(token, userDetails.getId(), userDetails.getUsername()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
