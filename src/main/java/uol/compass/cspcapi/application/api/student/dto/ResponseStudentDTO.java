package uol.compass.cspcapi.application.api.student.dto;

import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.grade.Grade;

public class ResponseStudentDTO {
    private Long id;
    private ResponseUserDTO user;
    private Grade grades;
    private Long squadId;
    private Long classroomId;

    public ResponseStudentDTO(Long id, ResponseUserDTO user, Grade grades) {
        this.id = id;
        this.user = user;
        this.grades = grades;
    }

    public ResponseStudentDTO(Long id, ResponseUserDTO user){
        this.id = id;
        this.user = user;
    }

    public ResponseStudentDTO(Long id, ResponseUserDTO user, Grade grades, Long squadId, Long classroomId) {
        this.id = id;
        this.user = user;
        this.grades = grades;
        this.squadId = squadId;
        this.classroomId = classroomId;
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

    public Long getSquadId() {
        return squadId;
    }

    public Long getClassroomId() {
        return classroomId;
    }
}
