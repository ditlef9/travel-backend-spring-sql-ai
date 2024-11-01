package com.ekeberg.travel.repo;

import com.ekeberg.travel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * java/com/ekeberg/travel/repo/UserRepo.java
 *
 * @author https://github.com/ditlef9
 */
@Repository
public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByEmail(String email);
}
