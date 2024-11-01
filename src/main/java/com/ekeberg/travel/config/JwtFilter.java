package com.ekeberg.travel.config;

import com.ekeberg.travel.service.JWTService;
import com.ekeberg.travel.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * java/com/ekeberg/travel/config/JwtFilter.java
 *
 * Filters incoming requests to authenticate the user based on the JWT token in the Authorization header.
 *
 * The JwtFilter is configured to:
 *
 *     Extract the JWT token from the Authorization header.
 *     Validate the token and retrieve the username.
 *     Load the user's details using MyUserDetailsService.
 *     If valid, create an UsernamePasswordAuthenticationToken and set it in SecurityContextHolder.
 *
 * @author https://github.com/ditlef9
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Log the Authorization header
        System.out.println("JwtFilter.doFilterInternal()::Authorization Header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractEmail(token);  // Extract the username from token

            // Log the token and username
            System.out.println("JwtFilter.doFilterInternal()::Extracted Token: " + token);
            System.out.println("JwtFilter.doFilterInternal()::Extracted Username: " + username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Load user details from the service
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

                // Log whether the user is found
                System.out.println("JwtFilter.doFilterInternal()::Loaded UserDetails: " + (userDetails != null ? userDetails.getUsername() : "User not found"));

                if (jwtService.validateToken(token, userDetails)) {
                    // Log the successful validation of the token
                    System.out.println("JwtFilter.doFilterInternal()::Token validated successfully for user: " + username);

                    // Set the authentication object in the SecurityContextHolder
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);  // Important step

                    // Log the authentication object
                    System.out.println("JwtFilter.doFilterInternal()::Authentication set in SecurityContext: " + authToken);
                } else {
                    // Log token validation failure
                    System.out.println("JwtFilter.doFilterInternal()::Token validation failed for user: " + username);
                }
            } catch (Exception e) {
                // Log any exception encountered during user loading or token validation
                System.out.println("JwtFilter.doFilterInternal()::Error loading user or validating token: " + e.getMessage());
            }
        } else {
            // Log if the username is null or no Authentication was found
            if (username == null) {
                System.out.println("JwtFilter.doFilterInternal()::No username extracted from token.");
            } else {
                System.out.println("JwtFilter.doFilterInternal()::Authentication already set in SecurityContext.");
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
