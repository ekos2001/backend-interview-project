package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.UserRepository;
import com.ninjaone.backendinterviewproject.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsSecurityServiceTest {
    final static String USER_NAME = "user";
    final static String PASSWORD = "password";
    @InjectMocks
    private UserDetailsSecurityService userDetailsSecurityService;
    @Mock
    private UserRepository userRepository;
    private User userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new User(USER_NAME, PASSWORD);
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findById(USER_NAME)).thenReturn(Optional.of(userEntity));
        org.springframework.security.core.userdetails.User expectedUser =
                new org.springframework.security.core.userdetails.User(userEntity.getLogin(), userEntity.getPassword(), new ArrayList<>());
        assertEquals(expectedUser, userDetailsSecurityService.loadUserByUsername(USER_NAME));
    }
}