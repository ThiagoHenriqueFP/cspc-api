package uol.compass.cspcapi.domain.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private static final User USER_1 = new User(
            "primeiro",
            "segundo",
            "teste@mail.com",
            "12345678"
    );

    private static final Role ROLE_1 = new Role("ROLE_STUDENT");

    private static final Student STUDENT_1 = new Student(USER_1);


    @MockBean
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;
    @MockBean
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    @MockBean
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private CreateStudentDTO createStudent(){
        CreateUserDTO createUserDTO = new CreateUserDTO(
                USER_1.getFirstName(),
                USER_1.getLastName(),
                USER_1.getEmail(),
                USER_1.getPassword()
        );

        return new CreateStudentDTO(createUserDTO);
    }

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        userRepository = mock(UserRepository.class);
        roleRepository = createRoleRepository();

        roleService = new RoleService(roleRepository);
        userService = new UserService(userRepository, new PasswordEncoder());
        studentService = new StudentService(studentRepository, userService, new PasswordEncoder(), roleService);

        USER_1.getRoles().add(roleService.findRoleByName("ROLE_STUDENT"));
    }

    @AfterEach
    void tearDown() {
        reset(roleRepository);
        reset(userRepository);
        reset(studentRepository);
    }

    @Test
    void save() {
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT_1);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(ROLE_1));

        ResponseStudentDTO student = studentService.save(createStudent());

        assertEquals("teste@mail.com", student.getUser().getEmail());
    }

    @Test
    void saveFail(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(USER_1)).thenThrow(ResponseStatusException.class);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(ROLE_1));

        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> studentService.save(createStudent())
        );

        assertEquals(400, response.getStatusCode().value());
        assertEquals("user already exists", response.getReason());
    }

    @Test
    void getById() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT_1));

        ResponseStudentDTO student = studentService.getById(1L);

        assertEquals("teste@mail.com", student.getUser().getEmail());
    }

//    @Test
//    void getByIdFail(){
//        when(studentRepository.findById(1L)).thenThrow(ResponseStatusException.class);
//
//        Exception response = assertThrows(Exception.class,
//                () -> studentService.getById(1L)
//        );
//
//        assertEquals(404, response.getStatusCode().value());
//        assertEquals("user not found", response.getMessage());
//    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getAllStudentsById() {
    }

    @Test
    void attributeStudentsToSquad() {
    }

    @Test
    void attributeStudentsToClassroom() {
    }

    @Test
    void updateGradesFromStudent() {
    }

    @Test
    void mapToResponseStudent() {
    }

    @Test
    void mapToResponseStudents() {
    }

    private RoleRepository createRoleRepository() {
        RoleRepository mock = mock(RoleRepository.class);
        when(mock.findByName("ROLE_STUDENT")).thenReturn(Optional.of(ROLE_1));
        when(mock.findByName("ROLE_NOT_EXISTS")).thenReturn(Optional.of(ROLE_1)).thenThrow(ResponseStatusException.class);
        return mock;
    }
}