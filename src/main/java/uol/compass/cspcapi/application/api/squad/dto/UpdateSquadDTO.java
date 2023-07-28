package uol.compass.cspcapi.application.api.squad.dto;

public class UpdateSquadDTO {

    private String name;

    private Long idClassroom;

    public UpdateSquadDTO(String name, Long idClassroom) {
        this.name = name;
        this.idClassroom = idClassroom;
    }

    public String getName() {
        return name;
    }

    public Long getIdClassroom() {
        return idClassroom;
    }
}
