package uol.compass.cspcapi.application.api.student.dto;

// user -> student

import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;

public record CreateStudentDTO(
        @NotBlank(message = "user must not be empty")
        CreateUserDTO user
) {
}
