/**
 * java/com/ekeberg/travel/interest/InterestService.java
 *
 * Service layer for managing interests.
 * Provides business logic for creating, listing, updating, and deleting interests.
 *
 * Author: ditlef9
 */

package com.ekeberg.travel.service;

import com.ekeberg.travel.model.Interest;
import com.ekeberg.travel.model.Users;
import com.ekeberg.travel.repo.InterestRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class InterestService {

    private final InterestRepository interestRepository;

    public InterestService(InterestRepository interestRepository) {
        this.interestRepository = interestRepository;
    }

    // Fetch an interest by ID (if you don't already have this method)
    public Interest getInterestById(Long id) {
        return interestRepository.findById(id).orElse(null);  // Return null if not found
    }

    /**
     * Retrieves all interests associated with a user.
     */
    public List<Interest> listInterests(Users user) {
        return interestRepository.findByUserId(user.getId());
    }

    /**
     * Creates a new interest for the specified user.
     */
    public Interest createInterest(Users user, String interestName) {
        Interest interest = new Interest();
        interest.setName(interestName);
        interest.setUser(user);  // Set Users object directly
        System.out.println("InterestService.createInterest()::interestName: " + interestName);
        return interestRepository.save(interest);
    }

    /**
     * Updates an existing interest's name.

    public Interest updateInterest(Long interestId, String newName) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new RuntimeException("Interest not found"));
        interest.setName(newName);
        return interestRepository.save(interest);
    } */
    // Update an interest
    public Interest updateInterest(Interest interest) {
        return interestRepository.save(interest);  // Save the updated interest back to the database
    }
    /**
     * Deletes an interest by its ID.
     */
    public void deleteInterest(Long interestId) {
        interestRepository.deleteById(interestId);
    }
}