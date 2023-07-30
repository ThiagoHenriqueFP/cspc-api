package uol.compass.cspcapi.domain.coordinator;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.user.User;

@Entity
@Table(name = "coordinators")
public class Coordinator {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    public Coordinator() {
    }

    public Coordinator(User user) {
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
