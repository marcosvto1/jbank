package teamdev.tech.jbank.controllers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CreateWalletDto(
        @CPF
        @NotBlank
        String cpf,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String name) {
}
