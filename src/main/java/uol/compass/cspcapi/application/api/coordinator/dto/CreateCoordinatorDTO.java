package uol.compass.cspcapi.application.api.coordinator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.domain.user.User;

public class CreateCoordinatorDTO {
    // User
    @NotBlank(message = "user must not be empty")
    private CreateUserDTO user;

    public CreateCoordinatorDTO() {
    }

    public CreateCoordinatorDTO(CreateUserDTO user) {
        this.user = user;
    }

    public CreateUserDTO getUser() {
        return user;
    }

    public void setUser(CreateUserDTO user) {
        this.user = user;
    }
}
