package com.ekeberg.travel.repo;

import com.ekeberg.travel.model.VisitedDestination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitedDestinationRepository extends JpaRepository<VisitedDestination, Long> {
    // Method to find visited destinations by user ID
    List<VisitedDestination> findByUserId(Long userId);
}
