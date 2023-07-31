package uol.compass.cspcapi.application.api.classroom.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;

import java.util.List;

public class UpdateClassroomDTO {
    @NotBlank(message = "Title must not be empty")
    @Min(value = 3, message = "Title must be greater than 3 letters")
    private String title;

    private Long coordinatorId;

    private List<Long> generalUsersIds;

    public UpdateClassroomDTO(String title, Long coordinatorId) {
        this.title = title;
        this.coordinatorId = coordinatorId;
    }

    public UpdateClassroomDTO(List<Long> generalUsersIds) {
        this.generalUsersIds = generalUsersIds;
    }

    public UpdateClassroomDTO() {
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

    public List<Long> getGeneralUsersIds() {
        return generalUsersIds;
    }

    public void setGeneralUsersIds(List<Long> generalUsersIds) {
        this.generalUsersIds = generalUsersIds;
    }
}
