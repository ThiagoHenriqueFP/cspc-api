package uol.compass.cspcapi.application.api.classroom.dto;

import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;


import java.util.List;

public class ResponseClassroomDTO {

    private Long id;
    private String title;
    //private Coordinator coordinator;
    private ResponseCoordinatorDTO coordinator;
    private List<ResponseStudentDTO> students;
    private List<ResponseInstructorDTO> instructors;
    private List<ResponseScrumMasterDTO> scrumMasters;
    private List<ResponseSquadDTO> squads;

    public ResponseClassroomDTO() {
    }

    public ResponseClassroomDTO(Long id, String title, ResponseCoordinatorDTO coordinator, List<ResponseStudentDTO> students, List<ResponseInstructorDTO> instructors, List<ResponseScrumMasterDTO> scrumMasters, List<ResponseSquadDTO> squads) {
        this.id = id;
        this.title = title;
        this.coordinator = coordinator;
        this.students = students;
        this.instructors = instructors;
        this.scrumMasters = scrumMasters;
        this.squads = squads;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ResponseCoordinatorDTO getCoordinator() {
        return coordinator;
    }

    public List<ResponseStudentDTO> getStudents() {
        return students;
    }

    public List<ResponseInstructorDTO> getInstructors() {
        return instructors;
    }

    public List<ResponseScrumMasterDTO> getScrumMasters() {
        return scrumMasters;
    }

    public List<ResponseSquadDTO> getSquads() {
        return squads;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoordinator(ResponseCoordinatorDTO coordinator) {
        this.coordinator = coordinator;
    }

    public void setStudents(List<ResponseStudentDTO> students) {
        this.students = students;
    }

    public void setInstructors(List<ResponseInstructorDTO> instructors) {
        this.instructors = instructors;
    }

    public void setScrumMasters(List<ResponseScrumMasterDTO> scrumMasters) {
        this.scrumMasters = scrumMasters;
    }

    public void setSquads(List<ResponseSquadDTO> squads) {
        this.squads = squads;
    }
}
