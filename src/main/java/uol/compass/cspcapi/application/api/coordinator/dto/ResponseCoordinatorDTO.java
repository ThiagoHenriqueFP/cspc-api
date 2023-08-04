package uol.compass.cspcapi.application.api.coordinator.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;

public class ResponseCoordinatorDTO {
    private Long id;
    private ResponseUserDTO user;

    public ResponseCoordinatorDTO() {
    }

    public ResponseCoordinatorDTO(Long id, ResponseUserDTO user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public ResponseUserDTO getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(ResponseUserDTO user) {
        this.user = user;
    }
}
