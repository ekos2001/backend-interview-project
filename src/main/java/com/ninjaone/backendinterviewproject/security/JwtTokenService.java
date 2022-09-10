package com.ninjaone.backendinterviewproject.security;

import com.ninjaone.backendinterviewproject.exception.GenericApiException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {
    @Value("${app.jwt.expiration}")
    private String expiration;
    @Value("${app.jwt.secret}")
    private String secret;

    public JwtToken generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, authentication.getName());
    }

    private JwtToken createToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(LocalDateTime.now()
                        .plusMinutes(Long.valueOf(expiration))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
        return new JwtToken(token);
    }

    private String getSubject(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUsername(String token) throws GenericApiException {
        try {
            return getSubject(token);
        } catch (Exception ex) {
            throw new GenericApiException("Token is not valid", ex);
        }
    }
}
