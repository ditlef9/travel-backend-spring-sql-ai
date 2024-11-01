/**
 * java/com/ekeberg/travel/interest/InterestController.java
 *
 * REST API Controller for managing user interests.
 * Provides endpoints for creating, listing, updating, and deleting interests.
 * Requires user authentication to access.
 *
 * Author: ditlef9
 */

package com.ekeberg.travel.controller;

import com.ekeberg.travel.model.Interest;
import com.ekeberg.travel.model.UserPrincipal;
import com.ekeberg.travel.model.Users;
import com.ekeberg.travel.service.InterestService;
import com.ekeberg.travel.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/interests")
public class InterestController {

    private final InterestService interestService;
    private final UserService userService;

    public InterestController(InterestService interestService, UserService userService) {
        this.interestService = interestService;
        this.userService = userService;
    }

    @GetMapping
    public List<Interest> listInterests(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Users user = userPrincipal.getUser();  // Extract Users from UserPrincipal
        return interestService.listInterests(user);  // Pass Users to the service
    }

    @PostMapping
    public Interest createInterest(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody JsonNode jsonNode) {
        if (userPrincipal == null) {
            System.out.println("InterestController.createInterest()::User is null");
            return null;  // Handle the case when user is null
        } else {
            Users user = userPrincipal.getUser();  // Extract the Users object
            String interestName = jsonNode.get("name").asText();  // Extract interestName from the JSON
            System.out.println("InterestController.createInterest()::Authenticated user: " + user.getEmail());
            System.out.println("InterestController.createInterest()::Interest name: " + interestName);
            return interestService.createInterest(user, interestName);  // Pass Users object and interestName to service
        }
    }


    // Update an existing interest
    @PutMapping("/{id}")
    public Interest updateInterest(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id, @RequestBody JsonNode jsonNode) {

        if (userPrincipal == null) {
            System.out.println("InterestController.updateInterest()::User is null");
            return null;  // Handle the case when user is null
        } else {
            Users user = userPrincipal.getUser();  // Extract the Users object
            String interestName = jsonNode.get("name").asText();  // Get the updated interest name

            // Check if the interest exists, if not return null or throw error (depending on your design)
            Interest interest = interestService.getInterestById(id);  // Fetch the interest by ID
            if (interest == null) {
                System.out.println("InterestController.updateInterest()::Interest not found with ID: " + id);
                return null;
            }

            // Ensure the user is the owner of the interest
            if (!interest.getUser().getId().equals(user.getId())) {
                System.out.println("InterestController.updateInterest()::User not authorized to update this interest");
                return null;  // Return an error or handle authorization failure
            }

            // Proceed with the update
            interest.setName(interestName);
            return interestService.updateInterest(interest);  // Update and return the updated interest
        }
    }

    // Delete an interest
    @DeleteMapping("/{id}")
    public void deleteInterest(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id) {
        if (userPrincipal == null) {
            System.out.println("InterestController.deleteInterest()::User is null");
            return;  // Handle the case when user is null
        } else {
            Users user = userPrincipal.getUser();  // Extract the Users object

            // Check if the interest exists
            Interest interest = interestService.getInterestById(id);  // Fetch the interest by ID
            if (interest == null) {
                System.out.println("InterestController.deleteInterest()::Interest not found with ID: " + id);
                return;
            }

            // Ensure the user is the owner of the interest
            if (!interest.getUser().getId().equals(user.getId())) {
                System.out.println("InterestController.deleteInterest()::User not authorized to delete this interest");
                return;  // Return an error or handle authorization failure
            }

            // Proceed with the deletion
            interestService.deleteInterest(id);  // Delete the interest from the service
            System.out.println("InterestController.deleteInterest()::Interest deleted with ID: " + id);
        }
    }
}