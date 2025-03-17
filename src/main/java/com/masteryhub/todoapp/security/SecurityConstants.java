package com.masteryhub.todoapp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

  private final String secretKey;
  private final long jwtExpiration;

  public SecurityConstants(
      @Value("${security.jwt.secret}") String secretKey,
      @Value("${security.jwt.expiration}") long jwtExpiration) {
    this.secretKey = secretKey;
    this.jwtExpiration = jwtExpiration;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public long getJwtExpiration() {
    return jwtExpiration;
  }
}
