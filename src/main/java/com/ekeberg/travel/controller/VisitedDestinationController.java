/**
 * java/com/ekeberg/travel/controller/VisitedDestinationController.java
 *
 * REST API Controller for managing visited destinations.
 * Provides endpoints for creating, listing, updating, and deleting visited destinations.
 * Requires user authentication to access.
 *
 * Author: ditlef9
 */

package com.ekeberg.travel.controller;

import com.ekeberg.travel.model.VisitedDestination;
import com.ekeberg.travel.model.UserPrincipal;
import com.ekeberg.travel.model.Users;
import com.ekeberg.travel.service.VisitedDestinationService;
import com.ekeberg.travel.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visited-destinations")
public class VisitedDestinationController {

    private final VisitedDestinationService visitedDestinationService;
    private final UserService userService;

    public VisitedDestinationController(VisitedDestinationService visitedDestinationService, UserService userService) {
        this.visitedDestinationService = visitedDestinationService;
        this.userService = userService;
    }

    // List visited destinations for a user
    @GetMapping
    public List<VisitedDestination> listVisitedDestinations(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Users user = userPrincipal.getUser();  // Extract Users from UserPrincipal
        return visitedDestinationService.listVisitedDestinations(user);  // Pass Users to the service
    }

    // Create a new visited destination
    @PostMapping
    public VisitedDestination createVisitedDestination(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody JsonNode jsonNode) {
        if (userPrincipal == null) {
            System.out.println("VisitedDestinationController.createVisitedDestination()::User is null");
            return null;  // Handle the case when user is null
        } else {
            Users user = userPrincipal.getUser();  // Extract the Users object
            String destinationName = jsonNode.get("name").asText();  // Extract destinationName from the JSON
            System.out.println("VisitedDestinationController.createVisitedDestination()::Authenticated user: " + user.getEmail());
            System.out.println("VisitedDestinationController.createVisitedDestination()::Destination name: " + destinationName);
            return visitedDestinationService.createVisitedDestination(user, destinationName);  // Pass Users object and destinationName to service
        }
    }

    // Update an existing visited destination
    @PutMapping("/{id}")
    public VisitedDestination updateVisitedDestination(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id, @RequestBody JsonNode jsonNode) {

        if (userPrincipal == null) {
            System.out.println("VisitedDestinationController.updateVisitedDestination()::User is null");
            return null;  // Handle the case when user is null
        } else {
            Users user = userPrincipal.getUser();  // Extract the Users object
            String destinationName = jsonNode.get("name").asText();  // Get the updated destination name

            // Check if the destination exists, if not return null or throw error (depending on your design)
            VisitedDestination destination = visitedDestinationService.getVisitedDestinationById(id);  // Fetch the destination by ID
            if (destination == null) {
                System.out.println("VisitedDestinationController.updateVisitedDestination()::Destination not found with ID: " + id);
                return null;
            }

            // Ensure the user is the owner of the destination
            if (!destination.getUser().getId().equals(user.getId())) {
                System.out.println("VisitedDestinationController.updateVisitedDestination()::User not authorized to update this destination");
                return null;  // Return an error or handle authorization failure
            }

            // Proceed with the update
            destination.setName(destinationName);
            return visitedDestinationService.updateVisitedDestination(destination);  // Update and return the updated destination
        }
    }

    // Delete a visited destination
    @DeleteMapping("/{id}")
    public void deleteVisitedDestination(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id) {
        if (userPrincipal == null) {
            System.out.println("VisitedDestinationController.deleteVisitedDestination()::User is null");
            return;  // Handle the case when user is null
        } else {
            Users user = userPrincipal.getUser();  // Extract the Users object

            // Check if the destination exists
            VisitedDestination destination = visitedDestinationService.getVisitedDestinationById(id);  // Fetch the destination by ID
            if (destination == null) {
                System.out.println("VisitedDestinationController.deleteVisitedDestination()::Destination not found with ID: " + id);
                return;
            }

            // Ensure the user is the owner of the destination
            if (!destination.getUser().getId().equals(user.getId())) {
                System.out.println("VisitedDestinationController.deleteVisitedDestination()::User not authorized to delete this destination");
                return;  // Return an error or handle authorization failure
            }

            // Proceed with the deletion
            visitedDestinationService.deleteVisitedDestination(id);  // Delete the destination from the service
            System.out.println("VisitedDestinationController.deleteVisitedDestination()::Destination deleted with ID: " + id);
        }
    }

    // Endpoint to suggest destinations
    @GetMapping("/suggestions")
    public List<VisitedDestination> suggestDestinations(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Users user = userPrincipal.getUser();  // Get the user from the authenticated principal
        System.out.println("VisitedDestinationController.createVisitedDestination()::Authenticated user: " + user.getEmail());
        return visitedDestinationService.suggestDestinations(user);
    }


}
