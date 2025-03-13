package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.AuthenticationResponseDto;
import com.masteryhub.todoapp.dto.LoginDto;
import com.masteryhub.todoapp.dto.RegisterDto;
import com.masteryhub.todoapp.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
  public ResponseEntity<String> register(
      @Valid @RequestBody RegisterDto registerDto, BindingResult result) {
    return authenticationService.registerUser(registerDto, result);
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
