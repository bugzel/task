package com.task.controller;
import com.task.model.UserDTO;
import com.task.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;

    private UserService userService;

    private final String login = "login";

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void testGetUserSuccess() {
        /* Preparation */
        UserDTO userDTO = prepareUserDTO();
        when(userService.getUser(login)).thenReturn(userDTO);

        /* Execution */
        ResponseEntity<?> response = userController.getUser(login);

        /* Verification */
        verify(userService, times(1)).getUser(login);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), userDTO);
    }

    @Test
    public void testGetUserNotFound() {
        /* Preparation */
        when(userService.getUser(login)).thenReturn(null);

        /* Execution */
        try {
            userController.getUser(login);
        } catch (ResponseStatusException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }

        /* Verification */
        verify(userService, times(1)).getUser(login);
    }

    private UserDTO prepareUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(583231);
        userDTO.setLogin("octocat");
        userDTO.setName("The Octocat");
        userDTO.setType("User");
        userDTO.setAvatarUrl("https://avatars.githubusercontent.com/u/583231?v=4");
        userDTO.setCreatedAt(LocalDateTime.parse("2011-01-25T18:44:36"));
        userDTO.setCalculations(BigDecimal.valueOf(0.01));
        return userDTO;
    }
}