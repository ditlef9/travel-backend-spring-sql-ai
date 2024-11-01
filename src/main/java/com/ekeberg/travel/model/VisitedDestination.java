package com.ekeberg.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * java/com/ekeberg/travel/model/VisitedDestination.java
 * Model class for visited destinations.
 *
 * Author: ditlef9
 */
@Entity(name = "visited_destinations")
public class VisitedDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Name of the visited destination

    @ManyToOne
    @JsonIgnore  // Prevent the user field from being serialized into the response
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // The user who visited the destination

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Users getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
