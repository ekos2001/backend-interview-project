package com.ninjaone.backendinterviewproject.security;

import lombok.Getter;

@Getter
public class JwtToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }
}
