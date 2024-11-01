package com.ekeberg.travel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Service
public class SuggestionService {

    @Value("${OPENAI_API_KEY}")
    private String openAIApiKey;

    private final RestTemplate restTemplate;

    public SuggestionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getSuggestedDestinationsFromAI(String interests, List<String> visitedDestinations) {
        // API request to OpenAI or any other AI provider
        String prompt = generatePrompt(interests, visitedDestinations);

        // OpenAI API URL for the latest chat model
        String apiUrl = "https://api.openai.com/v1/chat/completions";

        // Prepare the payload
        Map<String, Object> payload = Map.of(
                "model", "gpt-3.5-turbo",  // Using the latest gpt-3.5-turbo model
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant."),
                        Map.of("role", "user", "content", prompt)
                ),
                "max_tokens", 150
        );

        // Set up the HTTP request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openAIApiKey);

        // Prepare the request entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        // Send the request
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

        // Parse the response to get the message content
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    return (String) choice.get("message"); // Adjust based on the structure of response
                }
            }
        }

        // Return a default message if there's an error or no valid response
        return "No suggestions found.";
    }

    private String generatePrompt(String interests, List<String> visitedDestinations) {
        String visitedPlaces = String.join(", ", visitedDestinations);
        return "Suggest travel destinations based on the following interests: " + interests +
                ". Exclude these places from the suggestions: " + visitedPlaces + ".";
    }
}
