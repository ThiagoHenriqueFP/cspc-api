package uol.compass.cspcapi.domain.instructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class InstructorServiceTest {

    private static InstructorRepository instructorRepository;
    private static UserRepository userRepository;
    private static RoleRepository roleRepository;
    private static UserService userService;
    private static RoleService roleService;
    private static InstructorService instructorService;
    private static UserService mockUserService;
    private static InstructorService mockInstructorService;

    @BeforeAll()
    public static void setUp(){
        userRepository = mock(UserRepository.class);
        instructorRepository = mock(InstructorRepository.class);
        roleRepository = mock(RoleRepository.class);
        mockUserService = mock(UserService.class);
        mockInstructorService = mock(InstructorService.class);

        userService = new UserService(userRepository, new PasswordEncoder());
        roleService = new RoleService(roleRepository);
//        instructorService = new InstructorService(instructorRepository, userService, new PasswordEncoder(), roleService);
        instructorService = new InstructorService(instructorRepository, mockUserService, new PasswordEncoder(), roleService);
    }

    @AfterEach()
    void down(){
        reset(userRepository);
        reset(roleRepository);
        reset(instructorRepository);
        reset(mockInstructorService);
    }

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

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
//    @Test
//    public void testSave_Success() {
//        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();
//        User userDTO = new User("John", "Doe", "johndoe@compass.com", "password");
//        instructorDTO.setUser(userDTO);
//
//        when(userService.findByEmail("johndoe@compass.com")).thenReturn(Optional.empty());
//
//        Instructor savedInstructor = new Instructor();
//        Classroom classroom = new Classroom();
//
//        savedInstructor.setId(1L);
//        savedInstructor.setUser(new User("John", "Doe", "johndoe@compass.com", "hashed_password"));
//        savedInstructor.setClassroom(classroom);
//
//        when(instructorRepository.save(any(Instructor.class))).thenReturn(savedInstructor);
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userDTO));
//        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role("ROLE_INSTRUCTOR")));
////        when(mockService.mapToResponseInstructor(savedInstructor)).thenReturn(
////                new ResponseInstructorDTO(1L, new ResponseUserDTO(
////                        userDTO.getId(),
////                        userDTO.getFirstName(),
////                        userDTO.getLastName(),
////                        userDTO.getEmail()
////                )
////        ));
//
//        ResponseInstructorDTO result = instructorService.save(instructorDTO);
//
//        assertEquals(savedInstructor.getId(), result.getId());
//        assertEquals(savedInstructor.getUser().getFirstName(), result.getUser().getFirstName());
//        assertEquals(savedInstructor.getUser().getLastName(), result.getUser().getLastName());
//        assertEquals(savedInstructor.getUser().getEmail(), result.getUser().getEmail());
//    }

    @Test
    public void testSave_Success() {
        User user = new User("John", "Doe", "johndoe@compass.com", "password");
        Instructor instructor = new Instructor();
        Classroom classroom = new Classroom();
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();

        instructor.setId(1L);
        instructor.setUser(user);
        instructor.setClassroom(classroom);
        classroom.setId(1L);
        instructorDTO.setUser(user);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role("ROLE_INSTRUCTOR")));
        when(mockUserService.mapToResponseUser(user)).thenReturn(new ResponseUserDTO());
        when(instructorService.mapToResponseInstructor(instructor)).thenReturn(new ResponseInstructorDTO());

        ResponseInstructorDTO result = instructorService.save(instructorDTO);

        assertEquals(instructor.getId(), result.getId());
        assertEquals(instructor.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(instructor.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(instructor.getUser().getEmail(), result.getUser().getEmail());
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

        Instructor instructor = new Instructor(instructorId, new User("Jane", "Smith", "janesmith@compass.com", "senha"));

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        ResponseInstructorDTO result = instructorService.update(instructorId, instructorDTO);

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
        User user = new User("John", "Doe", "johndoe@compass.com", "password");
        user.setId(1L);
        Instructor instructor = new Instructor(1L, user);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ResponseInstructorDTO result = instructorService.getById(1L);

        assertEquals(instructor.getId(), result.getId());
        /*
        assertEquals(instructor.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(instructor.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(instructor.getUser().getEmail(), result.getUser().getEmail());
        */
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

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> instructorService.getAllInstructorsById(instructorIds));

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

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> instructorService.deleteById(instructorId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("instructor not found", exception.getReason());
    }


    //Attribute instructor to classroom
    @Test
    public void testAttributeInstructorsToClassroom_Success() {
        Classroom classroom = new Classroom( "Classroom");

        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor(1L, new User("John", "Doe", "john@compass.com", "password")));
        instructors.add(new Instructor(2L, new User("Jane", "Smith", "jane@compass.com", "password")));

        when(instructorRepository.saveAll(anyList())).thenReturn(instructors);
        when(instructorRepository.findAllByIdIn(anyList())).thenReturn(instructors);

        List<ResponseInstructorDTO> result = instructorService.attributeInstructorsToClassroom(classroom, instructors);

        for (Instructor instructor : instructors) {
            assertEquals(classroom, instructor.getClassroom());
        }

        assertEquals(instructors.size(), result.size());
//        result.forEach(
//                it -> {
//                    assertEquals(1L, it.getId());
//                    assertEquals(2L, it.getId());
//                }
//        );
        verify(instructorRepository).findAllByIdIn(anyList());
        verify(instructorRepository).saveAll(anyList());
    }

    @Test
    public void testAttributeInstructorsToClassroom_Failure() {
        Classroom classroom = new Classroom( "Classroom");
        User user = new User("John", "Doe", "test@mail.com", "12344321");
        Instructor i1 = new Instructor(user);
        Instructor i2 = new Instructor(user);

        i1.setId(1L);
        i2.setId(2L);

        List<Instructor> instructors = new ArrayList<>();
        instructors.add(i1);
        instructors.add(i2);

        List<Long> idList = List.of(1L);

        when(instructorRepository.findAllByIdIn(idList)).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class,
                () -> instructorService.attributeInstructorsToClassroom(classroom, instructors)
        );
    }



}







