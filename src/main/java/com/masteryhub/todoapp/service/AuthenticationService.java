package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.AuthenticationResponseDto;
import com.masteryhub.todoapp.dto.LoginDto;
import com.masteryhub.todoapp.dto.MessageDto;
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

  public ResponseEntity<MessageDto> registerUser(RegisterDto registerDto, BindingResult result) {
    List<String> errors =
        result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
    if (!errors.isEmpty()) {
      MessageDto message = new MessageDto();
      message.setMessage(String.join("\n", errors));
      return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    if (userRepository.existsByUsername(registerDto.getUsername())) {
      MessageDto message = new MessageDto();
      message.setMessage("Username is already taken!");
      return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    if (userRepository.existsByEmail(registerDto.getEmail())) {
      MessageDto message = new MessageDto();
      message.setMessage("Email is already taken!");
      return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    UserEntity user = new UserEntity();
    user.setUsername(registerDto.getUsername());
    user.setEmail(registerDto.getEmail());
    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
    userRepository.save(user);
    MessageDto message = new MessageDto();
    message.setMessage("User Registered Successfully!");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  public ResponseEntity<AuthenticationResponseDto> authenticateUser(LoginDto loginDto) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String token = jwtGenerator.generateToken(userDetails);
    return new ResponseEntity<>(new AuthenticationResponseDto(token), HttpStatus.OK);
  }

  public ResponseEntity<MessageDto> logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    user.get().setTokenVersion((user.get().getTokenVersion() + 1) % 1024);
    userRepository.save(user.get());
    MessageDto message = new MessageDto();
    message.setMessage("User Logged Out Successfully!");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }
}
