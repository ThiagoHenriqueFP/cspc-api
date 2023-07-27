package uol.compass.cspcapi.application.api.coordinator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateCoordinatorDTO {
    @NotBlank(message = "first name must not be empty")
    @Min(value = 3, message = "first name must be greater than 3 letters")
    private String firstName;
    @NotBlank(message = "last name must not be empty")
    @Min(value = 3, message = "last name must be greater than 3 letters")
    private String lastName;
    @NotBlank(message = "email name must not be empty")
    @Email(message = "this field must be an email pattern")
    private String email;

    public UpdateCoordinatorDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
