package com.example.to_do_list.handlers;

import com.example.to_do_list.dto.AuthResponseDTO;
import com.example.to_do_list.models.UserEntity;
import com.example.to_do_list.repository.UserRepository;
import com.example.to_do_list.security.JwtGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;


public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername(name);
            user.setEmail(email);
            userRepository.save(user);
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(name, null, new ArrayList<>());

        String token = jwtGenerator.generateToken(auth);

        AuthResponseDTO authResponse = new AuthResponseDTO(token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(authResponse));
        response.getWriter().flush();
    }
}