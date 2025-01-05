package teamdev.tech.jbank.exceptions.dto;

public record InvalidParamDto(
        String field,
        String reason
) {
}
