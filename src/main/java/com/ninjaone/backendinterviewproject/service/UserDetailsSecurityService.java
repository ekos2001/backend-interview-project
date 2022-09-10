package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.UserRepository;
import com.ninjaone.backendinterviewproject.exception.GenericApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
public class UserDetailsSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("Loading user by user name {}", userName);
        var user = userRepository.findById(userName).orElseThrow(() -> new GenericApiException("Invalid credentials"));
        return new User(user.getLogin(), user.getPassword(), new ArrayList<>());
    }
}
