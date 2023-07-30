package uol.compass.cspcapi.application.api.coordinator.dto;

import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;

public class UpdateCoordinatorDTO {
    @NotBlank(message = "user must not be empty")
    private UpdateUserDTO user;

    public UpdateCoordinatorDTO() {}
    public UpdateCoordinatorDTO(UpdateUserDTO user) {
        this.user = user;
    }

    public UpdateUserDTO getUser() {
        return user;
    }
}
