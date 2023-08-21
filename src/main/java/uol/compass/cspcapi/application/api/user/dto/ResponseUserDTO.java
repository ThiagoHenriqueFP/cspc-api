package uol.compass.cspcapi.application.api.user.dto;

public record ResponseUserDTO(
        Long id,
         String firstName,
         String lastName,
         String email
) {}
