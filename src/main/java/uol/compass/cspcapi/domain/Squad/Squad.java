package uol.compass.cspcapi.domain.Squad;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.student.Student;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "squads")
public class Squad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private List<Student> students;

    public Squad() {
        this.students = new ArrayList<>();
    }

    public Squad(String name) {
        this.name = name;
        this.students = new ArrayList<>();
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
