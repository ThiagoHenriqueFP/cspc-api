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

    public String getName() {
        return name;
    }

    public Long getIdClassroom() {
        return idClassroom;
    }

    public List<Long> getStudentsIds() {
        return studentsIds;
    }
}
