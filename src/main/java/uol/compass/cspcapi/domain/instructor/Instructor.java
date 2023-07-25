package uol.compass.cspcapi.domain.instructor;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.user.User;

@Entity
@Table(name = "instructors")
public class Instructor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    public Instructor() {
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
}
