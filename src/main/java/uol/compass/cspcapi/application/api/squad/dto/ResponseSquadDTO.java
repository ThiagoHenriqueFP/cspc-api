package uol.compass.cspcapi.application.api.squad.dto;

import uol.compass.cspcapi.domain.student.Student;

import java.util.List;

public class ResponseSquadDTO {

    private Long id;

    private String name;

    private List<Student> students;

    public ResponseSquadDTO() {
    }

    public ResponseSquadDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ResponseSquadDTO(Long id, String name, List<Student> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
