package challenge.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
@Getter
public class Deposit {
    private UUID id;
    private UUID companyId;
    private DepositType depositType;
    private BigDecimal amount;
    private LocalDate distributionDate;
    private LocalDate expirationDate;

    private final static int GIFT_EXPIRATION_DAYS = 365 - 1;

    public static Deposit create(UUID companyId, DepositType depositType, BigDecimal amount, LocalDate distributionDate) {
        LocalDate expirationDate = calculateExpirationDate(depositType, distributionDate);
        return new Deposit(UUID.randomUUID(), companyId, depositType, amount, distributionDate, expirationDate);
    }

    private static LocalDate calculateExpirationDate(DepositType type, LocalDate distributionDate) {
        if (type == DepositType.MEAL) {
            return getEndOfFebruaryNextYear(distributionDate);
        } else {
            return distributionDate.plusDays(GIFT_EXPIRATION_DAYS);
        }
    }

    private static LocalDate getEndOfFebruaryNextYear(LocalDate distributionDate) {
        return distributionDate.withMonth(2).withYear(distributionDate.getYear() + 1)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    public BigDecimal getAmountIfNotExpired(LocalDate currentDate) {
        return isExpired(currentDate) ? new BigDecimal("0") : amount;
    }

    private boolean isExpired(LocalDate currentDate) {
        return currentDate.isAfter(expirationDate);
    }
}
