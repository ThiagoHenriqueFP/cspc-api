package uol.compass.cspcapi.application.api.instructor.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;

public class ResponseInstructorDTO {
    private Long id;
    private ResponseUserDTO user;
    private Long classroomId;

    public ResponseInstructorDTO(Long id, ResponseUserDTO user) {
        this.id = id;
        this.user = user;
    }

    public ResponseInstructorDTO(Long id, ResponseUserDTO user, Long classroomId) {
        this.id = id;
        this.user = user;
        this.classroomId = classroomId;
    }

    public Long getId() {
        return id;
    }

    public ResponseUserDTO getUser() {
        return user;
    }

    public Long getClassroomId() {
        return classroomId;
    }
}
