package com.masteryhub.todoapp.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masteryhub.todoapp.dto.AuthenticationResponseDto;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
import com.masteryhub.todoapp.security.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserRepository userRepository;
  private final JwtGenerator jwtGenerator;
  private final ObjectMapper objectMapper;

  @Autowired
  public CustomOAuth2SuccessHandler(CustomOAuth2SuccessHandler other) {
    this.userRepository = other.userRepository;
    this.jwtGenerator = other.jwtGenerator;
    this.objectMapper = other.objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String name = oAuth2User.getAttribute("name");
    String email = oAuth2User.getAttribute("email");

    UserEntity user =
        userRepository
            .findByEmail(email)
            .orElseGet(
                () -> {
                  UserEntity newUser = new UserEntity();
                  newUser.setUsername(name);
                  newUser.setEmail(email);
                  return userRepository.save(newUser);
                });

    UserDetailsImpl userDetails = UserDetailsImpl.build(user);

    Authentication auth =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);

    String token = jwtGenerator.generateToken(userDetails);

    AuthenticationResponseDto authResponse = new AuthenticationResponseDto(token);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(authResponse));
    response.getWriter().flush();
  }
}
