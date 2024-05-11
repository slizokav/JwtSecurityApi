package com.slizokav.JwtSecurityApi.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.slizokav.JwtSecurityApi.security.JWTUtil;
import com.slizokav.JwtSecurityApi.service.PersonUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final PersonUserDetailsService personUserDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonUserDetailsService personUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personUserDetailsService = personUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Невалидный JWT токен");
            } else {
                try {
                    String username = jwtUtil.validateToken(jwt);
                    UserDetails userDetails = personUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Невалидный JWT токен");
                }
            }

        }
        filterChain.doFilter(request, response);
    }
}
