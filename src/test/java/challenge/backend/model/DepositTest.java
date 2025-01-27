package challenge.backend.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

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
    class createDepositTests {
        @Test
        @DisplayName("Should create deposit with correct values when deposit type is gift")
        void should_CreateDepositWithCorrectValues_When_DepositTypeIsGift() {
            // given
            UUID companyId = UUID.randomUUID();
            DepositType depositType = GIFT;
            BigDecimal amount = BigDecimal.valueOf(100.0);
            LocalDate distributionDate = LocalDate.of(2021, JUNE, 15);

            LocalDate expectedExpirationDate = LocalDate.of(2022, JUNE, 14);

            // when
            Deposit deposit = Deposit.create(companyId, depositType, amount, distributionDate);

            // then
            assertAll(
                    () -> assertEquals(depositType, deposit.getDepositType()),
                    () -> assertEquals(amount, deposit.getAmount()),
                    () -> assertEquals(distributionDate, deposit.getDistributionDate()),
                    () -> assertEquals(expectedExpirationDate, deposit.getExpirationDate())
            );
        }

        @Test
        @DisplayName("Should create deposit with correct values when deposit type is meal")
        void should_CreateDepositWithCorrectValues_When_DepositTypeIsMeal() {
            // given
            UUID companyId = UUID.randomUUID();
            DepositType depositType = DepositType.MEAL;
            BigDecimal amount = BigDecimal.valueOf(200.0);
            LocalDate distributionDate = LocalDate.of(2020, JANUARY, 1);

            LocalDate expectedExpirationDate = LocalDate.of(2021, FEBRUARY, 28);

            // when
            Deposit deposit = Deposit.create(companyId, depositType, amount, distributionDate);

            // then
            assertAll(
                    () -> assertEquals(depositType, deposit.getDepositType()),
                    () -> assertEquals(amount, deposit.getAmount()),
                    () -> assertEquals(distributionDate, deposit.getDistributionDate()),
                    () -> assertEquals(expectedExpirationDate, deposit.getExpirationDate())
            );
        }

        @Test
        @DisplayName("Should create deposit with correct values when deposit type is meal and is leap year")
        void should_CreateDepositWithCorrectValues_When_DepositTypeIsMealAndIsLeapYear() {
            // given
            UUID companyId = UUID.randomUUID();
            DepositType depositType = DepositType.MEAL;
            BigDecimal amount = BigDecimal.valueOf(200.0);
            LocalDate distributionDate = LocalDate.of(2027, JUNE, 12);

            LocalDate expectedExpirationDate = LocalDate.of(2028, FEBRUARY, 29); // année bissextile

            // when
            Deposit deposit = Deposit.create(companyId, depositType, amount, distributionDate);

            // then
            assertAll(
                    () -> assertEquals(depositType, deposit.getDepositType()),
                    () -> assertEquals(amount, deposit.getAmount()),
                    () -> assertEquals(distributionDate, deposit.getDistributionDate()),
                    () -> assertEquals(expectedExpirationDate, deposit.getExpirationDate())
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
