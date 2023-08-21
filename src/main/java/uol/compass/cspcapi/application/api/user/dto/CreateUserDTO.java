package uol.compass.cspcapi.application.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank(message = "first name must not be empty")
        @Size(min = 3, message = "first name must be greater than 3 letters")
        String firstName,
        @NotBlank(message = "last name must not be empty")
        @Size(min = 3, message = "last name must be greater than 3 letters")
        String lastName,
        @NotBlank(message = "email name must not be empty")
        @Email(message = "this field must be an email pattern")
        String email,

        @NotBlank(message = "password name must not be empty")
        @Size(min = 8, message = "password length must be greater 8 letters")
        String password
) {
}
