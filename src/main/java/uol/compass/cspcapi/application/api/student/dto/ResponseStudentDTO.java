package uol.compass.cspcapi.application.api.student.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.grade.Grade;

public class ResponseStudentDTO {
    private Long id;
    private ResponseUserDTO user;
    private Grade grades;
    private Squad squad;
    private Classroom classroom;

    public ResponseStudentDTO(Long id, ResponseUserDTO user, Grade grades) {
        this.id = id;
        this.user = user;
        this.grades = grades;
    }

    public ResponseStudentDTO(Long id, ResponseUserDTO user, Grade grades, Squad squad, Classroom classroom) {
        this.id = id;
        this.user = user;
        this.grades = grades;
        this.squad = squad;
        this.classroom = classroom;
    }

    public Long getId() {
        return id;
    }

    public ResponseUserDTO getUser() {
        return user;
    }

    public Grade getGrades() {
        return grades;
    }

    public Squad getSquad() {
        return squad;
    }

    public Classroom getClassroom() {
        return classroom;
    }
}
