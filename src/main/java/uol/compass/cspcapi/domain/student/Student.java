package uol.compass.cspcapi.domain.student;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.user.User;

@Entity
@Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

//    @OneToMany(mappedBy = "users")
//    private List<grade> grade;

    public Student(User user) {
        this.user = user;
    }

    public Student() {
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
