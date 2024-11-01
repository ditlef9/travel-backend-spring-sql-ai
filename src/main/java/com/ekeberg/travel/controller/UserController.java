package com.ekeberg.travel.controller;

import com.ekeberg.travel.model.Users;
import com.ekeberg.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UserController for handling user sign-up and sign-in
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/sign-up")
    public Users register(@RequestBody Users user) {
        System.out.println("UserController.register()::User: " + user.getEmail());
        return service.register(user);  // Registers the user and returns the saved user object
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody Users user) {
        System.out.println("UserController.login()::sign-in request received------------------");
        System.out.println("UserController.login()::User email: " + user.getEmail());
        // System.out.println("UserController.login()::User password: " + user.getPassword());

        // Verify the user and retrieve the token
        String token = service.verify(user);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserController.login()::Invalid credentials.");
        }

        // Return the token as the response
        System.out.println("UserController.login()::Success! Returning token: " + token);
        return ResponseEntity.ok().body(Map.of("token", token)); // Return the token in the response directly
    }
}
