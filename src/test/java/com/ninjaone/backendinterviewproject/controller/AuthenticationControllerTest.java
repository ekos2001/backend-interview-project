package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.model.User;
import com.ninjaone.backendinterviewproject.security.JwtToken;
import com.ninjaone.backendinterviewproject.security.JwtTokenService;
import com.ninjaone.backendinterviewproject.service.ServiceTypeService;
import com.ninjaone.backendinterviewproject.service.UserDetailsSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class, JwtTokenService.class, UserDetailsSecurityService.class})
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
class AuthenticationControllerTest {
    final static String BASE_URL = "/api/v1/authenticate";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserDetailsSecurityService userDetailsSecurityService;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    JwtTokenService jwtTokenService;
    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(value = "spring")
    void authenticate() throws Exception {
        JwtToken expectedResponse = new JwtToken("abc");
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken("user", "user"));
        when(jwtTokenService.generateToken(any())).thenReturn(expectedResponse);
        String sampleEntityString = objectMapper.writeValueAsString(new User("user", "user"));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(sampleEntityString))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
    }
}