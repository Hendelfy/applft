package com.example.applfttest.filter;

import com.example.applfttest.domain.User;
import com.example.applfttest.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class SimplyAuthFilter extends OncePerRequestFilter {
    private final UserService userService;

    public SimplyAuthFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationToken != null) {
            User user = userService.getByToken(authorizationToken);
            if (user != null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getId(), authorizationToken, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }
}
