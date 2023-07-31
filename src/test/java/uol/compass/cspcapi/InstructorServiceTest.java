package uol.compass.cspcapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorRepository;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    /*
    @Mock
    private PasswordEncrypt passwordEncrypt;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepository, passwordEncoder);
        instructorService = new InstructorService(instructorRepository, userService, passwordEncrypt);
        idList = new ArrayList<>();
    }
    */


    private List<Long> idList;

    /*
    save : Success/Failure
    getById : Success/Failure
    getAll : Success/Failure
    update
    deleteById : Success/Failure
    getAllInstructorsById : Success/Failure
    attributeInstructorsToClassroom
    */

    //Save
    @Test
    public void saveInstructor_Success() {
        User user = new User("John", "Doe", "johndoe@compass.com", "senha");
        CreateInstructorDTO instructor = new CreateInstructorDTO(user);
        ResponseInstructorDTO response = instructorService.save(instructor);
        if (response != null) {
            idList.add(response.getId());
        }
        Assertions.assertThat(response).isNotEqualTo(null);
    }

    @Test
	public void saveInstructor_Failure() {
        User user = new User("John", "Doe", "johndoe@compass.com", "senha");
		CreateInstructorDTO instructor = new CreateInstructorDTO(user);
		ResponseInstructorDTO response = null;
		boolean error = false;
		try {
			response = instructorService.save(instructor);

		} catch (ResponseStatusException e) {
			error = true;
		}

		Assertions.assertThat(error).isEqualTo(true);
	}

    //Update instructor




    //GetById
    @Test
    public void testGetInstructorById_Success() {
        Long instructorId = 1L;
        Instructor instructor = new Instructor();
        instructor.setId(instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));

        ResponseInstructorDTO result = instructorService.getById(instructorId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(instructorId);
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



}







