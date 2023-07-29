package uol.compass.cspcapi.application.api.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;
import uol.compass.cspcapi.domain.grade.Grade;
import uol.compass.cspcapi.domain.user.User;

public class UpdateStudentDTO {
    @NotBlank(message = "first name must not be empty")
    @Min(value = 3, message = "first name must be greater than 3 letters")
    private String firstName;
    @NotBlank(message = "last name must not be empty")
    @Min(value = 3, message = "last name must be greater than 3 letters")
    private String lastName;
    @NotBlank(message = "email name must not be empty")
    @Email(message = "this field must be an email pattern")
    private String email;
    private UpdateGradeDTO grades;
    @NotBlank(message = "user must not be empty")
    private User user;

    public UpdateStudentDTO() {
    }

    public UpdateStudentDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UpdateStudentDTO(UpdateGradeDTO grades) {
        this.grades = grades;
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

    public UpdateGradeDTO getGrades() {
        return grades;
    }

    public User getUser() {
        return user;
    }
}
