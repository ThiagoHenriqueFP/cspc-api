package uol.compass.cspcapi.application.api.student.dto;

// user -> student

import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;

public class CreateStudentDTO {
    // User
    @NotBlank(message = "user must not be empty")
    private CreateUserDTO user;

    public CreateStudentDTO() {
    }

    public CreateStudentDTO(CreateUserDTO user) {
        this.user = user;
    }

    public CreateUserDTO getUser() {
        return user;
    }

    public void setUser(CreateUserDTO user) {
        this.user = user;
    }
}
