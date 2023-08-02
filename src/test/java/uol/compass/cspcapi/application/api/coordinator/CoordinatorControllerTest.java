package uol.compass.cspcapi.application.api.coordinator;

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
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.UpdateCoordinatorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.instructor.Instructor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.CoordinatorsConstants.*;

@ExtendWith(MockitoExtension.class)
public class CoordinatorControllerTest {
    @Mock
    private CoordinatorService coordinatorService;

    @InjectMocks
    private CoordinatorController coordinatorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCoordinator_Success() {
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second");
        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO();
        coordinatorDTO.setUser(userDTO);

        ResponseUserDTO expectedUserDTO = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseCoordinatorDTO expectedCoordinator = new ResponseCoordinatorDTO(1L, expectedUserDTO);

        when(coordinatorService.save(any(CreateCoordinatorDTO.class))).thenReturn(expectedCoordinator);

        ResponseEntity<ResponseCoordinatorDTO> responseEntity = coordinatorController.createCoordinator(coordinatorDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedCoordinator.getId(), responseEntity.getBody().getId());
        assertEquals(expectedCoordinator.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testCreateCoordinator_Error() {
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second");
        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO();
        coordinatorDTO.setUser(userDTO);

        when(coordinatorService.save(any(CreateCoordinatorDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving coordinator"));

        assertThrows(RuntimeException.class, () -> coordinatorController.createCoordinator(coordinatorDTO));
    }

    @Test
    public void testGetCoordinatorById_Success() {
        Long coordinatorId = 1L;
        ResponseUserDTO expectedCreateUserDTO = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseCoordinatorDTO expectedCoordinator = new ResponseCoordinatorDTO(coordinatorId, expectedCreateUserDTO);

        when(coordinatorService.getById(anyLong())).thenReturn(expectedCoordinator);

        ResponseEntity<ResponseCoordinatorDTO> responseEntity = coordinatorController.getCoordinatorById(coordinatorId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCoordinator.getId(), responseEntity.getBody().getId());
        assertEquals(expectedCoordinator.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testGetCoordinatorById_NotFound() {
        Long coordinatorId = 999L;

        when(coordinatorService.getById(anyLong())).thenReturn(null);

        // Performing the test and verifying the exception
        try {
            coordinatorController.getCoordinatorById(coordinatorId);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllCoordinators_Success() {
        List<ResponseCoordinatorDTO> expectedCoordinators = new ArrayList<>();
        expectedCoordinators.addAll(List.of(RESPONSE_COORDINATOR_1, RESPONSE_COORDINATOR_2, RESPONSE_COORDINATOR_3));

        when(coordinatorService.getAll()).thenReturn(expectedCoordinators);

        ResponseEntity<List<ResponseCoordinatorDTO>> responseEntity = coordinatorController.getAllCoordinators();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCoordinators.size(), responseEntity.getBody().size());
        assertEquals(expectedCoordinators, responseEntity.getBody());
    }

    @Test
    public void testGetAllCoordinators_EmptyList() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for empty list scenario
        List<ResponseCoordinatorDTO> expectedCoordinators = new ArrayList<>();

        when(coordinatorService.getAll()).thenReturn(expectedCoordinators);

        // Performing the test
        ResponseEntity<List<ResponseCoordinatorDTO>> responseEntity = coordinatorController.getAllCoordinators();

        // Asserting the response status code and the returned list of classrooms (should be an empty list)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCoordinators.size(), responseEntity.getBody().size());
        assertEquals(expectedCoordinators, responseEntity.getBody());
    }

    @Test
    public void testUpdateCoordinator_Success() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for success scenario
        Long coordinatorId = 1L;
        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.getFirstName(),
                RESPONSE_USER_1.getLastName(),
                RESPONSE_USER_1.getEmail(),
                "password"
        );
        UpdateCoordinatorDTO coordinatorDTO = new UpdateCoordinatorDTO();
        coordinatorDTO.setUser(userDTO);

        Coordinator updatedCoordinator = new Coordinator();
        updatedCoordinator.setId(coordinatorId);
        updatedCoordinator.setUser(USER_1);

        when(coordinatorService.update(anyLong(), any(UpdateCoordinatorDTO.class))).thenReturn(RESPONSE_COORDINATOR_1);

        // Performing the test
        ResponseEntity<ResponseCoordinatorDTO> responseEntity = coordinatorController.updateCoordinator(coordinatorId, coordinatorDTO);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCoordinator.getId(), responseEntity.getBody().getId());
        assertEquals(updatedCoordinator.getUser().getFirstName(), responseEntity.getBody().getUser().getFirstName());
        assertEquals(updatedCoordinator.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
        assertEquals(updatedCoordinator.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
    }

    @Test
    public void testUpdateCoordinator_NotFound() {
        Long coordinatorId = 999L;

        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.getFirstName(),
                RESPONSE_USER_1.getLastName(),
                RESPONSE_USER_1.getEmail(),
                "password"
        );
        UpdateCoordinatorDTO coordinatorDTO = new UpdateCoordinatorDTO();
        coordinatorDTO.setUser(userDTO);

        when(coordinatorService.update(anyLong(), any(UpdateCoordinatorDTO.class))).thenReturn(null);

        try {
            coordinatorController.updateCoordinator(coordinatorId, coordinatorDTO);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for success scenario
        Long coordinatorId = 1L;

        doNothing().when(coordinatorService).deleteById(coordinatorId);

        // Performing the test
        ResponseEntity<Void> responseEntity = coordinatorController.deleteCoordinatorById(coordinatorId);

        // Asserting the response status code (should be HTTP 204 No Content)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for scenario where classroom is not found
        Long coordinatorId = 999L;

        doThrow(ResponseStatusException.class).when(coordinatorService).deleteById(coordinatorId);

        // Performing the test and verifying the exception
        try {
            coordinatorController.deleteCoordinatorById(coordinatorId);
        } catch (ResponseStatusException exception) {
            // Assertion for ResourceNotFoundException
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}
