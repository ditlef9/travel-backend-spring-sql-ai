
package com.ekeberg.travel.repo;

import com.ekeberg.travel.model.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * java/com/ekeberg/travel/interest/InterestRepository.java
 *
 * Repository for managing Interest data.
 * Provides database access methods for the Interest entity.
 *
 * Author: ditlef9
 */
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByUserId(Long userId); // Find interests associated with a specific user ID
}

