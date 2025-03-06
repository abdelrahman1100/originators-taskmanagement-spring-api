package com.masteryhub.todoapp.controllers;


import com.masteryhub.todoapp.dto.LoginDTO;
import com.masteryhub.todoapp.dto.RegisterDTO;
import com.masteryhub.todoapp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        return authenticationService.authenticateUser(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDto) {
        return authenticationService.registerUser(registerDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        return authenticationService.logout(token);
    }

    @GetMapping("/google")
    public void redirectToGoogleAuth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

}
