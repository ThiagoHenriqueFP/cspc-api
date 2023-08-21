package uol.compass.cspcapi.application.api.scrumMaster.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;

public record ResponseScrumMasterDTO(
        Long id,
        ResponseUserDTO user,
        Long classroomId
) {
}
