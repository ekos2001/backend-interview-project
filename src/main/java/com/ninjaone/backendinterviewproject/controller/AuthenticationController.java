package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.exception.GenericApiException;
import com.ninjaone.backendinterviewproject.model.User;
import com.ninjaone.backendinterviewproject.security.JwtToken;
import com.ninjaone.backendinterviewproject.security.JwtTokenService;
import com.ninjaone.backendinterviewproject.service.UserDetailsSecurityService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/authenticate")
public class AuthenticationController {

    private UserDetailsSecurityService userDetailsSecurityService;
    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;

    public AuthenticationController(UserDetailsSecurityService userDetailsSecurityService, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.userDetailsSecurityService = userDetailsSecurityService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping
    public JwtToken authenticate(@RequestBody @Valid User user) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
            );
            return jwtTokenService.generateToken(authentication);
        } catch (AuthenticationException e) {
            throw new GenericApiException("Invalid credentials", e);
        }
    }
}
