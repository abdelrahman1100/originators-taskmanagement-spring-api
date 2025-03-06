package com.example.to_do_list.controllers;


import com.example.to_do_list.dto.LoginDTO;
import com.example.to_do_list.dto.RegisterDTO;
import com.example.to_do_list.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;


    // TODO: authService to be authenticationService
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        return authService.authenticateUser(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDto) {
        return authService.registerUser(registerDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        return authService.logout(token);
    }
}
