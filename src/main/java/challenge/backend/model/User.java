package challenge.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {
    private UUID id;
    private String name;
    private List<Deposit> deposits = new ArrayList<>();

    public void addDeposit(Deposit deposit) {
        deposits.add(deposit);
    }

    public BigDecimal calculateBalance() {
        LocalDate referenceDate = LocalDate.now();
        return deposits.stream()
                .map(deposit -> deposit.getAmountIfNotExpired(referenceDate))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
