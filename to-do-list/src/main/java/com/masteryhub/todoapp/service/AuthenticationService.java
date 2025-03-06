package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.AuthResponseDTO;
import com.masteryhub.todoapp.dto.LoginDTO;
import com.masteryhub.todoapp.dto.RegisterDTO;
import com.masteryhub.todoapp.models.BlacklistedTokens;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.TokenRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
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

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<String> registerUser(RegisterDTO registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.CONFLICT);
        }
        if (registerDto.getUsername().length() < 3 || registerDto.getUsername().length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be between 3 and 20 characters");
        }
        if (registerDto.getPassword().length() < 6 || registerDto.getPassword().length() > 40) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be between 6 and 40 characters");
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    public ResponseEntity<?> authenticateUser(LoginDTO loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> logout(String token) {
        token = token.substring(7);
        BlacklistedTokens blacklistedToken = new BlacklistedTokens(token);
        tokenRepository.save(blacklistedToken);
        return new ResponseEntity<>("User logged out successfully!", HttpStatus.OK);
    }

}
