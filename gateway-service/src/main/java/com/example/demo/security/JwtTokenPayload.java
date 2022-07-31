package com.example.demo.security;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class JwtTokenPayload {
    private String role;
    private boolean active;
    private Long userId;
    private String email;
    private boolean authenticated;
    List<String> authorities;
    private boolean isRefreshToken;
    private Date expiration;
}
