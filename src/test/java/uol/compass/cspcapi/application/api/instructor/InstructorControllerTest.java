package uol.compass.cspcapi.application.api.instructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.InstructorsConstants.*;

@ExtendWith(MockitoExtension.class)
public class InstructorControllerTest {
    @Mock
    private InstructorService instructorService;

    @InjectMocks
    private InstructorController instructorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInstructor_Success() {
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();
        instructorDTO.setUser(user);

        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseInstructorDTO expectedInstructor = new ResponseInstructorDTO(1L, expectedUser);

        when(instructorService.save(any(CreateInstructorDTO.class))).thenReturn(expectedInstructor);

        ResponseEntity<ResponseInstructorDTO> responseEntity = instructorController.createInstructor(instructorDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedInstructor.getId(), responseEntity.getBody().getId());
        assertEquals(expectedInstructor.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testCreateInstructor_Error() {
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        CreateInstructorDTO instructorDTO = new CreateInstructorDTO();
        instructorDTO.setUser(user);

        when(instructorService.save(any(CreateInstructorDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving instructor"));

        assertThrows(RuntimeException.class, () -> instructorController.createInstructor(instructorDTO));
    }

    @Test
    public void testGetInstructorById_Success() {
        Long instructorId = 1L;
        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseInstructorDTO expectedInstructor = new ResponseInstructorDTO(instructorId, expectedUser);

        when(instructorService.getById(anyLong())).thenReturn(expectedInstructor);

        ResponseEntity<ResponseInstructorDTO> responseEntity = instructorController.getInstructorById(instructorId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedInstructor.getId(), responseEntity.getBody().getId());
        assertEquals(expectedInstructor.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testGetInstructorById_NotFound() {
        Long instructorId = 999L;

        when(instructorService.getById(anyLong())).thenReturn(null);

        // Performing the test and verifying the exception
        try {
            instructorController.getInstructorById(instructorId);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllInstructors_Success() {
        List<ResponseInstructorDTO> expectedInstructors = new ArrayList<>();
        expectedInstructors.addAll(List.of(RESPONSE_INSTRUCTOR_1, RESPONSE_INSTRUCTOR_2, RESPONSE_INSTRUCTOR_3));

        when(instructorService.getAll()).thenReturn(expectedInstructors);

        ResponseEntity<List<ResponseInstructorDTO>> responseEntity = instructorController.getAllInstructors();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedInstructors.size(), responseEntity.getBody().size());
        assertEquals(expectedInstructors, responseEntity.getBody());
    }

    @Test
    public void testGetAllInstructors_EmptyList() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for empty list scenario
        List<ResponseInstructorDTO> expectedInstructors = new ArrayList<>();

        when(instructorService.getAll()).thenReturn(expectedInstructors);

        // Performing the test
        ResponseEntity<List<ResponseInstructorDTO>> responseEntity = instructorController.getAllInstructors();

        // Asserting the response status code and the returned list of classrooms (should be an empty list)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedInstructors.size(), responseEntity.getBody().size());
        assertEquals(expectedInstructors, responseEntity.getBody());
    }

    @Test
    public void testUpdateInstructor_Success() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for success scenario
        Long instructorId = 1L;
        User user = new User(
                RESPONSE_USER_1.getFirstName(),
                RESPONSE_USER_1.getLastName(),
                RESPONSE_USER_1.getEmail(),
                "password"
        );
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO();
        instructorDTO.setUser(user);

        Instructor updatedInstructor = new Instructor();
        updatedInstructor.setId(instructorId);
        updatedInstructor.setUser(instructorDTO.getUser());

        when(instructorService.update(anyLong(), any(UpdateInstructorDTO.class))).thenReturn(RESPONSE_INSTRUCTOR_1);

        // Performing the test
        ResponseEntity<ResponseInstructorDTO> responseEntity = instructorController.updateInstructor(instructorId, instructorDTO);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedInstructor.getId(), responseEntity.getBody().getId());
        assertEquals(updatedInstructor.getUser().getFirstName(), responseEntity.getBody().getUser().getFirstName());
        assertEquals(updatedInstructor.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
        assertEquals(updatedInstructor.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
    }

    @Test
    public void testUpdateInstructor_NotFound() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for scenario where classroom is not found
        Long instructorId = 999L;
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO();
        instructorDTO.setUser(user);

        when(instructorService.update(anyLong(), any(UpdateInstructorDTO.class))).thenReturn(null);

        try {
            instructorController.updateInstructor(instructorId, instructorDTO);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for success scenario
        Long instructorId = 1L;

        doNothing().when(instructorService).deleteById(instructorId);

        // Performing the test
        ResponseEntity<Void> responseEntity = instructorController.deleteInstructorById(instructorId);

        // Asserting the response status code (should be HTTP 204 No Content)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for scenario where classroom is not found
        Long instructorId = 999L;

        doThrow(ResponseStatusException.class).when(instructorService).deleteById(instructorId);

        // Performing the test and verifying the exception
        try {
            instructorController.deleteInstructorById(instructorId);
        } catch (ResponseStatusException exception) {
            // Assertion for ResourceNotFoundException
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}
