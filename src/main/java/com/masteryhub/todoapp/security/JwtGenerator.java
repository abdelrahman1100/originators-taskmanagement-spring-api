package com.masteryhub.todoapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {
  private static final String SECRET_KEY = SecurityConstants.SECRET_KEY;

  public String generateToken(UserDetailsImpl userDetails) {
    String username = userDetails.getUsername();
    Integer versionToken = userDetails.get__v();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

    String token =
        Jwts.builder()
            .setSubject(username)
            .claim("versionToken", versionToken)
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .signWith(getSigningKey())
            .compact();
    return token;
  }

  public String getUsernameFromJWT(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public Integer getVersionToken(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    return claims.get("versionToken", Integer.class);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private SecretKey getSigningKey() {
    byte[] KeyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(KeyBytes);
  }
}
