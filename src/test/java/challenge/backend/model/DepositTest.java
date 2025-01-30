package challenge.backend.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import static challenge.backend.model.DepositType.GIFT;
import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DepositTest {
    @Nested
    @DisplayName("Tests for create method")
    class CreateDepositTests {

        record DepositTestCase(String description, UUID companyId, DepositType depositType, BigDecimal amount,
                               LocalDate distributionDate, LocalDate expectedExpirationDate) {
        }

        static Stream<Arguments> provideDepositsData() {
            return Stream.of(
                    Arguments.of(new DepositTestCase(
                            "Gift deposit with standard expiration",
                            UUID.randomUUID(), DepositType.GIFT, BigDecimal.valueOf(100.0),
                            LocalDate.of(2021, JUNE, 15), LocalDate.of(2022, JUNE, 14))
                    ),

                    Arguments.of(new DepositTestCase(
                            "Meal deposit with standard expiration",
                            UUID.randomUUID(), DepositType.MEAL, BigDecimal.valueOf(200.0),
                            LocalDate.of(2020, JANUARY, 1), LocalDate.of(2021, FEBRUARY, 28))
                    ),

                    Arguments.of(new DepositTestCase(
                            "Meal deposit with standard expiration (non-leap year)",
                            UUID.randomUUID(), DepositType.MEAL, BigDecimal.valueOf(200.0),
                            LocalDate.of(2026, JUNE, 12), LocalDate.of(2027, FEBRUARY, 28))
                    ),

                    Arguments.of(new DepositTestCase(
                            "Meal deposit with leap year expiration",
                            UUID.randomUUID(), DepositType.MEAL, BigDecimal.valueOf(200.0),
                            LocalDate.of(2027, JUNE, 12), LocalDate.of(2028, FEBRUARY, 29))
                    )
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("provideDepositsData")
        @DisplayName("Should create deposit with correct values")
        void should_CreateDepositWithCorrectValues(DepositTestCase testCase) {
            // when
            Deposit deposit = Deposit.create(testCase.companyId, testCase.depositType, testCase.amount, testCase.distributionDate);

            // then
            assertAll(
                    () -> assertEquals(testCase.depositType, deposit.getDepositType()),
                    () -> assertEquals(testCase.amount, deposit.getAmount()),
                    () -> assertEquals(testCase.distributionDate, deposit.getDistributionDate()),
                    () -> assertEquals(testCase.expectedExpirationDate, deposit.getExpirationDate())
            );
        }
    }

    @Nested
    @DisplayName("Tests for getAmountIfNotExpired method")
    class GetAmountIfNotExpiredTests {
        @Test
        @DisplayName("Should return zero amount when deposit is expired")
        void should_ReturnZeroAmount_When_DepositIsExpired() {
            // given
            UUID companyId = UUID.randomUUID();
            BigDecimal amount = BigDecimal.valueOf(300.0);
            LocalDate distributionDate = LocalDate.of(2024, JANUARY, 1);

            Deposit deposit = Deposit.create(companyId, GIFT, amount, distributionDate);

            LocalDate expiredDate = LocalDate.of(2025, MARCH, 15);

            BigDecimal expectedAmount = BigDecimal.ZERO;

            // when
            BigDecimal actualAmount = deposit.getAmountIfNotExpired(expiredDate);

            // then
            assertEquals(expectedAmount, actualAmount);
        }

        @Test
        @DisplayName("Should return amount when deposit is not expired")
        void should_ReturnAmount_When_DepositIsNotExpired() {
            // given
            UUID companyId = UUID.randomUUID();
            BigDecimal amount = BigDecimal.valueOf(300.0);
            LocalDate distributionDate = LocalDate.of(2024, JANUARY, 1);

            Deposit deposit = Deposit.create(companyId, GIFT, amount, distributionDate);

            LocalDate notExpiredDate = LocalDate.of(2024, JUNE, 15);

            BigDecimal expectedAmount = BigDecimal.valueOf(300.0);

            // when
            BigDecimal actualAmount = deposit.getAmountIfNotExpired(notExpiredDate);

            // then
            assertEquals(expectedAmount, actualAmount);
        }
    }
}
