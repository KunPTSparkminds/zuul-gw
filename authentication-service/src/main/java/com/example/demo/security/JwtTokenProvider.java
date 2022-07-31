package com.example.demo.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {


    private Key key;
    private final ObjectMapper parser = new ObjectMapper();

    public static final String JWT_ROLE_KEY = "role";
    public static final String JWT_ACTIVE_KEY = "active";
    public static final String JWT_SESSION_ID_KEY = "sessionId";
    public static final String JWT_USER_ID_KEY = "userId";
    public static final String JWT_EMAIL_KEY = "email";
    public static final String JWT_AUTHENTICATE_STATUS = "authenticated";
    public static final String JWT_AUTHORITY_KEY = "authorities";
    public static final String JWT_IS_REFRESH_KEY = "isRefresh";
    public static final String TWO_FA_TYPE = "twoFaType";

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode("b5LIGTl5MUy7Ut3Cb9MRXKZqMUbssxj818rooyzUzCnQTDYaR8LL1CjboT//8JphkgtKqtn6d0irRxGWAgbg4w==");
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * The method for generate JWT token
     * 
     * @param user
     * @param sessionConfig
     * @param sessionId
     * 
     * @param authenticated
     * @return
     */
    public String createJwtToken(Long expireTime) {
       Date current = new Date();
       Date validity = new Date(current.getTime() + expireTime);
       return Jwts.builder()
       .setSubject("QUANG")
       .claim(JWT_ROLE_KEY, "ROLE_USER")
       .claim(JWT_ACTIVE_KEY, true)
       .claim(JWT_AUTHORITY_KEY, Arrays.asList("ROLE_USER"))
       .setIssuedAt(current)
       .setExpiration(validity)
       .signWith(key, SignatureAlgorithm.HS512)
       .compact();
    }

    public String createJwtTokenAdmin(Long expireTime) {
        Date current = new Date();
        Date validity = new Date(current.getTime() + expireTime);
        return Jwts.builder()
        .setSubject("QUANG")
        .claim(JWT_ROLE_KEY, "ROLE_ADMIN")
        .claim(JWT_ACTIVE_KEY, true)
        .claim(JWT_AUTHORITY_KEY, Arrays.asList("logManagement", "reviewApplicant"))
        .setIssuedAt(current)
        .setExpiration(validity)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
     }

    public String createJwtTokenStaff(Long expireTime) {
        Date current = new Date();
        Date validity = new Date(current.getTime() + expireTime);
        return Jwts.builder()
        .setSubject("QUANG")
        .claim(JWT_ROLE_KEY, "ROLE_STAFF")
        .claim(JWT_ACTIVE_KEY, true)
        .claim(JWT_AUTHORITY_KEY, Arrays.asList("reviewApplicant"))
        .setIssuedAt(current)
        .setExpiration(validity)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
     }

    @SuppressWarnings("unchecked")
    public JwtTokenPayload extractJwtPayload(String jwtToken) {
        JwtTokenPayload payload = new JwtTokenPayload();
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
        payload.setActive(claims.get(JWT_ACTIVE_KEY, Boolean.class));
        payload.setEmail(claims.getSubject());
        payload.setAuthorities(claims.get(JWT_AUTHORITY_KEY, List.class));
        payload.setExpiration(claims.getExpiration());
        return payload;
    }
    
    /**
     * Validate jwt token.
     *
     * @param jwtToken the jwt token
     * @return the {@link TokenValidateType}
     */
    public TokenValidateType validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return TokenValidateType.VALID;
        } catch (SignatureException e) {
            return TokenValidateType.INVALID_SIGNATURE;
        } catch (ExpiredJwtException e) {
            return TokenValidateType.EXPIRED;
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            return TokenValidateType.INVALID_FORMAT;
        }
    }
}