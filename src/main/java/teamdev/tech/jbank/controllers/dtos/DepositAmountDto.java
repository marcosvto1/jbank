package teamdev.tech.jbank.controllers.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositAmountDto(
        @NotNull @DecimalMin("10.00")
        BigDecimal amount
) {
}
