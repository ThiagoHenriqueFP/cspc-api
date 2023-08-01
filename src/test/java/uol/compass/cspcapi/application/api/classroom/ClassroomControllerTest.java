package uol.compass.cspcapi.application.api.classroom;

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
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.ClassroomsConstants.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomControllerTest {

    @Mock
    private ClassroomService classroomService;

    @InjectMocks
    private ClassroomController classroomController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateClassroom_Success() {
        // Mocking the behavior of the ClassroomService.saveClassroom method
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO();
        classroomDTO.setCoordinatorId(1L);

        ResponseClassroomDTO expectedClassroom = new ResponseClassroomDTO(
                1L,
                "Math Class",
                new ResponseCoordinatorDTO(1L, new ResponseUserDTO(1L, "Mary", "Jane", "mary.jane@mail.com")),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(classroomService.saveClassroom(any(CreateClassroomDTO.class), any(Long.class))).thenReturn(expectedClassroom);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity = classroomController.createClassroom(classroomDTO);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedClassroom.getId(), responseEntity.getBody().getId());
        assertEquals(expectedClassroom.getTitle(), responseEntity.getBody().getTitle());
        assertEquals(expectedClassroom.getCoordinator().getId(), responseEntity.getBody().getCoordinator().getId());
    }

    @Test
    public void testCreateClassroom_Error() {
        // Mocking the behavior of the ClassroomService.saveClassroom method to throw an exception
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO();
        classroomDTO.setCoordinatorId(1L);

        when(classroomService.saveClassroom(any(CreateClassroomDTO.class), any(Long.class)))
                .thenThrow(new RuntimeException("Error occurred while saving classroom"));

        // Performing the test and verifying the exception
        assertThrows(RuntimeException.class, () -> classroomController.createClassroom(classroomDTO));
    }

    @Test
    public void testGetClassroomById_Success() {
        // Mocking the behavior of the ClassroomService.getById method for success scenario
        Long classroomId = 1L;
        ResponseClassroomDTO expectedClassroom = new ResponseClassroomDTO(
                1L,
                "Math Class",
                new ResponseCoordinatorDTO(1L, new ResponseUserDTO(1L, "Mary", "Jane", "mary.jane@mail.com")),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(classroomService.getById(anyLong())).thenReturn(expectedClassroom);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity = classroomController.getClassroomById(classroomId);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedClassroom.getId(), responseEntity.getBody().getId());
        assertEquals(expectedClassroom.getTitle(), responseEntity.getBody().getTitle());
        assertEquals(expectedClassroom.getCoordinator().getId(), responseEntity.getBody().getCoordinator().getId());
    }

    @Test
    public void testGetClassroomById_NotFound() throws Exception {
        // Mocking the behavior of the ClassroomService.getById method for scenario where classroom is not found
        Long classroomId = 999L;

        when(classroomService.getById(anyLong())).thenReturn(null);

        // Performing the test and verifying the exception
        try {
            classroomController.getClassroomById(classroomId);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllClassrooms_Success() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for success scenario
        List<ResponseClassroomDTO> expectedClassrooms = new ArrayList<>();
        expectedClassrooms.add(RESPONSE_CLASSROOM);
        expectedClassrooms.add(RESPONSE_CLASSROOM2);

        when(classroomService.getAllClassrooms()).thenReturn(expectedClassrooms);

        // Performing the test
        ResponseEntity<List<ResponseClassroomDTO>> responseEntity = classroomController.getAllClassrooms();

        // Asserting the response status code and the returned list of classrooms
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedClassrooms.size(), responseEntity.getBody().size());
        assertEquals(expectedClassrooms, responseEntity.getBody());
    }

    @Test
    public void testGetAllClassrooms_EmptyList() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for empty list scenario
        List<ResponseClassroomDTO> expectedClassrooms = new ArrayList<>();

        when(classroomService.getAllClassrooms()).thenReturn(expectedClassrooms);

        // Performing the test
        ResponseEntity<List<ResponseClassroomDTO>> responseEntity = classroomController.getAllClassrooms();

        // Asserting the response status code and the returned list of classrooms (should be an empty list)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedClassrooms.size(), responseEntity.getBody().size());
        assertEquals(expectedClassrooms, responseEntity.getBody());
    }

    @Test
    public void testUpdateClassroom_Success() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setTitle("Updated Math Class");
        classroomDTO.setCoordinatorId(3L);

        Classroom updatedClassroom = new Classroom();
        updatedClassroom.setId(classroomId);
        updatedClassroom.setTitle(classroomDTO.getTitle());
        updatedClassroom.setCoordinator(new Coordinator());
        updatedClassroom.getCoordinator().setId(classroomDTO.getCoordinatorId());

        ResponseCoordinatorDTO responseCoordinator = new ResponseCoordinatorDTO(
                3L,
                new ResponseUserDTO(1L, "Teste", "Test", "teste.test@mail.com")
        );
        ResponseClassroomDTO responseClassroom = new ResponseClassroomDTO(
                1L,
                "Updated Math Class",
                responseCoordinator,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        when(classroomService.updateClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroom);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.updateClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedClassroom.getId(), responseEntity.getBody().getId());
        assertEquals(updatedClassroom.getTitle(), responseEntity.getBody().getTitle());
        assertEquals(updatedClassroom.getCoordinator().getId(), responseEntity.getBody().getCoordinator().getId());
    }

    @Test
    public void testUpdateClassroom_NotFound() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setTitle("Updated Math Class");
        classroomDTO.setCoordinatorId(3L);

        when(classroomService.updateClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(null);

        try {
            classroomController.updateClassroom(classroomId, classroomDTO);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for success scenario
        Long classroomId = 1L;

        doNothing().when(classroomService).deleteClassroom(classroomId);

        // Performing the test
        ResponseEntity<Void> responseEntity = classroomController.delete(classroomId);

        // Asserting the response status code (should be HTTP 204 No Content)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for scenario where classroom is not found
        Long classroomId = 999L;

        doThrow(ResponseStatusException.class).when(classroomService).deleteClassroom(classroomId);

        // Performing the test and verifying the exception
        try {
            classroomController.delete(classroomId);
        } catch (ResponseStatusException exception) {
            // Assertion for ResourceNotFoundException
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }

    @Test
    public void testAddStudentsToClassroom_Success() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setStudents(new ArrayList<>());
        responseClassroomDTO.getStudents().addAll(List.of(RESPONSE_STUDENT_1, RESPONSE_STUDENT_2, RESPONSE_STUDENT_3));

        when(classroomService.addStudentsToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.addStudentsToClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());

        List<Long> studentsIdsResult = new ArrayList<>();
        for (ResponseStudentDTO student : responseEntity.getBody().getStudents()) {
            studentsIdsResult.add(student.getId());
        }

        assertEquals(classroomDTO.getGeneralUsersIds(), studentsIdsResult);
    }

    @Test
    public void testAddStudentsToClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        when(classroomService.addStudentsToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.addStudentsToClassroom(classroomId, classroomDTO));
    }

    @Test
    public void testAddScrumMastersToClassroom_Success() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setScrumMasters(new ArrayList<>());
        responseClassroomDTO.getScrumMasters().addAll(List.of(RESPONSE_SCRUMMASTER_1, RESPONSE_SCRUMMASTER_2, RESPONSE_SCRUMMASTER_3));

        when(classroomService.addScrumMastersToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.addScumMastersToClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());

        List<Long> scrumMastersIdsResult = new ArrayList<>();
        for (ResponseScrumMasterDTO scrumMaster : responseEntity.getBody().getScrumMasters()) {
            scrumMastersIdsResult.add(scrumMaster.getId());
        }

        assertEquals(classroomDTO.getGeneralUsersIds(), scrumMastersIdsResult);
    }

    @Test
    public void testAddScrumMastersToClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        when(classroomService.addScrumMastersToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.addScumMastersToClassroom(classroomId, classroomDTO));
    }

    @Test
    public void testAddInstructorsToClassroom_Success() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setInstructors(new ArrayList<>());
        responseClassroomDTO.getInstructors().addAll(List.of(RESPONSE_INSTRUCTOR_1, RESPONSE_INSTRUCTOR_2, RESPONSE_INSTRUCTOR_3));

        when(classroomService.addInstructorsToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.addInstructorsToClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());

        List<Long> instructorsIdsResult = new ArrayList<>();
        for (ResponseInstructorDTO instructor : responseEntity.getBody().getInstructors()) {
            instructorsIdsResult.add(instructor.getId());
        }

        assertEquals(classroomDTO.getGeneralUsersIds(), instructorsIdsResult);
    }

    @Test
    public void testAddInstructorsToClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        when(classroomService.addInstructorsToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.addInstructorsToClassroom(classroomId, classroomDTO));
    }

    @Test
    public void testAddSquadsToClassroom_Success() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setSquads(new ArrayList<>());
        responseClassroomDTO.getSquads().addAll(List.of(RESPONSE_SQUAD_1, RESPONSE_SQUAD_2, RESPONSE_SQUAD_3));

        when(classroomService.addSquadsToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.addSquadsToClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());

        List<Long> squadsIdsResult = new ArrayList<>();
        for (ResponseSquadDTO squad : responseEntity.getBody().getSquads()) {
            squadsIdsResult.add(squad.getId());
        }

        assertEquals(classroomDTO.getGeneralUsersIds(), squadsIdsResult);
    }

    @Test
    public void testAddSquadsToClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.addStudentsToClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(1L, 2L, 3L));

        when(classroomService.addSquadsToClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.addSquadsToClassroom(classroomId, classroomDTO));
    }

    @Test
    public void testRemoveStudentsFromClassroom_Success() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setStudents(Collections.emptyList()); // Classroom with no students after removal

        when(classroomService.removeStudentsFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.removeStudentsFromClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());
        assertEquals(Collections.emptyList(), responseEntity.getBody().getStudents());
    }

    @Test
    public void testRemoveStudentsFromClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        when(classroomService.removeStudentsFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.removeStudentsFromClassroom(classroomId, classroomDTO));
    }

    @Test
    public void testRemoveScrumMastersFromClassroom_Success() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setScrumMasters(Collections.emptyList());

        when(classroomService.removeScrumMastersFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.removeScrumMastersFromClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());
        assertEquals(Collections.emptyList(), responseEntity.getBody().getScrumMasters());
    }

    @Test
    public void testRemoveScrumMastersFromClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        when(classroomService.removeScrumMastersFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.removeScrumMastersFromClassroom(classroomId, classroomDTO));
    }

    @Test
    public void testRemoveInstructorsFromClassroom_Success() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setInstructors(Collections.emptyList());

        when(classroomService.removeInstructorsFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.removeInstructorsFromClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());
        assertEquals(Collections.emptyList(), responseEntity.getBody().getInstructors());
    }

    @Test
    public void testRemoveInstructorsFromClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        when(classroomService.removeInstructorsFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.removeInstructorsFromClassroom(classroomId, classroomDTO));
    }

    @Test
    public void testRemoveSquadsFromClassroom_Success() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for success scenario
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        ResponseClassroomDTO responseClassroomDTO = new ResponseClassroomDTO();
        responseClassroomDTO.setId(classroomId);
        responseClassroomDTO.setTitle("Math Class");
        responseClassroomDTO.setCoordinator(RESPONSE_COORDINATOR_3);
        responseClassroomDTO.setSquads(Collections.emptyList());

        when(classroomService.removeSquadsFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenReturn(responseClassroomDTO);

        // Performing the test
        ResponseEntity<ResponseClassroomDTO> responseEntity =
                classroomController.removeSquadsFromClassroom(classroomId, classroomDTO);

        // Asserting the response status code and the returned ResponseClassroomDTO object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(classroomId, responseEntity.getBody().getId());
        assertEquals("Math Class", responseEntity.getBody().getTitle());
        assertEquals(3L, responseEntity.getBody().getCoordinator().getId());
        assertEquals(Collections.emptyList(), responseEntity.getBody().getSquads());
    }

    @Test
    public void testRemoveSquadsFromClassroom_ClassroomNotFound() {
        // Mocking the behavior of the ClassroomService.removeStudentsFromClassroom method for scenario where classroom is not found
        Long classroomId = 999L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        when(classroomService.removeSquadsFromClassroom(anyLong(), any(UpdateClassroomDTO.class)))
                .thenThrow(ResponseStatusException.class);

        // Performing the test and verifying the exception
        assertThrows(ResponseStatusException.class,
                () -> classroomController.removeSquadsFromClassroom(classroomId, classroomDTO));
    }
}
