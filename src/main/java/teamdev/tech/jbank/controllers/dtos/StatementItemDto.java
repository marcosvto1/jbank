package teamdev.tech.jbank.controllers.dtos;


import teamdev.tech.jbank.enums.EStatementOperation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementItemDto(
        String statementId,
        String statementType,
        BigDecimal statementAmount,
        String literal,
        LocalDateTime datetime,
        EStatementOperation operation
) {
}
