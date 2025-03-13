package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.AuthenticationResponseDto;
import com.masteryhub.todoapp.dto.LoginDto;
import com.masteryhub.todoapp.dto.RegisterDto;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
import com.masteryhub.todoapp.security.UserDetailsImpl;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public ResponseEntity<String> registerUser(RegisterDto registerDto) {
    if (registerDto.getUsername().isEmpty() || registerDto.getPassword().isEmpty()) {
      return new ResponseEntity<>("Username and Password can't be empty!", HttpStatus.BAD_REQUEST);
    }
    if (registerDto.getUsername().length() < 4) {
      return new ResponseEntity<>("Username is too short", HttpStatus.BAD_REQUEST);
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
    user.get().set__v(user.get().get__v() + 1);
    userRepository.save(user.get());
    return new ResponseEntity<>("User logged out successfully!", HttpStatus.OK);
  }
}
