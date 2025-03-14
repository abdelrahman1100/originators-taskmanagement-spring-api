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

  private final SecurityConstants securityConstants;
  private final SecretKey signingKey;

  public JwtGenerator(SecurityConstants securityConstants) {
    this.securityConstants = securityConstants;
    byte[] keyBytes = Decoders.BASE64.decode(securityConstants.getSecretKey());
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(UserDetailsImpl userDetails) {
    System.out.println("SECRET_KEY: " + securityConstants.getSecretKey());
    String username = userDetails.getUsername();
    Integer versionToken = userDetails.get__v();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + securityConstants.getJwtExpiration());

    return Jwts.builder()
        .setSubject(username)
        .claim("versionToken", versionToken)
        .setIssuedAt(currentDate)
        .setExpiration(expireDate)
        .signWith(signingKey)
        .compact();
  }

  public String getUsernameFromJWT(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public Integer getVersionToken(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    return claims.get("versionToken", Integer.class);
  }

  public boolean validateToken(String token) {
    try {
      Claims claims =
          Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
      return !claims.getExpiration().before(new Date());
    } catch (Exception ex) {
      return false;
    }
  }
}
