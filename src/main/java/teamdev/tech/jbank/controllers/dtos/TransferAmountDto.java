package teamdev.tech.jbank.controllers.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferAmountDto(
        @NotNull
        UUID sender,

        @NotNull @DecimalMin("1.00")
        BigDecimal amount,

        @NotNull
        UUID receiver
) {
}
