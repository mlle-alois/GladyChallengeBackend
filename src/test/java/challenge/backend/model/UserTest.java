package challenge.backend.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static challenge.backend.model.DepositType.GIFT;
import static challenge.backend.model.DepositType.MEAL;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Nested
    @DisplayName("Tests for addDeposit method")
    class AddDepositTests {
        @Test
        @DisplayName("Should add a deposit to the user's deposit list")
        void should_AddDeposit_ToUserDepositList() {
            // given
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .name("John Doe")
                    .deposits(new ArrayList<>())
                    .build();
            Deposit deposit = Deposit.create(UUID.randomUUID(), GIFT, BigDecimal.valueOf(100), LocalDate.of(2024, JANUARY, 1));

            int expectedSize = 1;

            // when
            user.addDeposit(deposit);

            // then
            List<Deposit> userDeposits = user.getDeposits();
            assertEquals(expectedSize, userDeposits.size());
            assertEquals(deposit, userDeposits.getFirst());
        }
    }

    @Nested
    @DisplayName("Tests for calculateBalance method")
    class CalculateBalanceTests {
        @Test
        @DisplayName("Should calculate balance correctly when deposits are not expired")
        void should_CalculateBalanceCorrectly_When_DepositsAreNotExpired() {
            // given
            UUID companyId = UUID.randomUUID();
            LocalDate notExpiredDate = LocalDate.now().minusDays(156);
            List<Deposit> deposits = List.of(
                    Deposit.create(companyId, GIFT, BigDecimal.valueOf(100), notExpiredDate),
                    Deposit.create(companyId, MEAL, BigDecimal.valueOf(200), notExpiredDate)
            );
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .name("Jane Doe")
                    .deposits(deposits)
                    .build();

            BigDecimal expectedBalance = BigDecimal.valueOf(300);

            // when
            BigDecimal actualBalance = user.calculateBalance();

            // then
            assertEquals(expectedBalance, actualBalance);
        }

        @Test
        @DisplayName("Should calculate balance correctly when some deposits are expired")
        void should_CalculateBalanceCorrectly_When_SomeDepositsAreExpired() {
            // given
            UUID companyId = UUID.randomUUID();
            LocalDate notExpiredDate = LocalDate.now().minusDays(156);
            LocalDate expiredDate = LocalDate.now().minusDays(500);

            List<Deposit> deposits = List.of(
                    Deposit.create(companyId, GIFT, BigDecimal.valueOf(100), expiredDate),
                    Deposit.create(companyId, MEAL, BigDecimal.valueOf(220), notExpiredDate)
            );
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .name("Jane Doe")
                    .deposits(deposits)
                    .build();

            BigDecimal expectedBalance = BigDecimal.valueOf(220);

            // when
            BigDecimal actualBalance = user.calculateBalance();

            // then
            assertEquals(expectedBalance, actualBalance);
        }

        @Test
        @DisplayName("Should return zero balance when all deposits are expired")
        void should_ReturnZeroBalance_When_AllDepositsAreExpired() {
            // given
            UUID companyId = UUID.randomUUID();
            LocalDate expiredDate = LocalDate.now().minusDays(500);

            List<Deposit> deposits = List.of(
                    Deposit.create(companyId, GIFT, BigDecimal.valueOf(100), expiredDate),
                    Deposit.create(companyId, MEAL, BigDecimal.valueOf(220), expiredDate)
            );
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .name("Jane Doe")
                    .deposits(deposits)
                    .build();

            BigDecimal expectedBalance = BigDecimal.ZERO;

            // when
            BigDecimal actualBalance = user.calculateBalance();

            // then
            assertEquals(expectedBalance, actualBalance);
        }
    }
}
