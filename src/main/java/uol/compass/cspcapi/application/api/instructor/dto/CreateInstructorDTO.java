package uol.compass.cspcapi.application.api.instructor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.user.User;

public class CreateInstructorDTO {
    // User
    @NotBlank(message = "user must not be empty")
    private User user;

    public CreateInstructorDTO() {
    }

    public CreateInstructorDTO(long id, User user) {
    }

    public CreateInstructorDTO(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
