package uol.compass.cspcapi.application.api.scrumMaster.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;

public class ResponseScrumMasterDTO {
    private Long id;
    private ResponseUserDTO user;
    private Classroom classroom;

    public ResponseScrumMasterDTO(Long id, ResponseUserDTO user) {
        this.id = id;
        this.user = user;
    }

    public ResponseScrumMasterDTO(Classroom classroom) {
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
