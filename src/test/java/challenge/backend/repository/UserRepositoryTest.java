package challenge.backend.repository;

import challenge.backend.model.User;
import challenge.backend.util.JsonFileReader;
import challenge.backend.util.JsonFileWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    @InjectMocks
    private UserRepository userRepository;

    @Mock
    private JsonFileReader jsonFileReader;
    @Mock
    private JsonFileWriter jsonFileWriter;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .name("Test User")
                .deposits(new ArrayList<>())
                .build();
    }

    @Nested
    @DisplayName("Tests for findById method")
    class FindByIdTests {
        @Test
        @DisplayName("Should return user when it's found")
        void should_ReturnUser_When_Found() {
            // Given
            when(jsonFileReader.readJsonFile(any(), any())).thenReturn(Optional.of(List.of(user)));

            // When
            Optional<User> foundUser = userRepository.findById(userId);

            // Then
            assertTrue(foundUser.isPresent());
            assertEquals(userId, foundUser.get().getId());
        }

        @Test
        @DisplayName("Should return empty when user not found")
        void should_ReturnEmpty_When_NotFound() {
            // Given
            UUID notExistingId = UUID.randomUUID();

            // When
            Optional<User> foundUser = userRepository.findById(notExistingId);

            // Then
            assertTrue(foundUser.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for save method")
    class SaveTests {
        @Test
        void should_WriteToFile_When_UserIsSaved() {
            // Given
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .name("Test User")
                    .deposits(new ArrayList<>())
                    .build();

            when(jsonFileReader.readJsonFile(any(), any())).thenReturn(Optional.of(new ArrayList<>(List.of(user))));

            // When
            userRepository.save(user);

            // Then
            verify(jsonFileWriter, times(1)).writeListToFile(any(), anyList());
        }
    }
}
