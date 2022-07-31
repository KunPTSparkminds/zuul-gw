package com.example.demo.model;

import java.time.Instant;
import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class User implements UserDetails, CredentialsContainer {
    /**
     * 
     */
    private static final long serialVersionUID = -5931354324054039687L;
    private Long userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;
    private boolean authenticated;
    private boolean blocked;
    private String role;
    private String ipAdress;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;
    private String phoneNumber;
    private String countryCode;
    private Integer blockedCount;
    private Instant blockedTime;
    private String preferredLanguage;
    private String fullName;
    private Long roleId;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
