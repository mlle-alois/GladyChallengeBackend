package challenge.backend.model;

import challenge.backend.exception.IllegalDepositAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompanyTest {
    @Nested
    @DisplayName("Tests for distributeDeposit method")
    class DistributeDepositTests {
        @Test
        @DisplayName("Should subtract amount from balance when balance is sufficient")
        void should_SubtractAmountFromBalance_When_BalanceIsSufficient() {
            // given
            Company company = Company.builder()
                    .id(UUID.randomUUID())
                    .name("Microsoft")
                    .balance(BigDecimal.valueOf(1000.0))
                    .build();
            Deposit deposit = Deposit.create(company.getId(), DepositType.GIFT, BigDecimal.valueOf(500.0), LocalDate.now());

            // when
            company.distributeDeposit(deposit);

            // then
            assertEquals(company.getBalance(), BigDecimal.valueOf(500.0));
        }

        @Test
        @DisplayName("Should throw exception when balance is insufficient")
        void should_ThrowException_When_BalanceIsInsufficient() {
            // given
            Company company = Company.builder()
                    .id(UUID.randomUUID())
                    .name("Apple")
                    .balance(BigDecimal.valueOf(100.0))
                    .build();
            Deposit deposit = Deposit.create(company.getId(), DepositType.GIFT, BigDecimal.valueOf(200.0), LocalDate.now());

            // when
            Executable executable = () -> company.distributeDeposit(deposit);

            // then
            IllegalDepositAmountException exception = assertThrows(IllegalDepositAmountException.class, executable);
            assertEquals("Insufficient balance for the deposit.", exception.getMessage());
            assertEquals(company.getBalance(), BigDecimal.valueOf(100.0));
        }
    }
}
