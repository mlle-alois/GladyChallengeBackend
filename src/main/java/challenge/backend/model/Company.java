package challenge.backend.model;

import challenge.backend.exception.IllegalDepositAmountException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private UUID id;
    private String name;
    private BigDecimal balance;

    public void distributeDeposit(final Deposit deposit) {
        if (isBalanceSufficient(deposit.getAmount())) {
            balance = balance.subtract(deposit.getAmount());
        } else {
            throw new IllegalDepositAmountException("Insufficient balance for the deposit.");
        }
    }

    private boolean isBalanceSufficient(BigDecimal amount) {
        return balance.compareTo(amount) >= 0;
    }
}
