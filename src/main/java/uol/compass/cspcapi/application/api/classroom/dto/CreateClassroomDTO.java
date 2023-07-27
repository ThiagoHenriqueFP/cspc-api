package uol.compass.cspcapi.application.api.classroom.dto;

import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;

import java.util.List;

public class CreateClassroomDTO {

    private String title;
    private Long coordinator;


    public String getTitle() {
        return title;
    }

    public Long getCoordinator() {
        return coordinator;
    }

}
