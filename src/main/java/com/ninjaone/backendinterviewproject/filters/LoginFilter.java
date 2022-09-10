package com.ninjaone.backendinterviewproject.filters;

import com.ninjaone.backendinterviewproject.security.JwtTokenService;
import com.ninjaone.backendinterviewproject.service.UserDetailsSecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class LoginFilter extends OncePerRequestFilter {
    private JwtTokenService jwtTokenService;
    private UserDetailsSecurityService userDetailsSecurityService;

    public LoginFilter(JwtTokenService jwtTokenService, UserDetailsSecurityService userDetailsSecurityService) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsSecurityService = userDetailsSecurityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getToken(request);
        if (jwtToken != null) {
            authenticateUser(jwtToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String jwtToken = null;
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
        }
        return jwtToken;
    }

    private void authenticateUser(String jwtToken) {
        String userName = jwtTokenService.getUsername(jwtToken);
        UserDetails userDetails = userDetailsSecurityService.loadUserByUsername(userName);
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("User {} authenticated as {}", userName, userDetails.getUsername());
    }
}
