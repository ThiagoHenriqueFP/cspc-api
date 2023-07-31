/*
package uol.compass.cspcapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.classroom.ClassroomRepository;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorRepository;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.instructor.InstructorRepository;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterRepository;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.student.StudentRepository;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private ScrumMasterRepository scrumMasterRepository;
    @Mock
    private CoordinatorRepository coordinatorRepository;


    @InjectMocks
    private ClassroomService classroomService;
    @InjectMocks
    private UserService userService;
    @InjectMocks
    private CoordinatorService coordinatorService;
    @InjectMocks
    private SquadService squadService;
    @InjectMocks
    private StudentService studentService;
    @InjectMocks
    private ScrumMasterService scrumMasterService;
    @InjectMocks
    private InstructorService instructorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Save classroom
    @Test
    public void testSaveClassroom_Failure() {
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO();
        classroomDTO.setTitle("Existing Classroom");

        Classroom existingClassroom = new Classroom("Existing Classroom");
        when(classroomRepository.findByTitle(classroomDTO.getTitle())).thenReturn(Optional.of(existingClassroom));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.saveClassroom(classroomDTO, 1L))
                .withMessageContaining("Title already exists");

        verify(classroomRepository, never()).save(any(Classroom.class));
    }



    //Add students in classroom

    @Test
    public void testAddSquadsToClassroom_Success() {
        // Criar dados simulados para o teste
        Long classroomId = 1L;
        Squad squad1 = new Squad("Squad 1");
        Squad squad2 = new Squad("Squad 2");
        List<Squad> squads = new ArrayList<>(List.of(squad1));
        List<Long> squadIdsToAdd = Collections.singletonList(squad2.getId());

        Classroom classroom = new Classroom("Classroom Test");
        classroom.setId(classroomId);
        classroom.setSquads(squads);
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));

        when(squadService.getAllSquadsById(squadIdsToAdd)).thenReturn(List.of(squad2));

        Classroom result = classroomService.addSquadsToClassroom(classroomId, squadIdsToAdd);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(classroomId);
        assertThat(result.getSquads()).hasSize(2);
        assertThat(result.getSquads().get(1).getId()).isEqualTo(squad2.getId());

        verify(classroomRepository).save(result);
        verify(squadService).attributeSquadsToClassroom(result, List.of(squad2));
    }




    @Test
    public void testAddStudentsToClassroom_Failure() {
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L));

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.addStudentsToClassroom(classroomId, classroomDTO))
                .withMessageContaining("classroom not found");

        verify(classroomRepository, never()).save(any());
    }


    //Add scrumMaster in classroom
    @Test
    public void testAddScrumMastersToClassroom_Failure() {
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L));

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.addScrumMastersToClassroom(classroomId, classroomDTO))
                .withMessageContaining("Classroom not found");

        verify(classroomRepository, never()).save(any());
    }

    //Add Instructor in classroom
    @Test
    public void testAddInstructorsToClassroom_Failure() {
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L));

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.addInstructorsToClassroom(classroomId, classroomDTO))
                .withMessageContaining("Classroom not found");

        verify(classroomRepository, never()).save(any());
    }

    //Add Squad in classroom

    @Test
    public void testAddSquadsToClassroom_Success() {
        Long classroomId = 1L;
        Squad squad1 = new Squad("Squad 1");
        Squad squad2 = new Squad("Squad 2");
        List<Squad> squads = new ArrayList<>(List.of(squad1));
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Collections.singletonList(squad2.getId()));

        Classroom classroom = new Classroom("Classroom Test");
        classroom.setId(classroomId);
        classroom.setSquads(squads);
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));

        when(squadService.getAllSquadsById(classroomDTO.getGeneralUsersIds())).thenReturn(List.of(squad2));

        Classroom result = classroomService.addSquadsToClassroom(classroomId, classroomDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(classroomId);
        assertThat(result.getSquads()).hasSize(2);
        assertThat(result.getSquads().get(1).getId()).isEqualTo(squad2.getId());

        verify(classroomRepository).save(result);
        verify(squadService).attributeSquadsToClassroom(result, List.of(squad2));
    }



    //Update clasroom
    @Test
    public void testUpdateClassroom_Success() {
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setTitle("Updated Classroom");
        classroomDTO.setCoordinatorId(1L);

        Long classroomId = 1L;
        Classroom existingClassroom = new Classroom("Old Classroom");
        existingClassroom.setId(classroomId);
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(existingClassroom));

        ResponseClassroomDTO result = classroomService.updateClassroom(classroomId, classroomDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(classroomId);
        assertThat(result.getTitle()).isEqualTo(classroomDTO.getTitle());
        assertThat(result.getCoordinator()).isEqualTo(classroomDTO.getCoordinatorId());

        verify(classroomRepository).save(existingClassroom);
    }

    @Test
    public void testUpdateClassroom_Failure() {
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setTitle("New Classroom Name");
        classroomDTO.setCoordinatorId(1L);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.updateClassroom(classroomId, classroomDTO))
                .withMessageContaining("Classroom not found");

        verify(classroomRepository, never()).save(any());
    }


    //GetAll
    @Test
    public void testListClassroom_Success() {
        Classroom classroom1 = new Classroom("Test Classroom primary");
        Classroom classroom2 = new Classroom("Test Classroom secondary");
        List<Classroom> classrooms = new ArrayList<>();
        classrooms.add(classroom1);
        classrooms.add(classroom2);

        when(classroomRepository.findAll()).thenReturn(classrooms);

        ResponseEntity<List<Classroom>> response = classroomService.listClassroom();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Classroom> returnedClassrooms = response.getBody();
        assertThat(returnedClassrooms).isNotNull();
        assertThat(returnedClassrooms.size()).isEqualTo(2);

        verify(classroomRepository).findAll();
    }

    @Test
    public void testListClassroom_Failure() {
        when(classroomRepository.findAll()).thenReturn(new ArrayList<>());

        try {
            classroomService.listClassroom();
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(e.getReason()).isEqualTo("No classrooms found");
        }

        verify(classroomRepository).findAll();
    }


    //GetById
    @Test
    public void testGetClassroomById_Success() {
        long classroomId = 1L;

        Classroom classroom = new Classroom("Test Classroom");
        classroom.setId(classroomId);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));

        Classroom result = classroomService.getById(classroomId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(classroomId);
        assertThat(result.getTitle()).isEqualTo("Test Classroom");

        verify(classroomRepository).findById(classroomId);
    }

    @Test
    public void testGetClassroomById_Failure() {
        Long classroomId = 1L;

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.getById(classroomId))
                .withMessageContaining("Classroom not found");

        verify(classroomRepository).findById(classroomId);
    }


    //DeleteById
    @Test
    public void testDeleteClassroom_Success() {
        long classroomId = 1L;
        Classroom classroom = new Classroom("Test Classroom");

        when(classroomRepository.existsById(classroomId)).thenReturn(true);

        ResponseEntity<Long> response = classroomService.deleteClassroom(classroomId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(classroomId);

        verify(classroomRepository).deleteById(classroomId);
    }

    @Test
    public void testDeleteClassroom_Failure() {
        long classroomId = 1L;

        when(classroomRepository.existsById(classroomId)).thenReturn(false);

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.deleteClassroom(classroomId))
                .withMessageContaining("classroom not found");

    }




}
*/