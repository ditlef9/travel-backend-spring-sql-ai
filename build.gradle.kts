plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.ekeberg"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot starters
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security") // For Spring Security
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // For JPA support

	// PostgreSQL Driver
	runtimeOnly("org.postgresql:postgresql")

	// Development tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// JWT Support
	implementation("io.jsonwebtoken:jjwt-api:0.12.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")


	// Swagger Docs
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	// Read env
	implementation("io.github.cdimascio:dotenv-java:3.0.2")



	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
