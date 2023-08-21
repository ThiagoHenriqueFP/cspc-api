package uol.compass.cspcapi.application.api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserDTO (
        @NotBlank(message = "first name must not be empty")
        @Min(value = 3, message = "first name must be greater than 3 letters")
        String firstName,
        @NotBlank(message = "last name must not be empty")
        @Min(value = 3, message = "last name must be greater than 3 letters")
        String lastName,
        @NotBlank(message = "email name must not be empty")
        @Email(message = "this field must be an email pattern")
        String email,

        @NotBlank(message = "password name must not be empty")
        @Min(value = 8, message = "password length must be greater 8 letters")
        String password
){}
