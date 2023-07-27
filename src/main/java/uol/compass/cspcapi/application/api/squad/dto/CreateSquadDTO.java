package uol.compass.cspcapi.application.api.squad.dto;

public class CreateSquadDTO {

    private String name;

    private Long idClassroom;

    public CreateSquadDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getIdClassroom() {
        return idClassroom;
    }

    public void setIdClassroom(Long idClassroom) {
        this.idClassroom = idClassroom;
    }
}
