package challenge.backend.controller;

import challenge.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UUID userId;
    private BigDecimal userBalance;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userId = UUID.randomUUID();
        userBalance = BigDecimal.valueOf(1000.0);
    }

    @Test
    @DisplayName("Should return balance when user exists")
    void should_ReturnBalance_When_UserExists() throws Exception {
        // Given
        when(userService.calculateUserBalance(userId)).thenReturn(userBalance);

        // When
        mockMvc.perform(get("/user/{userId}/balance", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(userBalance));
    }

    @Test
    @DisplayName("Should return not found when user does not exist")
    void should_ReturnNotFound_When_UserDoesNotExist() throws Exception {
        // Given
        when(userService.calculateUserBalance(userId)).thenThrow(new IllegalArgumentException("User not found"));

        // When
        mockMvc.perform(get("/user/{userId}/balance", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("User not found"));
    }
}
