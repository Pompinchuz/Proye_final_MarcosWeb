package com.example.catgameweb.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = null;
        String header = request.getHeader("Authorization");

        log.debug("Request path: {}", request.getRequestURI());
        log.debug("Authorization header: {}", header);

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            log.debug("Token from header: {}", token);
        } else {
            // Leer de cookie si no hay header
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                log.debug("Cookies found: {}", cookies.length);
                for (Cookie cookie : cookies) {
                    log.debug("Cookie name: {}, value: {}", cookie.getName(), cookie.getValue());
                    if ("jwt".equals(cookie.getName())) {
                        token = cookie.getValue();
                        log.debug("Token from cookie: {}", token);
                        break;
                    }
                }
            } else {
                log.debug("No cookies found");
            }
        }

        if (token != null) {
            try {
                String username = jwtUtil.extractUsername(token);
                log.debug("Extracted username from token: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.debug("Loaded user details for: {}", username);

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.debug("Authentication set for user: {}", username);
                    } else {
                        log.warn("Token validation failed for user: {}", username);
                    }
                }
            } catch (Exception e) {
                log.error("Error processing JWT: {}", e.getMessage());
            }
        } else {
            log.debug("No token found in header or cookie");
        }

        chain.doFilter(request, response);
    }
}