package teamdev.tech.jbank.controllers.dtos;

import java.util.List;

public record ApiResponse<T>(
        List<T> items,
        ApiPagination pagination
) {
}
