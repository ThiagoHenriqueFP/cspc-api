package uol.compass.cspcapi.domain.classroom;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
public class Classrooms {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    private Coordinator coordinator;

    @OneToMany(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ScrumMaster> scrumMasters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Squad> squads;

    public Classrooms() {
    }

    public Classrooms(String title, Coordinator coordinator) {
        this.title = title;
        this.coordinator = coordinator;
        this.students = new ArrayList<>();
        this.scrumMasters = new ArrayList<>();
        this.squads = new ArrayList<>();
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
