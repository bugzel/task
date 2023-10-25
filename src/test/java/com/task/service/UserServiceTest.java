package com.task.service;

import com.task.model.UserDTO;
import com.task.repository.UserRequestCounterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRequestCounterRepository userRequestCounterRepository;

    @Mock
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRequestCounterRepository);
    }

    private final String correctLogin = "octocat";
    private final String incorrectLogin = "jgoxwkfxwjnto";

    @Test
    public void testGetUserDTOSuccess_LoginRequestCounterExecuted() {
        /* Execution */
        userDTO = userService.getUser(correctLogin);

        /* Verification */
        verify(userRequestCounterRepository).save(any());
        assertNotNull(userDTO);
    }

    @Test
    public void testGetUserDTOFailed_LoginRequestCounterExecuted() {
        /* Execution */
        userDTO = userService.getUser(incorrectLogin);

        /* Verification */
        verify(userRequestCounterRepository).save(any());
        assertNull(userDTO);
    }

}