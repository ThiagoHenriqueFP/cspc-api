package uol.compass.cspcapi.application.api.scrumMaster.dto;

// user -> student
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.user.User;

public class CreateScrumMasterDTO {
    // User
    @NotBlank(message = "user must not be empty")
    private User user;

    public CreateScrumMasterDTO() {
    }

    public CreateScrumMasterDTO(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
