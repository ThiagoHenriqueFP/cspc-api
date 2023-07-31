package uol.compass.cspcapi.application.api.scrumMaster.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;

public class UpdateScrumMasterDTO {
    @NotBlank(message = "user must not be empty")
    private User user;
    private Classroom classroom;

    public UpdateScrumMasterDTO() {}

    public UpdateScrumMasterDTO(User user, Classroom classroom) {
        this.user = user;
        this.classroom = classroom;
    }

    public User getUser() {
        return user;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}
