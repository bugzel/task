package com.task.repository;

import com.task.entity.UserRequestCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRequestCounterRepository extends JpaRepository<UserRequestCounter, String> {
    Optional<UserRequestCounter> findByLogin(String login);
}