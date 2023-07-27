package uol.compass.cspcapi.application.api.squad.dto;

public class UpdateSquadDTO {

    private String name;

    public UpdateSquadDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
