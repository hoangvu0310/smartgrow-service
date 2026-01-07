package com.hoang.smartgrow.config.security;

import com.hoang.smartgrow.common.Const;
import com.hoang.smartgrow.entity.User;
import com.hoang.smartgrow.property.AuthTokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
  private final AuthTokenProperties authTokenProperties;

  private SecretKey getSigningSecretKey() {
    return Keys.hmacShaKeyFor(authTokenProperties.getJwtSecret().getBytes());
  }

  public String generateToken(User user) {
    Key secretKey = getSigningSecretKey();

    Instant currentTime = Instant.now();
    Instant expirationTime = currentTime.plusSeconds(authTokenProperties.getAccessTokenTTL() * 60);

    Map<String, Object> tokenClaims = new HashMap<>();
    tokenClaims.put("userId", user.getUserId());
    tokenClaims.put("role", user.getRole());
    tokenClaims.put("fullName", user.getFullName());

    return Jwts.builder()
        .expiration(Date.from(expirationTime))
        .subject(user.getUsername())
        .issuedAt(Date.from(currentTime))
        .claims(tokenClaims)
        .signWith(secretKey)
        .compact();
  }

  public Boolean validateToken(String token, String tokenType) {
    try {
      Claims payload = getTokenPayload(token);
      return payload != null;
    } catch (Exception e) {
      log.warn("{} token invalid: {}", tokenType, e.getMessage());
      return false;
    }
  }

  public Claims getTokenPayload(@NonNull String token) {
    String rawToken = token.replace("Bearer ", "").trim();

    return Jwts.parser()
        .verifyWith(getSigningSecretKey())
        .build()
        .parseSignedClaims(rawToken)
        .getPayload();

  }
}