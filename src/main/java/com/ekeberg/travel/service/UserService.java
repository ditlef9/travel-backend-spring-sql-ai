package com.ekeberg.travel.service;

import com.ekeberg.travel.model.Users;
import com.ekeberg.travel.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * java/com/ekeberg/travel/service/UserService.java
 *
 * @author https://github.com/ditlef9
 */


@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;

    public Users register(Users user) {
        // Store password securely
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        repo.save(user);
        return user;
    }

    public String verify(Users user) {
        // Authenticate using email and password
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())  // Using email for authentication
        );

        if (authentication.isAuthenticated()) {
            // Fetch the user by email to get userId and name
            Users loggedInUser = repo.findByEmail(user.getEmail());

            // Make sure the user is found in the repository
            if (loggedInUser != null) {
                String email = loggedInUser.getEmail();
                Long userId = loggedInUser.getId();  // Get userId
                String name = loggedInUser.getName();  // Get name

                // Debugging log to verify values
                System.out.println("UserService.verify()::email: " + email + " userId: " + userId + " name: " + name);

                // Pass user details to JWTService to generate the token
                return jwtService.generateToken(email, userId, name);
            }
        }
        return "fail";  // Return failure if authentication is unsuccessful
    }

    public Users getUserByEmail(String email) {
        return repo.findByEmail(email);  // Fetch user details by email
    }
}
