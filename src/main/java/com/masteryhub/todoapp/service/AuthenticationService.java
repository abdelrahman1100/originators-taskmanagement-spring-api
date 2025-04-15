package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dtos.messageDto.MessageDto;
import com.masteryhub.todoapp.dtos.userDto.AuthenticationResponseDto;
import com.masteryhub.todoapp.dtos.userDto.LoginDto;
import com.masteryhub.todoapp.dtos.userDto.RegisterDto;
import com.masteryhub.todoapp.models.todoModel.TodoEntity;
import com.masteryhub.todoapp.models.userModel.UserEntity;
import com.masteryhub.todoapp.repository.TodoRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
import com.masteryhub.todoapp.security.UserDetailsImpl;
import java.util.ArrayList;
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
  private final TodoRepository todoRepository;

  public AuthenticationService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtGenerator jwtGenerator,
      AuthenticationManager authenticationManager,
      TodoRepository todoRepository) {
    this.jwtGenerator = jwtGenerator;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.todoRepository = todoRepository;
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
    if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
      MessageDto message = new MessageDto();
      message.setMessage("Passwords do not match!");
      return new ResponseEntity<>(message, HttpStatus.CONFLICT);
    }
    UserEntity user = new UserEntity();
    user.setUsername(registerDto.getUsername());
    user.setEmail(registerDto.getEmail());
    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
    user.setFullName(registerDto.getFullName());
    user.setPhoneNumber(registerDto.getPhoneNumber());
    user.setAddress(registerDto.getAddress());
    userRepository.save(user);
    MessageDto message = new MessageDto();
    message.setMessage("User Registered Successfully");
    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  public ResponseEntity<AuthenticationResponseDto> authenticateUser(LoginDto loginDto) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String token = jwtGenerator.generateToken(userDetails);
    UserEntity user = userRepository.findByEmail(userDetails.getEmail()).get();
    List<String> todo = user.getTodos();
    List<TodoEntity> todoList = new ArrayList<>();
    if (todo.size() == 0) {
      return new ResponseEntity<>(
          new AuthenticationResponseDto(token, user, todoList), HttpStatus.OK);
    }
    for (int i = 0; i < 5; i++) {
      TodoEntity todoEntity = todoRepository.findById(todo.get(i)).get();
      todoList.add(todoEntity);
    }
    return new ResponseEntity<>(
        new AuthenticationResponseDto(
            token, userRepository.findByUsername(userDetails.getUsername()).get(), todoList),
        HttpStatus.OK);
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
