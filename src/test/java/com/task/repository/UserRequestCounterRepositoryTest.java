package com.task.repository;

import com.task.entity.UserRequestCounter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRequestCounterRepositoryTest {

    @Autowired
    private UserRequestCounterRepository userRequestCounterRepository;

    private final UserRequestCounter userRequestCounter = new UserRequestCounter("login");


    @Test
    void testFindByLoginSuccess() {
        /* Preparation */
        userRequestCounterRepository.save(userRequestCounter);

        /* Execution */
        Optional<UserRequestCounter> checked = userRequestCounterRepository.findByLogin("login");

        /* Verification */
        assertTrue(checked.isPresent());
    }

    @Test
    void testFindByLoginFailed() {
        /* Preparation */
        userRequestCounterRepository.save(userRequestCounter);

        /* Execution */
        Optional<UserRequestCounter> checked = userRequestCounterRepository.findByLogin("login2");

        /* Verification */
        assertFalse(checked.isPresent());
    }
}