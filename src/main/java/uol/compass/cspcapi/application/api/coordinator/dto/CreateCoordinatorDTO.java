package uol.compass.cspcapi.application.api.coordinator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.user.User;

public class CreateCoordinatorDTO {
    // User
    @NotBlank(message = "user must not be empty")
    private User user;

    public CreateCoordinatorDTO() {
    }

    public CreateCoordinatorDTO(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
