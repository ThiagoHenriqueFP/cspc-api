package uol.compass.cspcapi.domain.instructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;

@Entity
@Table(name = "instructors")
public class Instructor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "classroom_id", referencedColumnName = "id")
    private Classroom classroom;

    public Instructor() {
    }

    public Instructor(long id, User user) {
    }

    public Instructor(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
}
