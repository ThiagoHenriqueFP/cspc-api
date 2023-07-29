package uol.compass.cspcapi.application.api.squad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateSquadDTO {
    @NotBlank(message = "first name must not be empty")
    @Min(value = 3, message = "first name must be greater than 3 letters")
    private String name;

    private Long idClassroom;

    public CreateSquadDTO() {
    }

    public CreateSquadDTO(String name) {
        this.name = name;
    }

    public CreateSquadDTO(String name, Long idClassroom) {
        this.name = name;
        this.idClassroom = idClassroom;
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
