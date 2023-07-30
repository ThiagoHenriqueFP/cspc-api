package uol.compass.cspcapi.application.api.squad.dto;

import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.domain.student.Student;

import java.util.List;

public class ResponseSquadDTO {

    private Long id;

    private String name;

    private List<ResponseStudentDTO> students;

    public ResponseSquadDTO() {
    }

    public ResponseSquadDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ResponseSquadDTO(Long id, String name, List<ResponseStudentDTO> students) {
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

    public List<ResponseStudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<ResponseStudentDTO> students) {
        this.students = students;
    }
}
