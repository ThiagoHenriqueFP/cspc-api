package uol.compass.cspcapi.application.api.student.dto;

// user -> student

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateStudentDTO {
    // User
    @NotBlank(message = "first name must not be empty")
    @Min(value = 3, message = "first name must be greater than 3 letters")
    private String firstName;
    @NotBlank(message = "last name must not be empty")
    @Min(value = 3, message = "last name must be greater than 3 letters")
    private String lastName;
    @NotBlank(message = "email name must not be empty")
    @Email(message = "this field must be an email pattern")
    private String email;

    @NotBlank(message = "password name must not be empty")
    @Min(value = 8, message = "password length must be greater 8 letters")
    private String password;

//    request para salvar qualquer tipo de usuário
//    (como coincidentemetne as relações de usuário não tem atributos
//    extras, pode ser utilizado desta forma para salavar instrutor,
//    coordenador e scrum master também)
//
//    {
//        "firstName": "Mak",
//        "lastName": "Jesus",
//        "email": "mak.jesus@mail.com"
//    }

    // --

    public CreateStudentDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }
}
