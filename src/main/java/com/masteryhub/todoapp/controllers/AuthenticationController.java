package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.AuthenticationResponseDto;
import com.masteryhub.todoapp.dto.LoginDto;
import com.masteryhub.todoapp.dto.RegisterDto;
import com.masteryhub.todoapp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginDto loginDto) {
    return authenticationService.authenticateUser(loginDto);
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
    return authenticationService.registerUser(registerDto);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
    return authenticationService.logout(token);
  }

  @GetMapping("/google")
  public void redirectToGoogleAuth(HttpServletResponse response) throws IOException {
    response.sendRedirect("/oauth2/authorization/google");
  }
}
