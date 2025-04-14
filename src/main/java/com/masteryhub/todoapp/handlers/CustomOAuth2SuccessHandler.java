package com.masteryhub.todoapp.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masteryhub.todoapp.dtos.userDto.AuthenticationResponseDto;
import com.masteryhub.todoapp.models.todoModel.TodoEntity;
import com.masteryhub.todoapp.models.userModel.UserEntity;
import com.masteryhub.todoapp.repository.TodoRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtGenerator;
import com.masteryhub.todoapp.security.UserDetailsImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
  private final TodoRepository todoRepository;

  @Autowired
  public CustomOAuth2SuccessHandler(
      UserRepository userRepository,
      JwtGenerator jwtGenerator,
      ObjectMapper objectMapper,
      TodoRepository todoRepository) {
    this.userRepository = userRepository;
    this.jwtGenerator = jwtGenerator;
    this.objectMapper = objectMapper;
    this.todoRepository = todoRepository;
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
                  UserEntity savedUser = userRepository.save(newUser);
                  return savedUser;
                });

    UserDetailsImpl userDetails = UserDetailsImpl.build(user);

    Authentication auth =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);

    String token = jwtGenerator.generateToken(userDetails);

    List<String> todo = user.getTodos();
    List<TodoEntity> todoList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      TodoEntity todoEntity = todoRepository.findById(todo.get(i)).get();
      todoList.add(todoEntity);
    }

    AuthenticationResponseDto authResponse =
        new AuthenticationResponseDto(
            token, userRepository.findByUsername(userDetails.getUsername()).get(), todoList);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(authResponse));
    response.getWriter().flush();
  }
}
