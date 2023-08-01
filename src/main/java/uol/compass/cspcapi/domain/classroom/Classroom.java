package uol.compass.cspcapi.domain.classroom;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
public class Classroom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    private Coordinator coordinator;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Instructor> instructors;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<ScrumMaster> scrumMasters;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private List<Squad> squads;

    public Classroom(String title, Coordinator coordinator) {
        this.title = title;
        this.coordinator = coordinator;
    }

    public Classroom() {
    }

    public Classroom(List<Student> students, List<Instructor> instructors, List<ScrumMaster> scrumMasters, List<Squad> squads) {
        this.students = students;
        this.instructors = instructors;
        this.scrumMasters = scrumMasters;
        this.squads = squads;
    }

    public Classroom(String title) {
        this.title = title;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    public List<ScrumMaster> getScrumMasters() {
        return scrumMasters;
    }

    public void setScrumMasters(List<ScrumMaster> scrumMasters) {
        this.scrumMasters = scrumMasters;
    }

    public List<Squad> getSquads() {
        return squads;
    }

    public void setSquads(List<Squad> squads) {
        this.squads = squads;
    }
}
