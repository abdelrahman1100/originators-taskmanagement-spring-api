package com.masteryhub.todoapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masteryhub.todoapp.handlers.CustomOAuth2SuccessHandler;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.JwtAuthEntryPoint;
import com.masteryhub.todoapp.security.JwtAuthenticationFilter;
import com.masteryhub.todoapp.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthEntryPoint authEntryPoint;
  private final CustomOAuth2SuccessHandler successHandler;

  @Autowired
  public SecurityConfig(
      JwtAuthEntryPoint authEntryPoint, @Lazy CustomOAuth2SuccessHandler successHandler) {
    this.authEntryPoint = authEntryPoint;
    this.successHandler = successHandler;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/auth/**",
                        "/api/**",
                        "/swagger-ui/**",
                        "/api-docs/**",
                        "/v3/api-docs/**",
                        "/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(oauth2 -> oauth2.successHandler(successHandler).failureUrl("/login"))
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Bean
  public CustomOAuth2SuccessHandler customOAuth2SuccessHandler(
      UserRepository userRepository, JwtGenerator jwtGenerator, ObjectMapper objectMapper) {
    return new CustomOAuth2SuccessHandler(userRepository, jwtGenerator, objectMapper);
  }
}
