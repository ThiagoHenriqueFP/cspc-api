package uol.compass.cspcapi.application.api.classroom.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateClassroomDTO {
    @NotBlank(message = "Title must not be empty")
    @Min(value = 3, message = "Title must be greater than 3 letters")
    private String title;

    private Long coordinatorId;

    public UpdateClassroomDTO(String title, Long coordinatorId) {
        this.title = title;
        this.coordinatorId = coordinatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Long coordinatorId) {
        this.coordinatorId = coordinatorId;
    }
}
