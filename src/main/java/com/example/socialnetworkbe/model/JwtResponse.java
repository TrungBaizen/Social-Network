package com.example.socialnetworkbe.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
// Đại điện cho token trả về
public class JwtResponse {
    private Long id;
    private String token;
    private String type = "Bearer";
    private String email;
    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, Long id, String email, Collection<? extends GrantedAuthority> roles) {
        this.token = accessToken;
        this.email = email;
        this.roles = roles;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }
}