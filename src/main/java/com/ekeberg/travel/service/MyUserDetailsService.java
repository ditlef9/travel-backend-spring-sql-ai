package com.ekeberg.travel.service;

import com.ekeberg.travel.model.UserPrincipal;
import com.ekeberg.travel.model.Users;
import com.ekeberg.travel.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * java/com/ekeberg/travel/service/MyUserDetailsService.java
 *
 * @author https://github.com/ditlef9
 */


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user by email
        Users user = userRepo.findByEmail(email);  // Now using email for loading the user
        if (user == null) {
            System.out.println("MyUserDetailsService.loadUserByUsername()::User Not Found");
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new UserPrincipal(user);
    }
}
