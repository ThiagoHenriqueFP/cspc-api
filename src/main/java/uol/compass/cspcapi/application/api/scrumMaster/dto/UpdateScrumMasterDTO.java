package uol.compass.cspcapi.application.api.scrumMaster.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;

public record UpdateScrumMasterDTO(
        @NotBlank(message = "user must not be empty")
        User user,
        Classroom classroom
) { }
