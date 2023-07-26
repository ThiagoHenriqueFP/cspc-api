package uol.compass.cspcapi.application.api.classroom.dto;

import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;

import java.util.List;

public class CreateClassroomDTO {

    private String title;
    private Long coordinator;

    private List<Student> students;

    private List<ScrumMaster> scrumMasters;

    private List<Squad> squads;

    public String getTitle() {
        return title;
    }

    public Long getCoordinator() {
        return coordinator;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<ScrumMaster> getScrumMasters() {
        return scrumMasters;
    }

    public List<Squad> getSquads() {
        return squads;
    }
}
