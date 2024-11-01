package com.ekeberg.travel;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class TravelApplication {

	public static void main(String[] args) {
		// Load the .env file (You could also do this in a @PostConstruct method if you want)
		Dotenv dotenv = Dotenv.load();

		// Access the environment variable
		String openaiApiKey = dotenv.get("OPENAI_API_KEY");
		// Optionally, set it as a Spring Property
		System.setProperty("OPENAI_API_KEY", openaiApiKey);

		SpringApplication.run(TravelApplication.class, args);
	}

	// Allow http://localhost to 8080
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Allow all endpoints
						.allowedOrigins("http://localhost:3000") // Allow the specific origin
						.allowedMethods("*") // Allow all methods (GET, POST, PUT, DELETE, OPTIONS)
						.allowedHeaders("*"); // Allow all headers
			}
		};
	}

	// Define RestTemplate bean
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
