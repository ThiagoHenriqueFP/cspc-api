package uol.compass.cspcapi.application.api.squad.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdateSquadDTO {

    private String name;

    private Long idClassroom;

    private List<Long> studentsIds;

    public UpdateSquadDTO(String name, Long idClassroom) {
        this.name = name;
        this.idClassroom = idClassroom;
    }

    public UpdateSquadDTO(List<Long> studentsIds) {
        this.studentsIds = studentsIds;
    }

    public UpdateSquadDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdClassroom() {
        return idClassroom;
    }

    public void setIdClassroom(Long idClassroom) {
        this.idClassroom = idClassroom;
    }

    public List<Long> getStudentsIds() {
        return studentsIds;
    }

    public void setStudentsIds(List<Long> studentsIds) {
        this.studentsIds = studentsIds;
    }
}
