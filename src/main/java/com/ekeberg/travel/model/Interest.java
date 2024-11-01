
package com.ekeberg.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * java/com/ekeberg/travel/interest/Interest.java
 *
 * Represents an interest or hobby associated with a user.
 * Each interest has a name and is linked to a specific user.
 *
 * Author: ditlef9
 */
@Entity(name="interests")
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Name or description of the interest

    @ManyToOne
    @JsonIgnore  // Prevent the user field from being serialized into the response
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // The user associated with this interest

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
        this.user = user;  // Accept Users directly
    }
}