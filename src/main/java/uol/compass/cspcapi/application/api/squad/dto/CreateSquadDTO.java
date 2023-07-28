package uol.compass.cspcapi.application.api.squad.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.student.Student;

import java.util.ArrayList;
import java.util.List;

public class CreateSquadDTO {
    @NotBlank(message = "first name must not be empty")
    @Min(value = 3, message = "first name must be greater than 3 letters")
    private String name;

    private Long idClassroom;

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
