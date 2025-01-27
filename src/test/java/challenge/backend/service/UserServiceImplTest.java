package challenge.backend.service;

import challenge.backend.model.Deposit;
import challenge.backend.model.User;
import challenge.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static challenge.backend.model.DepositType.GIFT;
import static challenge.backend.model.DepositType.MEAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        LocalDate notExpiredDate = LocalDate.now().minusDays(156);
        user = User.builder()
                .id(userId)
                .name("John Doe")
                .deposits(List.of(
                        Deposit.create(UUID.randomUUID(), GIFT, BigDecimal.valueOf(100), notExpiredDate),
                        Deposit.create(UUID.randomUUID(), MEAL, BigDecimal.valueOf(200), notExpiredDate)
                ))
                .build();
    }

    @Nested
    @DisplayName("Tests for getUserById method")
    class GetUserByIdTests {
        @Test
        @DisplayName("Should return user by ID when exists")
        void should_ReturnUserById_When_UserExists() {
            // given
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // when
            Optional<User> foundUser = userService.getUserById(userId);

            // then
            assertTrue(foundUser.isPresent());
            assertEquals(userId, foundUser.get().getId());
        }

        @Test
        @DisplayName("Should return empty when user not found")
        void should_ThrowException_When_UserNotFound() {
            // given
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when
            Optional<User> foundUser = userService.getUserById(userId);

            // then
            assertTrue(foundUser.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests for save method")
    class SaveTests {
        @Test
        @DisplayName("Should save user")
        void should_SaveUser() {
            // given
            doNothing().when(userRepository).save(user);

            // when
            userService.save(user);

            // then
            verify(userRepository, times(1)).save(user);
        }
    }

    @Nested
    @DisplayName("Tests for calculateUserBalance method")
    class CalculateUserBalanceTests {
        @Test
        @DisplayName("Should calculate user balance correctly without mocking the user")
        void should_CalculateUserBalanceCorrectly() {
            // given
            BigDecimal expectedBalance = BigDecimal.valueOf(300);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // when
            BigDecimal actualBalance = userService.calculateUserBalance(userId);

            // then
            assertEquals(expectedBalance, actualBalance);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void should_ThrowException_When_CalculatingBalanceForUserNotFound() {
            // given
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when
            Executable executable = () -> userService.calculateUserBalance(userId);

            // then
            assertThrows(IllegalArgumentException.class, executable);
        }
    }
}
