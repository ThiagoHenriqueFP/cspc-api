package uol.compass.cspcapi.application.api.instructor.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;

public class ResponseInstructorDTO {
    private Long id;
    private ResponseUserDTO user;
    private Classroom classroom;

    public ResponseInstructorDTO(Long id, ResponseUserDTO user) {
        this.id = id;
        this.user = user;
    }

    public ResponseInstructorDTO(Classroom classroom) {
        this.classroom = classroom;
    }

    public Long getId() {
        return id;
    }

    public ResponseUserDTO getUser() {
        return user;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}
