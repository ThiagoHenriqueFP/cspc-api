package uol.compass.cspcapi.application.api.user.dto;

public class ResponseUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public ResponseUserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
