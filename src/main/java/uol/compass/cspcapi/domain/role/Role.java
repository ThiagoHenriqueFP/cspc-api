package uol.compass.cspcapi.domain.role;

import jakarta.persistence.*;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public String getName(){return this.name;}

    public void setName(String name){this.name = name;}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
