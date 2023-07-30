package uol.compass.cspcapi.application.api.squad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateSquadDTO {
    @NotBlank(message = "name must not be empty")
    @Min(value = 3, message = "name must be greater than 3 letters")
    private String name;

    public CreateSquadDTO() {
    }

    public CreateSquadDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
