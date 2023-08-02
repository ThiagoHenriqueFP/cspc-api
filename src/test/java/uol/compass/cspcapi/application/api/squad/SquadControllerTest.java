package uol.compass.cspcapi.application.api.squad;

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
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.SquadsConstants.*;

@ExtendWith(MockitoExtension.class)
public class SquadControllerTest {

    @Mock
    private SquadService squadService;

    @InjectMocks
    private SquadController squadController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSquad_Success() {
        CreateSquadDTO squadDTO = new CreateSquadDTO();
        squadDTO.setName("Modern Bugs");

        ResponseSquadDTO expectedSquad = new ResponseSquadDTO(1L, "Modern Bugs");

        when(squadService.save(any(CreateSquadDTO.class))).thenReturn(expectedSquad);

        ResponseEntity<ResponseSquadDTO> responseEntity = squadController.createSquad(squadDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedSquad.getId(), responseEntity.getBody().getId());
        assertEquals(expectedSquad.getName(), responseEntity.getBody().getName());
    }

    @Test
    public void testCreateSquad_Error() {
        CreateSquadDTO squadDTO = new CreateSquadDTO();
        squadDTO.setName("Springforce");

        when(squadService.save(any(CreateSquadDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving squad"));

        assertThrows(RuntimeException.class, () -> squadController.createSquad(squadDTO));
    }

    @Test
    public void testGetSquadById_Success() {
        Long squadId = 1L;
        ResponseSquadDTO expectedSquad = new ResponseSquadDTO(1L, "Modern Bugs");

        when(squadService.getById(anyLong())).thenReturn(expectedSquad);

        ResponseEntity<ResponseSquadDTO> responseEntity = squadController.getSquadById(squadId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSquad.getId(), responseEntity.getBody().getId());
        assertEquals(expectedSquad.getName(), responseEntity.getBody().getName());
    }

    @Test
    public void testGetSquadById_NotFound() {
        Long squadId = 999L;

        when(squadService.getById(anyLong())).thenReturn(null);

        // Performing the test and verifying the exception
        try {
            squadController.getSquadById(squadId);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllSquads_Success() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for success scenario
        List<ResponseSquadDTO> expectedSquads = new ArrayList<>();
        expectedSquads.addAll(List.of(RESPONSE_SQUAD_1, RESPONSE_SQUAD_2, RESPONSE_SQUAD_3));

        when(squadService.getAll()).thenReturn(expectedSquads);

        // Performing the test
        ResponseEntity<List<ResponseSquadDTO>> responseEntity = squadController.getAllSquads();

        // Asserting the response status code and the returned list of classrooms
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSquads.size(), responseEntity.getBody().size());
        assertEquals(expectedSquads, responseEntity.getBody());
    }

    @Test
    public void testGetAllSquads_EmptyList() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for empty list scenario
        List<ResponseSquadDTO> expectedSquads = new ArrayList<>();

        when(squadService.getAll()).thenReturn(expectedSquads);

        // Performing the test
        ResponseEntity<List<ResponseSquadDTO>> responseEntity = squadController.getAllSquads();

        // Asserting the response status code and the returned list of classrooms (should be an empty list)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSquads.size(), responseEntity.getBody().size());
        assertEquals(expectedSquads, responseEntity.getBody());
    }

    @Test
    public void testUpdateSquad_Success() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for success scenario
        Long squadId = 1L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO();
        squadDTO.setName("Modern Bugs");

        Squad updatedSquad = new Squad();
        updatedSquad.setId(squadId);
        updatedSquad.setName(squadDTO.getName());

        ResponseSquadDTO responseSquad = new ResponseSquadDTO(
                1L,
                updatedSquad.getName()
        );

        when(squadService.updateSquad(anyLong(), any(UpdateSquadDTO.class)))
                .thenReturn(responseSquad);

        // Performing the test
        ResponseEntity<ResponseSquadDTO> responseEntity =
                squadController.updateSquad(squadId, squadDTO);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedSquad.getId(), responseEntity.getBody().getId());
        assertEquals(updatedSquad.getName(), responseEntity.getBody().getName());
    }

