package uol.compass.cspcapi.application.api.student.dto;

import jakarta.validation.constraints.NotBlank;
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;

public class UpdateStudentDTO {
    private UpdateGradeDTO grades;

    @NotBlank(message = "user must not be empty")
    private UpdateUserDTO user;

    private Squad squad;
    private Classroom classroom;

    public UpdateStudentDTO() {}

    public UpdateStudentDTO(UpdateGradeDTO grades) {
        this.grades = grades;
    }

    public UpdateStudentDTO(UpdateUserDTO user) {
        this.user = user;
    }

    public UpdateStudentDTO(Squad squad) {
        this.squad = squad;
    }

    public UpdateStudentDTO(Classroom classroom) {
        this.classroom = classroom;
    }

    public UpdateGradeDTO getGrades() {
        return grades;
    }

    public UpdateUserDTO getUser() {
        return user;
    }

    public Squad getSquad() {
        return squad;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setUser(UpdateUserDTO user) {
        this.user = user;
    }
}
