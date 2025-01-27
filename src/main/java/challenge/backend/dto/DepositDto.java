package challenge.backend.dto;

import challenge.backend.model.DepositType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record DepositDto(UUID userId, DepositType type, BigDecimal amount, LocalDate distributionDate) {
}

