package com.ekeberg.travel.service;

import com.ekeberg.travel.model.Users;
import com.ekeberg.travel.model.VisitedDestination;
import com.ekeberg.travel.repo.InterestRepository;
import com.ekeberg.travel.repo.VisitedDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitedDestinationService {

    private final VisitedDestinationRepository visitedDestinationRepository;
    private final InterestRepository interestRepository;
    private final SuggestionService suggestionService;

    @Autowired
    public VisitedDestinationService(VisitedDestinationRepository visitedDestinationRepository,
                                     InterestRepository interestRepository,
                                     SuggestionService suggestionService) {
        this.visitedDestinationRepository = visitedDestinationRepository;
        this.interestRepository = interestRepository;
        this.suggestionService = suggestionService;
    }

    // Fetch a visited destination by ID
    public VisitedDestination getVisitedDestinationById(Long id) {
        return visitedDestinationRepository.findById(id).orElse(null);  // Return null if not found
    }

    public List<VisitedDestination> listVisitedDestinations(Users user) {
        return visitedDestinationRepository.findByUserId(user.getId());  // Fetch visited destinations by user ID
    }

    public VisitedDestination createVisitedDestination(Users user, String visitedDestinationName) {
        VisitedDestination visitedDestination = new VisitedDestination();
        visitedDestination.setName(visitedDestinationName);
        visitedDestination.setUser(user);  // Set the user who visited the destination
        return visitedDestinationRepository.save(visitedDestination);  // Save the destination in the database
    }

    public VisitedDestination updateVisitedDestination(VisitedDestination visitedDestination) {
        return visitedDestinationRepository.save(visitedDestination);  // Update the destination in the database
    }

    public void deleteVisitedDestination(Long visitedDestinationId) {
        visitedDestinationRepository.deleteById(visitedDestinationId);  // Delete the destination from the database
    }

    // Suggest destinations based on interests and excluding visited destinations using AI
    public List<VisitedDestination> suggestDestinations(Users user) {
        // Get the list of interests the user has
        List<String> interests = interestRepository.findByUserId(user.getId())
                .stream()
                .map(interest -> interest.getName())
                .collect(Collectors.toList());

        // Get a list of visited destinations by the user
        List<String> visitedDestinations = visitedDestinationRepository.findByUserId(user.getId())
                .stream()
                .map(VisitedDestination::getName)
                .collect(Collectors.toList());

        // Fetch AI-generated suggestions
        String aiSuggestions = suggestionService.getSuggestedDestinationsFromAI(
                String.join(", ", interests), visitedDestinations);

        // Use the AI suggestions to return appropriate destinations
        // Parse and filter the suggestions if necessary
        // For now, return all the destinations as a placeholder
        return visitedDestinationRepository.findAll(); // Replace this with parsed AI suggestions
    }
}
