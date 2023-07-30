package uol.compass.cspcapi.domain.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    // Aqui eu não posso acessar diretamente o user repository
    // Essa abordagem serve para manter as classes protegidas
    private final UserService userService;

    private final PasswordEncoder passwordEncrypt;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserService userService, PasswordEncoder passwordEncrypt) {
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.passwordEncrypt = passwordEncrypt;
    }


    @Transactional
    public ResponseStudentDTO save(CreateStudentDTO student) {
        Optional<User> alreadyExists = userService.findByEmail(student.getEmail());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User newUser = new User(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                passwordEncrypt.encoder().encode(student.getPassword())
        );

        User savedUSer = userService.saveUser(newUser);

        Student newStudent = new Student(
                savedUSer
        );
        Student studentDb = studentRepository.save(newStudent);

        return new ResponseStudentDTO(
                studentDb.getUser().getId(),
                studentDb.getUser().getFirstName(),
                studentDb.getUser().getLastName(),
                studentDb.getUser().getEmail()
        );
    }

    public Student getById(Long id){
        return studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public List<Student> getAll(){
        return studentRepository.findAll();
    }

    public ResponseStudentDTO update(Long id, UpdateStudentDTO studentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        User user = student.getUser();

        user.setFirstName(studentDTO.getFirstName());
        user.setLastName(studentDTO.getLastName());
        user.setEmail(studentDTO.getEmail());

        student.setUser(user);

        studentRepository.save(student);

        return new ResponseStudentDTO (
                student.getId(),
                student.getUser().getFirstName(),
                student.getUser().getLastName(),
                student.getUser().getEmail()
        );
    }

    public boolean delete(Long id){
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );

        studentRepository.delete(student);

        return true;
    }

    public List<Student> getAllStudentsById(List<Long> studentsIds) {
        return studentRepository.findAllByIdIn(studentsIds);
    }

    @Transactional
    public List<Student> attributeStudentsToSquad(Squad squad, List<Student> students) {
        for (Student student : students) {
            student.setSquad(squad);
        }
        return studentRepository.saveAll(students);
    }

    @Transactional
    public List<Student> attributeStudentsToClassroom(Classroom classroom, List<Student> students) {
        for (Student student: students) {
            student.setClassroom(classroom);
        }
        return studentRepository.saveAll(students);
    }

    public List<Student> getAllStudentsBySquadId(Long squadId) {
        return studentRepository.findAllBySquadId(squadId);
    }


}
