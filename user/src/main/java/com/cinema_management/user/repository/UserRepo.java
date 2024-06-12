package com.cinema_management.user.repository;

import com.cinema_management.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByUsername (String username);

    Optional<User> findByUsernameAndIdIsNot(String username, String userId);
}
