package uol.compass.cspcapi.domain.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    // Aqui eu não posso acessar diretamente o user repository
    // Essa abordagem serve para manter as classes protegidas
    private final UserService userService;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }


//  User password must be in plain text
    public Student save(CreateStudentDTO student) {
        User newUser = new User(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getPassword()
        );

        User savedUSer = userService.saveUser(newUser);

        Student newStudent = new Student(
                savedUSer
        );

        return studentRepository.save(newStudent);


    }
}
