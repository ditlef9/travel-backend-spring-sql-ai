package com.ekeberg.travel;

import org.springframework.boot.SpringApplication;

public class TestTravelApplication {

	public static void main(String[] args) {
		SpringApplication.from(TravelApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
