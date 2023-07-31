package uol.compass.cspcapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorRepository;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;


    @Mock
    private PasswordEncoder passwordEncrypt;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private InstructorService instructorService;


    @BeforeEach
    public void setup() {
        /*userService = new UserService(userRepository, new PasswordEncoder());
        instructorService = new InstructorService(instructorRepository, userService, new PasswordEncoder(), roleService);
        idList = new ArrayList<>();
        */
        MockitoAnnotations.openMocks(this);
    }

    private List<Long> idList;


    /*
    save : Success/Failure
    getById : Success/Failure
    getAll : Success/Failure
    update : Success/Failure
    deleteById : Success/Failure
    getAllInstructorsById : Success/Failure
    attributeInstructorsToClassroom : Success/Failure
    */

    //Save
    @Test
    public void testSave_Success() {
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();
        User userDTO = new User("John", "Doe", "johndoe@compass.com", "password");
        instructorDTO.setUser(userDTO);

        when(userService.findByEmail("johndoe@compass.com")).thenReturn(Optional.empty());

        Instructor savedInstructor = new Instructor();
        savedInstructor.setId(1L);
        savedInstructor.setUser(new User("John", "Doe", "johndoe@compass.com", "hashed_password"));

        when(instructorRepository.save(any(Instructor.class))).thenReturn(savedInstructor);

        ResponseInstructorDTO result = instructorService.save(instructorDTO);

        assertEquals(savedInstructor.getId(), result.getId());
        assertEquals(savedInstructor.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(savedInstructor.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(savedInstructor.getUser().getEmail(), result.getUser().getEmail());
    }

    @Test
    public void testSave_Failure() {
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();
        User userDTO = new User("John", "Doe", "johndoe@compass.com", "password");
        instructorDTO.setUser(userDTO);

        User existingUser = new User("Jane", "Smith", "johndoe@compass.com", "hashed_password");

        when(userService.findByEmail("johndoe@compass.com")).thenReturn(Optional.of(existingUser));

        assertThrows(ResponseStatusException.class, () -> instructorService.save(instructorDTO));
    }


    //Update instructor
    @Test
    public void testUpdate_Success() {
        Long instructorId = 1L;
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO();
        instructorDTO.setUser(new User("John", "Doe", "johndoe@compass.com", "senha" ));

        // Criar um objeto simulado do tipo Instructor com ID 1
        Instructor instructor = new Instructor(instructorId, new User("Jane", "Smith", "janesmith@compass.com", "senha"));

        // Configurar o comportamento simulado do instructorRepository.findById para retornar o instructor simulado
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        // Configurar o comportamento simulado do instructorRepository.save para retornar o instructor atualizado
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Executar o método sendo testado
        ResponseInstructorDTO result = instructorService.update(instructorId, instructorDTO);

        // Verificar se o objeto ResponseInstructorDTO retornado possui as informações corretas do instructor atualizado
        assertEquals(instructorId, result.getId());
        assertEquals(instructorDTO.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(instructorDTO.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(instructorDTO.getUser().getEmail(), result.getUser().getEmail());
    }

    @Test
    public void testUpdate_Failure() {
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO( new User("John", "Doe", "johndoe@compass.com", "senha"));

        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> instructorService.update(1L, instructorDTO));
    }


    //GetById
    @Test
    public void testGetById_Success() {
        Instructor instructor = new Instructor(1L, new User("John", "Doe", "johndoe@compass.com", "password"));

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        ResponseInstructorDTO result = instructorService.getById(1L);

        assertEquals(instructor.getId(), result.getId());
        assertEquals(instructor.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(instructor.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(instructor.getUser().getEmail(), result.getUser().getEmail());
    }

    @Test
    public void testGetInstructorById_Failure() {
        Long instructorId = 2L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> instructorService.getById(instructorId));
    }


    //GetAll
    @Test
    public void testGetAll_Success() {
        Instructor instructor1 = new Instructor(new User("John", "Doe", "johndoe@compass.com", "senha"));
        Instructor instructor2 = new Instructor(new User("Jane", "Doe", "janedoe@compass.com", "senha"));
        List<Instructor> mockInstructors = Arrays.asList(instructor1, instructor2);

        when(instructorRepository.findAll()).thenReturn(mockInstructors);

        List<ResponseInstructorDTO> result = instructorService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    public void testGetAll_Failure() {
        when(instructorRepository.findAll()).thenReturn(Collections.emptyList());

        instructorService.getAll();
    }


    //GetAllById
    @Test
    public void testGetAllInstructorsById_Success() {
        List<Long> instructorIds = Arrays.asList(1L, 2L, 3L);

        List<Instructor> mockInstructors = Arrays.asList(new Instructor(), new Instructor(), new Instructor());

        when(instructorRepository.findAllByIdIn(instructorIds)).thenReturn(mockInstructors);

        List<Instructor> result = instructorService.getAllInstructorsById(instructorIds);

        assertEquals(mockInstructors, result);
    }

    @Test
    public void testGetAllInstructorsById_Failure() {
        List<Long> instructorIds = Arrays.asList(1L, 2L, 3L);

        when(instructorRepository.findAllByIdIn(instructorIds)).thenReturn(Arrays.asList(new Instructor(), new Instructor()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            instructorService.getAllInstructorsById(instructorIds);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("One or more instructors not found", exception.getReason());
    }



    //DeleteById
    @Test
    public void testDeleteById_Success() {
        Long instructorId = 1L;

        Instructor instructor = new Instructor(new User("John", "Doe", "johndoe@compass.com", "senha"));
        instructor.setId(instructorId);

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        instructorService.deleteById(instructorId);

        verify(instructorRepository).delete(instructor);
    }

    @Test
    public void testDeleteById_Failure() {
        Long instructorId = 1L;

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ResponseStatusException.class, () -> {
            instructorService.deleteById(instructorId);
        });

        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("instructor not found", ((ResponseStatusException) exception).getReason());
    }


    //Attribute instructor to classroom
    @Test
    public void testAttributeInstructorsToClassroom_Success() {
        Classroom classroom = new Classroom( "Mathematics");

        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor(1L, new User("John", "Doe", "john@compass.com", "password")));
        instructors.add(new Instructor(2L, new User("Jane", "Smith", "jane@compass.com", "password")));

        when(instructorRepository.saveAll(anyList())).thenReturn(instructors);

        List<ResponseInstructorDTO> result = instructorService.attributeInstructorsToClassroom(classroom, instructors);

        for (Instructor instructor : instructors) {
            assertEquals(classroom, instructor.getClassroom());
        }

        assertEquals(instructors.size(), result.size());
        for (int i = 0; i < instructors.size(); i++) {
            Instructor instructor = instructors.get(i);
            ResponseInstructorDTO responseDTO = result.get(i);

            assertEquals(instructor.getId(), responseDTO.getId());
            assertEquals(instructor.getUser().getFirstName(), responseDTO.getUser().getFirstName());
            assertEquals(instructor.getUser().getLastName(), responseDTO.getUser().getLastName());
            assertEquals(instructor.getUser().getEmail(), responseDTO.getUser().getEmail());
        }
    }

    @Test
    public void testAttributeInstructorsToClassroom_Failure() {
        Classroom classroom = new Classroom( "Classroom");

        List<Instructor> instructors = new ArrayList<>();

        assertThrows(ResponseStatusException.class, () -> instructorService.attributeInstructorsToClassroom(classroom, instructors));
    }



}







