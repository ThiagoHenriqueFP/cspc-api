package uol.compass.cspcapi.application.api.instructor.dto;

import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;

public class UpdateInstructorDTO {
    private User user;
    private Classroom classroom;

    public UpdateInstructorDTO() {}

    public UpdateInstructorDTO(User user, Classroom classroom) {
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
