package uol.compass.cspcapi.application.api.scrumMaster.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;

public class ResponseScrumMasterDTO {
    private Long id;
    private ResponseUserDTO user;
    private Long classroomId;

    public ResponseScrumMasterDTO() {}

    public ResponseScrumMasterDTO(Long id, ResponseUserDTO user) {
        this.id = id;
        this.user = user;
    }

    public ResponseScrumMasterDTO(Long id, ResponseUserDTO user, Long classroomId) {
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
