package uol.compass.cspcapi.application.api.squad.dto;

import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;

import java.util.ArrayList;
import java.util.List;

public class UpdateSquadDTO {

    private String name;

    private UpdateClassroomDTO classroom;

    private List<Long> studentsIds;

    public UpdateSquadDTO() {
    }

    public UpdateSquadDTO(String name) {
        this.name = name;
    }

    public UpdateSquadDTO(String name, UpdateClassroomDTO classroom) {
        this.name = name;
        this.classroom = classroom;
    }

    public UpdateSquadDTO(List<Long> studentsIds) {
        this.studentsIds = studentsIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UpdateClassroomDTO getClassroom() {
        return classroom;
    }

    public void setClassroom(UpdateClassroomDTO classroom) {
        this.classroom = classroom;
    }

    public List<Long> getStudentsIds() {
        return studentsIds;
    }

    public void setStudentsIds(List<Long> studentsIds) {
        this.studentsIds = studentsIds;
    }
}
