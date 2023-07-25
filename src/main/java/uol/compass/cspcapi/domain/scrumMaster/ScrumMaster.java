package uol.compass.cspcapi.domain.scrumMaster;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.user.User;

@Entity
@Table(name = "scrum_masters")
public class ScrumMaster {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    public ScrumMaster() {
    }

    public ScrumMaster(User user) {
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
