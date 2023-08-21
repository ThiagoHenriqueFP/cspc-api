package uol.compass.cspcapi.domain.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.grade.Grade;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    // Aqui eu não posso acessar diretamente o user repository
    // Essa abordagem serve para manter as classes protegidas
    private final UserService userService;

    private final PasswordEncoder passwordEncrypt;

    private final RoleService roleService;

    @Autowired
    public StudentService(StudentRepository studentRepository, UserService userService, PasswordEncoder passwordEncrypt, RoleService roleService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.passwordEncrypt = passwordEncrypt;
        this.roleService = roleService;
    }


    @Transactional
    public ResponseStudentDTO save(CreateStudentDTO student) {
        Optional<User> alreadyExists = userService.findByEmail(student.user().email());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User user = new User(
                student.user().firstName(),
                student.user().lastName(),
                student.user().email() ,
                passwordEncrypt.encoder().encode(student.user().password())
        );
      
        user.getRoles().add(roleService.findRoleByName("ROLE_STUDENT"));
        Student newStudent = new Student(user);
        Student studentDb = studentRepository.save(newStudent);

        return mapToResponseStudent(studentDb);
    }

    public ResponseStudentDTO getById(Long id){
        return mapToResponseStudent(studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        ));
    }

    public List<ResponseStudentDTO> getAll(){
        List<Student> students = studentRepository.findAll();
        List<ResponseStudentDTO> studentsNoPassword = new ArrayList<>();

        for (Student student : students) {
            studentsNoPassword.add(mapToResponseStudent(student));
        }
        return studentsNoPassword;
    }

    @Transactional
    public ResponseStudentDTO update(Long id, UpdateStudentDTO studentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        User user = student.getUser();

        user.setFirstName(studentDTO.getUser().firstName());
        user.setLastName(studentDTO.getUser().lastName());
        user.setEmail(studentDTO.getUser().email());

        student.setUser(user);

        Student updatedStudents = studentRepository.save(student);

        return mapToResponseStudent(updatedStudents);
    }

    @Transactional
    public void delete(Long id){
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        student.getUser().getRoles().removeAll(student.getUser().getRoles());

        studentRepository.delete(student);
    }

    public List<Student> getAllStudentsById(List<Long> studentsIds) {
        return studentRepository.findAllByIdIn(studentsIds);
    }

    @Transactional
    public List<ResponseStudentDTO> attributeStudentsToSquad(Squad squad, List<Student> students) {
        for (Student student : students) {
            student.setSquad(squad);
        }
        List<Student> updatedStudents = studentRepository.saveAll(students);
        List<ResponseStudentDTO> studentsNoPassword = new ArrayList<>();

        for (Student student : updatedStudents) {
            studentsNoPassword.add(mapToResponseStudent(student));
        }
        return studentsNoPassword;
    }

    @Transactional
    public List<ResponseStudentDTO> attributeStudentsToClassroom(Classroom classroom, List<Student> students) {
        for (Student student: students) {
            student.setClassroom(classroom);
        }
        List<Student> updatedStudents = studentRepository.saveAll(students);
        List<ResponseStudentDTO> studentsNoPassword = new ArrayList<>();

        for (Student student : updatedStudents) {
            studentsNoPassword.add(mapToResponseStudent(student));
        }
        return studentsNoPassword;
    }

    @Transactional
    public ResponseStudentDTO updateGradesFromStudent(Long id, UpdateStudentDTO studentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        Grade studentGrades;
        if (student.getGrades() == null) {
            studentGrades = new Grade(0.00, 0.00, 0.00, 0.00, 0.00, 0.00);
        } else {
            studentGrades = student.getGrades();
        }
        UpdateGradeDTO newGrades = studentDTO.getGrades();

        studentGrades.setCommunication(newGrades.getCommunication());
        studentGrades.setCollaboration(newGrades.getCollaboration());
        studentGrades.setAutonomy(newGrades.getAutonomy());
        studentGrades.setQuiz(newGrades.getQuiz());
        studentGrades.setIndividualChallenge(newGrades.getIndividualChallenge());
        studentGrades.setSquadChallenge(newGrades.getSquadChallenge());
        studentGrades.setFinalGrade(newGrades.getFinalGrade());

        student.setGrades(studentGrades);
        Student savedStudent = studentRepository.save(student);

        return mapToResponseStudent(savedStudent);
    }

    public ResponseStudentDTO mapToResponseStudent(Student student) {
        Long squadId;
        Long classroomId;

        if (student.getSquad() == null) {
            squadId = null;
        } else {
            squadId = student.getSquad().getId();
        }
        if (student.getClassroom() == null) {
            classroomId = null;
        } else {
            classroomId = student.getClassroom().getId();
        }

        return new ResponseStudentDTO(
                student.getId(),
                userService.mapToResponseUser(student.getUser()),
                student.getGrades(),
                squadId,
                classroomId
        );
    }

    public List<ResponseStudentDTO> mapToResponseStudents(List<Student> students) {
        List<ResponseStudentDTO> studentsNoPassword = new ArrayList<>();

        for (Student student : students) {
            studentsNoPassword.add(mapToResponseStudent(student));
        }

        return studentsNoPassword;
    }
}
