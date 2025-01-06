package teamdev.tech.jbank.controllers.dtos;

public record ApiPagination(
        Integer pageSize,
        Integer pageNumber,
        Long totalElements,
        Integer totalPage
) {
}
