package teamdev.tech.jbank.controllers.dtos;

import java.util.List;

public record StatementDto(
        WalletDto wallet,
        List<StatementItemDto> statements,
        ApiPagination pagination
) {
}