    @Test
    public void testUpdateSquad_NotFound() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for scenario where classroom is not found
        Long squadId = 999L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO();
        squadDTO.setName("Modern Bugs");

        when(squadService.updateSquad(anyLong(), any(UpdateSquadDTO.class))).thenReturn(null);

        try {
            squadController.updateSquad(squadId, squadDTO);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for success scenario
        Long squadId = 1L;

        doNothing().when(squadService).delete(squadId);

        // Performing the test
        ResponseEntity<Void> responseEntity = squadController.deleteSquadById(squadId);

        // Asserting the response status code (should be HTTP 204 No Content)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for scenario where classroom is not found
        Long squadId = 999L;

        doThrow(ResponseStatusException.class).when(squadService).delete(squadId);

        // Performing the test and verifying the exception
        try {
            squadController.deleteSquadById(squadId);
        } catch (ResponseStatusException exception) {
            // Assertion for ResourceNotFoundException
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }

    @Test
    public void testAddStudentsToSquad_Success() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for success scenario
        Long squadId = 1L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO();
        squadDTO.setStudentsIds(List.of(1L, 2L, 3L));

        ResponseSquadDTO responseSquadDTO = new ResponseSquadDTO();
        responseSquadDTO.setId(squadId);
        responseSquadDTO.setName("Math Squad");
        responseSquadDTO.setStudents(new ArrayList<>());
        responseSquadDTO.getStudents().addAll(List.of(RESPONSE_STUDENT_1, RESPONSE_STUDENT_2, RESPONSE_STUDENT_3));

        when(squadService.addStudentsToSquad(anyLong(), any(UpdateSquadDTO.class))).thenReturn(responseSquadDTO);

        // Performing the test
        ResponseEntity<ResponseSquadDTO> responseEntity =
                squadController.addStudentsToSquad(squadId, squadDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(squadId, responseEntity.getBody().getId());
        assertEquals("Math Squad", responseEntity.getBody().getName());

        List<Long> studentsIdsResult = new ArrayList<>();
        for (ResponseStudentDTO student : responseEntity.getBody().getStudents()) {
            studentsIdsResult.add(student.getId());
        }

        assertEquals(squadDTO.getStudentsIds(), studentsIdsResult);
    }

    @Test
    public void testAddStudentsToClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for scenario where classroom is not found
        Long squadId = 999L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO();
        squadDTO.setStudentsIds(List.of(1L, 2L, 3L));

        when(squadService.addStudentsToSquad(anyLong(), any(UpdateSquadDTO.class))).thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> squadController.addStudentsToSquad(squadId, squadDTO));
    }

    @Test
    public void testRemoveStudentsFromClassroom_Success() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for success scenario
        Long squadId = 1L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO();
        squadDTO.setStudentsIds(List.of(101L, 102L, 103L));

        ResponseSquadDTO responseSquadDTO = new ResponseSquadDTO();
        responseSquadDTO.setId(squadId);
        responseSquadDTO.setName("Math Class");
        responseSquadDTO.setStudents(Collections.emptyList()); // Classroom with no students after removal

        when(squadService.removeStudentsFromSquad(anyLong(), any(UpdateSquadDTO.class))).thenReturn(responseSquadDTO);

        // Performing the test
        ResponseEntity<ResponseSquadDTO> responseEntity =
                squadController.removeStudentsFromSquad(squadId, squadDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(squadId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getName());
        assertEquals(Collections.emptyList(), responseEntity.getBody().getStudents());
    }

    @Test
    public void testRemoveStudentsFromClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for scenario where classroom is not found
        Long squadId = 999L;
        UpdateSquadDTO squadDTO = new UpdateSquadDTO();
        squadDTO.setStudentsIds(List.of(101L, 102L, 103L));

        when(squadService.removeStudentsFromSquad(anyLong(), any(UpdateSquadDTO.class))).thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> squadController.removeStudentsFromSquad(squadId, squadDTO));
    }
}
