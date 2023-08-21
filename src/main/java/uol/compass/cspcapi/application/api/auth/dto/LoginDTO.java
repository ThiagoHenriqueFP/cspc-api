package uol.compass.cspcapi.application.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "username must not be empty") String email,
        @NotBlank(message = "password must not be empty") String password
) {
    public LoginDTO(String email, String password) {
        this.email = email.trim();
        this.password = password.trim();
    }
}
