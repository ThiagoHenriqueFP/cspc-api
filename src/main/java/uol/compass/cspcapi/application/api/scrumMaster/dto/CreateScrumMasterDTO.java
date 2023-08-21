package uol.compass.cspcapi.application.api.scrumMaster.dto;

import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.domain.user.User;

public record CreateScrumMasterDTO(
        @NotBlank(message = "user must not be empty")
        User user
) {}
