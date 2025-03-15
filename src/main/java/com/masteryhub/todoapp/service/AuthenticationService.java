package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.AuthenticationResponseDto;
import com.masteryhub.todoapp.dto.LoginDto;
import com.masteryhub.todoapp.dto.RegisterDto;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
import com.masteryhub.todoapp.security.UserDetailsImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtGenerator jwtGenerator;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtGenerator jwtGenerator,
      AuthenticationManager authenticationManager) {
    this.jwtGenerator = jwtGenerator;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  public ResponseEntity<String> registerUser(RegisterDto registerDto, BindingResult result) {
    List<String> errors =
        result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
    if (!errors.isEmpty()) {
      return ResponseEntity.badRequest().body(String.join("\n", errors));
    }
    if (userRepository.existsByUsername(registerDto.getUsername())) {
      return new ResponseEntity<>("Username is already taken!", HttpStatus.CONFLICT);
    }
    UserEntity user = new UserEntity();
    user.setUsername(registerDto.getUsername());
    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
    userRepository.save(user);
    return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
  }

  public ResponseEntity<AuthenticationResponseDto> authenticateUser(LoginDto loginDto) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String token = jwtGenerator.generateToken(userDetails);
    return new ResponseEntity<>(new AuthenticationResponseDto(token), HttpStatus.OK);
  }

  public ResponseEntity<String> logout(String token) {
    token = token.substring(7);
    Optional<UserEntity> user =
        userRepository.findByUsername(jwtGenerator.getUsernameFromJWT(token));
    user.get().set__v((user.get().get__v() + 1) % 1024);
    userRepository.save(user.get());
    return new ResponseEntity<>("User logged out successfully!", HttpStatus.OK);
  }
}
