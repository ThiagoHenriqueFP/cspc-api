package uol.compass.cspcapi.domain.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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
    private static final Student STUDENT_2 = new Student(USER_1);


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
        STUDENT_1.setId(1L);
        STUDENT_2.setId(2L);
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

    @Test
    void getByIdFail(){
        when(studentRepository.findById(1L)).thenThrow(ResponseStatusException.class);

        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> studentService.getById(0L)
        );

        assertEquals(404, response.getStatusCode().value());
        assertEquals("user not found", response.getReason());
    }

    @Test
    void getAll() {
        List<Student> list = new ArrayList<>();
        list.add(STUDENT_1);
        list.add(STUDENT_2);

        when(studentRepository.findAll()).thenReturn(list);

        List<ResponseStudentDTO> response = studentService.getAll();

        assertEquals(list.size(), response.size());
    }

    @Test
    void getAllIncorrectSize() {
        List<Student> list = new ArrayList<>();
        list.add(STUDENT_1);

        when(studentRepository.findAll()).thenReturn(list);

        List<ResponseStudentDTO> response = studentService.getAll();

        list.remove(STUDENT_1);

        assertNotEquals(list.size(), response.size());
        assertEquals(0, list.size());
        assertEquals(1, response.size());
    }

    @Test
    void update() {
        Student student = new Student(
                STUDENT_1.getUser()
        );

        student.getUser().setEmail("testePut@mail.com");

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT_1));
        when(studentRepository.save(any())).thenReturn(student);

        ResponseStudentDTO updatedStudent = studentService.update(1L, new UpdateStudentDTO(
                new UpdateUserDTO(
                        USER_1.getFirstName(),
                        USER_1.getLastName(),
                        USER_1.getEmail(),
                        USER_1.getPassword()
                )
        ));

        assertEquals(student.getUser().getEmail(), updatedStudent.getUser().getEmail());
    }

    @Test
    void updateFailNotFound(){
        when(studentRepository.findById(1L)).thenReturn(Optional.of(STUDENT_1)).thenThrow(ResponseStatusException.class);

        Student student = new Student(
                STUDENT_1.getUser()
        );

        student.getUser().setEmail("testePut@mail.com");


        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> studentService.update(0L, new UpdateStudentDTO(
                new UpdateUserDTO(
                        USER_1.getFirstName(),
                        USER_1.getLastName(),
                        USER_1.getEmail(),
                        USER_1.getPassword()
                )
        )));

        assertEquals(404, response.getStatusCode().value());
        assertEquals("student not found", response.getReason());
    }

    @Test
    void delete() {
       when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT_1));

       studentService.delete(anyLong());

       verify(studentRepository).delete(STUDENT_1);
    }

    @Test
    void deleteFailNotFound() {
        when(studentRepository.findById(1L)).thenThrow(ResponseStatusException.class);

        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> studentService.delete(anyLong())
        );

        assertEquals(404, response.getStatusCode().value());
        assertEquals("student not found", response.getReason());
    }

    @Test
    void getAllStudentsById() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);

        when(studentRepository.findAllByIdIn(list)).thenReturn(List.of(STUDENT_1, STUDENT_2));

        List<Student> response = studentService.getAllStudentsById(list);

        assertEquals(list.size(), response.size());
        assertEquals(list.get(0), response.get(0).getId());
    }



    @Test
    void getAllByIdFail(){
        List<Long> list = new ArrayList<>();
        list.add(2L);
        list.add(1L);

        when(studentRepository.findAllByIdIn(list)).thenReturn(List.of(STUDENT_1, STUDENT_2));

        List<Student> response = studentService.getAllStudentsById(list);

        assertEquals(list.size(), response.size());
        assertNotEquals(list.get(1), response.get(1).getId());
        assertNotEquals(list.get(0), response.get(0).getId());
    }

    @Test
    void getAllByIdFailNotFound(){
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);

        when(studentRepository.findAllByIdIn(list)).thenReturn(List.of(STUDENT_1, STUDENT_2));
        when(studentRepository.findById(0L)).thenThrow(ResponseStatusException.class);

        List<Student> students = studentService.getAllStudentsById(list);

        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> students.forEach( it -> studentService.getById(it.getId()))
        );

        assertEquals(404, response.getStatusCode().value());
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