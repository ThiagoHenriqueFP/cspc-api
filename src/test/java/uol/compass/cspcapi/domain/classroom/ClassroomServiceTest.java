package uol.compass.cspcapi.domain.classroom;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.classroom.ClassroomRepository;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.grade.Grade;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.User;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.ClassroomsConstants.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {
    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private CoordinatorService coordinatorService;

    @Mock
    private StudentService studentService;
    @Mock
    private InstructorService instructorService;
    @Mock
    private ScrumMasterService scrumMasterService;
    @Mock
    private SquadService squadService;


    @InjectMocks
    private ClassroomService classroomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveClassroom() {
        // Dados de teste
        Long coordinatorId = 1L;
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO("Classroom 101", coordinatorId);

        // Mock do repositório
        when(classroomRepository.findByTitle(classroomDTO.getTitle())).thenReturn(Optional.empty());
        when(classroomRepository.save(any(Classroom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock do serviço de coordenador
        Coordinator coordinator = VALID_COORDINATOR;
        when(coordinatorService.getByIdOriginal(coordinatorId)).thenReturn(coordinator);

        // Executando o método
        ResponseClassroomDTO result = classroomService.saveClassroom(classroomDTO, coordinatorId);

        // Verificação
        verify(classroomRepository).findByTitle(classroomDTO.getTitle());
        verify(classroomRepository).save(any(Classroom.class));
        verify(coordinatorService).getByIdOriginal(coordinatorId);

        // Verifica se o resultado não é nulo
        assertNotNull(result);

        // Verifica se o título da sala de aula no resultado é o mesmo que o fornecido na entrada
        assertEquals(classroomDTO.getTitle(), result.getTitle());
    }

    @Test
    void testSaveClassroomWithTitleAlreadyExists() {
        // Dados de teste
        Long coordinatorId = 1L;
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO("Classroom 101", coordinatorId);

        // Mock do repositório para retornar uma sala de aula existente
        when(classroomRepository.findByTitle(classroomDTO.getTitle())).thenReturn(Optional.of(new Classroom()));

        // Verificação de exceção quando o título já existe
        assertThrows(ResponseStatusException.class, () -> classroomService.saveClassroom(classroomDTO, coordinatorId));

        // Verificações de chamada de método
        verify(classroomRepository).findByTitle(classroomDTO.getTitle());
        verify(classroomRepository, never()).save(any(Classroom.class));
        verify(coordinatorService, never()).getByIdOriginal(coordinatorId);
    }

    @Test
    void testGetById_ExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;
        Classroom existingClassroom = new Classroom("Classroom 101", new Coordinator());
        existingClassroom.setId(classroomId);

        // Mock do repositório
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(existingClassroom));

        // Executando o método
        ResponseClassroomDTO result = classroomService.getById(classroomId);

        // Verificação
        verify(classroomRepository).findById(classroomId);

        // Verifica se o resultado não é nulo
        assertNotNull(result);

        // Verifica se o ID da sala de aula no resultado é o mesmo que o fornecido na entrada
        assertEquals(classroomId, result.getId());
    }

    @Test
    void testGetById_NonExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;

        // Mock do repositório para retornar Optional vazio
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> classroomService.getById(classroomId));

        // Verificação de chamada de método
        verify(classroomRepository).findById(classroomId);
    }

    @Test
    void testGetAllClassrooms() {
        // Dados de teste
        Classroom classroom1 = new Classroom("Classroom 101", new Coordinator());
        classroom1.setId(1L);
        Classroom classroom2 = new Classroom("Classroom 102", new Coordinator());
        classroom2.setId(2L);

        // Mock do repositório para retornar a lista de salas de aula
        when(classroomRepository.findAll()).thenReturn(Arrays.asList(classroom1, classroom2));

        // Executando o método
        List<ResponseClassroomDTO> result = classroomService.getAllClassrooms();

        // Verificação
        verify(classroomRepository).findAll();

        // Verifica se o resultado não é nulo
        assertNotNull(result);

        // Verifica se a quantidade de salas de aula retornadas está correta
        assertEquals(2, result.size());

        // Verifica se os IDs das salas de aula no resultado são os mesmos que os fornecidos nas entradas
        assertEquals(classroom1.getId(), result.get(0).getId());
        assertEquals(classroom2.getId(), result.get(1).getId());
    }

    @Test
    public void testUpdateClassroom_Success() {
        // Mocking input data
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setTitle("New Title");
        classroomDTO.setCoordinatorId(10L);

        // Mocking the coordinator object
        Coordinator coordinator = new Coordinator(new User("John", "Doe", "john.doe@mail.com", "john.doe"));
        coordinator.setId(10L);

        // Mocking the classroom object
        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setTitle("Old Title");
        //classroom.setCoordinator(coordinator);

        // Mocking repository behavior
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(coordinatorService.getByIdOriginal(classroomDTO.getCoordinatorId())).thenReturn(coordinator);
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // Mocking the mapping of students to response DTOs
        ResponseCoordinatorDTO responseCoordinator = new ResponseCoordinatorDTO(10L, new ResponseUserDTO(10L, "John", "Doe", "john.doe@mail.com"));
        when(coordinatorService.mapToResponseCoordinator(any(Coordinator.class))).thenReturn(responseCoordinator);

        // Call the method under test
        ResponseClassroomDTO response = classroomService.updateClassroom(classroomId, classroomDTO);

        // Assertions
        assertEquals(classroomDTO.getTitle(), response.getTitle());
        assertEquals(classroomDTO.getCoordinatorId(), response.getCoordinator().getId());
    }

    @Test
    void testUpdateClassroom_NonExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO("Updated Classroom", 2L);

        // Mock do repositório para retornar Optional vazio
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> classroomService.updateClassroom(classroomId, classroomDTO));

        // Verificação de chamada de método
        verify(classroomRepository).findById(classroomId);
        verify(coordinatorService, never()).getByIdOriginal(classroomDTO.getCoordinatorId());
        verify(classroomRepository, never()).save(any(Classroom.class));
    }

    @Test
    void testDeleteClassroom_ExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;

        Classroom existingClassroom = new Classroom("Classroom 101", new Coordinator());
        existingClassroom.setId(classroomId);

        List<Student> students = new ArrayList<>();
        students.add(new Student(new User("John", "Doe", "john.doe@mail.com", "john.doe")));
        students.add(new Student(new User("Jane", "Loyd", "jane.loyd@mail.com", "jane.loyd")));

        List<ScrumMaster> scrumMasters = new ArrayList<>();
        scrumMasters.add(new ScrumMaster(new User("Victor", "Silva", "victor.silva@mail.com", "victor.silva")));
        scrumMasters.add(new ScrumMaster(new User("Gilberto", "Maderos", "gilberto.maderos", "gilberto.maderos")));

        List<Squad> squads = new ArrayList<>();
        squads.add(new Squad("Squad 1"));
        squads.add(new Squad("Squad 2"));

        existingClassroom.setStudents(students);
        existingClassroom.setScrumMasters(scrumMasters);
        existingClassroom.setSquads(squads);

        // Mock do repositório para retornar a sala de aula existente
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.of(existingClassroom));

        // Executando o método
        classroomService.deleteClassroom(classroomId);

        // Verificação
        verify(classroomRepository).findById(classroomId);
        verify(studentService).attributeStudentsToClassroom(null, students);
        verify(scrumMasterService).attributeScrumMastersToClassroom(null, scrumMasters);
        verify(squadService).attributeSquadsToClassroom(null, squads);
        verify(classroomRepository).save(existingClassroom);
        verify(classroomRepository).deleteById(classroomId);
    }

    @Test
    void testDeleteClassroom_NonExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;

        // Mock do repositório para retornar Optional vazio
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> classroomService.deleteClassroom(classroomId));

        // Verificação de chamada de método
        verify(classroomRepository).findById(classroomId);
        verify(studentService, never()).attributeStudentsToClassroom(any(), any());
        verify(scrumMasterService, never()).attributeScrumMastersToClassroom(any(), any());
        verify(squadService, never()).attributeSquadsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any(Classroom.class));
        verify(classroomRepository, never()).deleteById(anyLong());
    }
    @Test
    void testAddStudentsToClassroom_ExistingClassroom() {
        // Mock data
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setStudents(new ArrayList<>());

        List<Student> newStudents = List.of(
                new Student(101L, "John"),
                new Student(102L, "Jane"),
                new Student(103L, "Smith")
        );

        // Mocking repository
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // Mocking studentService
        when(studentService.getAllStudentsById(classroomDTO.getGeneralUsersIds())).thenReturn(newStudents);

        // Mocking the mapping of students to response DTOs
        List<ResponseStudentDTO> responseStudents = List.of(
                new ResponseStudentDTO(101L, new ResponseUserDTO(), new Grade(), null, null),
                new ResponseStudentDTO(102L, new ResponseUserDTO(), new Grade(), null, null),
                new ResponseStudentDTO(103L, new ResponseUserDTO(), new Grade(), null, null)
        );
        when(studentService.mapToResponseStudents(newStudents)).thenReturn(responseStudents);

        // Call the method to be tested
        ResponseClassroomDTO response = classroomService.addStudentsToClassroom(classroomId, classroomDTO);

        // Verify the interactions
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(studentService, times(1)).getAllStudentsById(classroomDTO.getGeneralUsersIds());
        verify(studentService, times(1)).attributeStudentsToClassroom(classroom, newStudents);
        verify(studentService, times(1)).mapToResponseStudents(newStudents);

        // Assertions
        assertNotNull(response);
        assertEquals(3, response.getStudents().size());
    }

    @Test
    void testAddStudentsToClassroom_NonExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        // Mock do repositório para retornar Optional vazio
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> classroomService.addStudentsToClassroom(classroomId, classroomDTO));

        // Verificação de chamada de método
        verify(classroomRepository).findById(classroomId);
        verify(studentService, never()).getAllStudentsById(any());
        verify(studentService, never()).attributeStudentsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    void testAddScrumMastersToClassroom_ExistingClassroom() {
        // Mock data
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setScrumMasters(new ArrayList<>());

        List<ScrumMaster> newScrumMasters = List.of(
                new ScrumMaster(101L, new User("John", "Doe", "john.doe@mail.com", "john.doe")),
                new ScrumMaster(102L, new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new ScrumMaster(103L, new User("Harry", "Potter", "harry.potter@mail.com", "harry.potter"))
        );
        Classroom newClassroom = new Classroom();
        newClassroom.setScrumMasters(newScrumMasters);

        // Mocking repository
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // Mocking studentService
        when(scrumMasterService.getAllScrumMastersById(classroomDTO.getGeneralUsersIds())).thenReturn(newScrumMasters);

        // Mocking the mapping of students to response DTOs
        List<ResponseScrumMasterDTO> responseScrumMasters = List.of(
                new ResponseScrumMasterDTO(101L, new ResponseUserDTO(), null),
                new ResponseScrumMasterDTO(102L, new ResponseUserDTO(), null),
                new ResponseScrumMasterDTO(103L, new ResponseUserDTO(), null)
        );
        when(scrumMasterService.mapToResponseScrumMasters(newScrumMasters)).thenReturn(responseScrumMasters);

        // Call the method to be tested
        ResponseClassroomDTO response = classroomService.addScrumMastersToClassroom(classroomId, classroomDTO);

        // Verify the interactions
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(scrumMasterService, times(1)).getAllScrumMastersById(classroomDTO.getGeneralUsersIds());
        verify(scrumMasterService, times(1)).attributeScrumMastersToClassroom(classroom, newScrumMasters);
        verify(scrumMasterService, times(1)).mapToResponseScrumMasters(newScrumMasters);

        // Assertions
        assertNotNull(response);
        assertEquals(3, response.getScrumMasters().size());
    }

    @Test
    void testAddScrumMastersToClassroom_NonExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        // Mock do repositório para retornar Optional vazio
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> classroomService.addScrumMastersToClassroom(classroomId, classroomDTO));

        // Verificação de chamada de método
        verify(classroomRepository).findById(classroomId);
        verify(scrumMasterService, never()).getAllScrumMastersById(any());
        verify(scrumMasterService, never()).attributeScrumMastersToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    void testAddInstructorsToClassroom_ExistingClassroom() {
        // Mock data
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setInstructors(new ArrayList<>());

        List<Instructor> newInstructors = List.of(
                new Instructor(101L, new User("John", "Doe", "john.doe@mail.com", "john.doe")),
                new Instructor(102L, new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new Instructor(103L, new User("Harry", "Potter", "harry.potter@mail.com", "harry.potter"))
        );
        Classroom newClassroom = new Classroom();
        newClassroom.setInstructors(newInstructors);

        // Mocking repository
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // Mocking studentService
        when(instructorService.getAllInstructorsById(classroomDTO.getGeneralUsersIds())).thenReturn(newInstructors);

        // Mocking the mapping of students to response DTOs
        List<ResponseInstructorDTO> responseInstructors = List.of(
                new ResponseInstructorDTO(101L, new ResponseUserDTO(), null),
                new ResponseInstructorDTO(102L, new ResponseUserDTO(), null),
                new ResponseInstructorDTO(103L, new ResponseUserDTO(), null)
        );
        when(instructorService.mapToResponseInstructors(newInstructors)).thenReturn(responseInstructors);

        // Call the method to be tested
        ResponseClassroomDTO response = classroomService.addInstructorsToClassroom(classroomId, classroomDTO);

        // Verify the interactions
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(instructorService, times(1)).getAllInstructorsById(classroomDTO.getGeneralUsersIds());
        verify(instructorService, times(1)).attributeInstructorsToClassroom(classroom, newInstructors);
        verify(instructorService, times(1)).mapToResponseInstructors(newInstructors);

        // Assertions
        assertNotNull(response);
        assertEquals(3, response.getInstructors().size());
    }

    @Test
    void testAddInstructorsToClassroom_NonExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        // Mock do repositório para retornar Optional vazio
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> classroomService.addInstructorsToClassroom(classroomId, classroomDTO));

        // Verificação de chamada de método
        verify(classroomRepository).findById(classroomId);
        verify(instructorService, never()).getAllInstructorsById(any());
        verify(instructorService, never()).attributeInstructorsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    void testAddSquadsToClassroom_ExistingClassroom() {
        // Mock data
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(List.of(101L, 102L, 103L));

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setSquads(new ArrayList<>());

        List<Squad> newSquads = List.of(
                new Squad(101L, "SpringForce"),
                new Squad(102L, "Modern Bugs"),
                new Squad(103L, "Cyberchase")
        );
        Classroom newClassroom = new Classroom();
        newClassroom.setSquads(newSquads);

        // Mocking repository
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        // Mocking studentService
        when(squadService.getAllSquadsById(classroomDTO.getGeneralUsersIds())).thenReturn(newSquads);

        // Mocking the mapping of students to response DTOs
        List<ResponseSquadDTO> responseSquads = List.of(
                new ResponseSquadDTO(101L, "SpringForce"),
                new ResponseSquadDTO(102L, "Modern Bugs"),
                new ResponseSquadDTO(103L, "Cyberchase")
        );
        when(squadService.mapToResponseSquads(newSquads)).thenReturn(responseSquads);

        // Call the method to be tested
        ResponseClassroomDTO response = classroomService.addSquadsToClassroom(classroomId, classroomDTO);

        // Verify the interactions
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(classroomRepository, times(1)).save(classroom);
        verify(squadService, times(1)).getAllSquadsById(classroomDTO.getGeneralUsersIds());
        verify(squadService, times(1)).attributeSquadsToClassroom(classroom, newSquads);
        verify(squadService, times(1)).mapToResponseSquads(newSquads);

        // Assertions
        assertNotNull(response);
        assertEquals(3, response.getSquads().size());
    }

    @Test
    void testAddSquadsToClassroom_NonExistingClassroom() {
        // Dados de teste
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        // Mock do repositório para retornar Optional vazio
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> classroomService.addSquadsToClassroom(classroomId, classroomDTO));

        // Verificação de chamada de método
        verify(classroomRepository).findById(classroomId);
        verify(squadService, never()).getAllSquadsById(any());
        verify(squadService, never()).attributeSquadsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveStudentsFromClassroom_SuccessfulRemoval() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        List<Long> studentIdsToRemove = Arrays.asList(101L, 102L, 103L);
        classroomDTO.setGeneralUsersIds(studentIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setStudents(new ArrayList<>());

        List<Student> students = List.of(
                new Student(101L, "John"),
                new Student(102L, "Jane"),
                new Student(103L, "Smith"),
                new Student(104L, "David")
        );
        classroom.getStudents().addAll(students);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(studentService.getAllStudentsById(classroomDTO.getGeneralUsersIds())).thenReturn(students.subList(0, 3));

        List<ResponseStudentDTO> responseStudents = List.of(
                new ResponseStudentDTO(101L, new ResponseUserDTO(), new Grade(), null, null)
        );

        when(studentService.mapToResponseStudents(classroom.getStudents())).thenReturn(responseStudents);

        // Execução do método a ser testado
        ResponseClassroomDTO response = classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);

        // Verificação dos resultados esperados
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getStudents().size());

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(studentService, times(1)).getAllStudentsById(classroomDTO.getGeneralUsersIds());
        verify(studentService, times(1)).attributeStudentsToClassroom(eq(null), eq(students.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveStudentsFromClassroom_ClassroomNotFound() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Execução do método a ser testado (deve lançar uma exceção)
        // classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);
        assertThrows(ResponseStatusException.class, () -> classroomService.removeStudentsFromClassroom(classroomId, classroomDTO));

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository).findById(classroomId);
        verify(studentService, never()).getAllStudentsById(any());
        verify(studentService, never()).attributeStudentsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveScrumMastersFromClassroom_SuccessfulRemoval() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        List<Long> scrumMastersIdsToRemove = Arrays.asList(101L, 102L, 103L);
        classroomDTO.setGeneralUsersIds(scrumMastersIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setScrumMasters(new ArrayList<>());

        List<ScrumMaster> scrumMasters = List.of(
                new ScrumMaster(101L, new User("John", "Fooley", "john.fooley@mail.com", "john.fooley")),
                new ScrumMaster(102L, new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new ScrumMaster(103L, new User("Smith", "Sutherland", "smith.sutherland@mail.com", "smith.sutherland")),
                new ScrumMaster(104L, new User("David", "Homeland", "david.homeland@mail.com", "david.homeland"))
        );
        classroom.getScrumMasters().addAll(scrumMasters);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(scrumMasterService.getAllScrumMastersById(classroomDTO.getGeneralUsersIds())).thenReturn(scrumMasters.subList(0, 3));

        List<ResponseScrumMasterDTO> responseScrumMasters = List.of(
                new ResponseScrumMasterDTO(104L, new ResponseUserDTO(), 201L)
        );

        when(scrumMasterService.mapToResponseScrumMasters(classroom.getScrumMasters())).thenReturn(responseScrumMasters);

        // Execução do método a ser testado
        ResponseClassroomDTO response = classroomService.removeScrumMastersFromClassroom(classroomId, classroomDTO);

        // Verificação dos resultados esperados
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getScrumMasters().size());

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(scrumMasterService, times(1)).getAllScrumMastersById(classroomDTO.getGeneralUsersIds());
        verify(scrumMasterService, times(1)).attributeScrumMastersToClassroom(eq(null), eq(scrumMasters.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveScrumMastersFromClassroom_ClassroomNotFound() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Execução do método a ser testado (deve lançar uma exceção)
        // classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);
        assertThrows(ResponseStatusException.class, () -> classroomService.removeScrumMastersFromClassroom(classroomId, classroomDTO));

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository).findById(classroomId);
        verify(scrumMasterService, never()).getAllScrumMastersById(any());
        verify(scrumMasterService, never()).attributeScrumMastersToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveInstructorsFromClassroom_SuccessfulRemoval() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        List<Long> instructorsIdsToRemove = Arrays.asList(101L, 102L, 103L);
        classroomDTO.setGeneralUsersIds(instructorsIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setInstructors(new ArrayList<>());

        List<Instructor> instructors = List.of(
                new Instructor(101L, new User("John", "Fooley", "john.fooley@mail.com", "john.fooley")),
                new Instructor(102L, new User("Jane", "Smith", "jane.smith@mail.com", "jane.smith")),
                new Instructor(103L, new User("Smith", "Sutherland", "smith.sutherland@mail.com", "smith.sutherland")),
                new Instructor(104L, new User("David", "Homeland", "david.homeland@mail.com", "david.homeland"))
        );
        classroom.getInstructors().addAll(instructors);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(instructorService.getAllInstructorsById(classroomDTO.getGeneralUsersIds())).thenReturn(instructors.subList(0, 3));

        List<ResponseInstructorDTO> responseInstructors = List.of(
                new ResponseInstructorDTO(104L, new ResponseUserDTO(), 201L)
        );

        when(instructorService.mapToResponseInstructors(classroom.getInstructors())).thenReturn(responseInstructors);

        // Execução do método a ser testado
        ResponseClassroomDTO response = classroomService.removeInstructorsFromClassroom(classroomId, classroomDTO);

        // Verificação dos resultados esperados
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getInstructors().size());

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(instructorService, times(1)).getAllInstructorsById(classroomDTO.getGeneralUsersIds());
        verify(instructorService, times(1)).attributeInstructorsToClassroom(eq(null), eq(instructors.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveInstructorsFromClassroom_ClassroomNotFound() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Execução do método a ser testado (deve lançar uma exceção)
        // classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);
        assertThrows(ResponseStatusException.class, () -> classroomService.removeInstructorsFromClassroom(classroomId, classroomDTO));

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository).findById(classroomId);
        verify(instructorService, never()).getAllInstructorsById(any());
        verify(instructorService, never()).attributeInstructorsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testRemoveSquadsFromClassroom_SuccessfulRemoval() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        List<Long> squadsIdsToRemove = Arrays.asList(101L, 102L, 103L);
        classroomDTO.setGeneralUsersIds(squadsIdsToRemove);

        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setSquads(new ArrayList<>());

        List<Squad> squads = List.of(
                new Squad(101L, "Springforce"),
                new Squad(102L, "Modern Bugs"),
                new Squad(103L, "Cyberchase"),
                new Squad(104L, "Os JEDPS")
        );
        classroom.getSquads().addAll(squads);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(classroomRepository.save(any(Classroom.class))).thenReturn(classroom);

        when(squadService.getAllSquadsById(classroomDTO.getGeneralUsersIds())).thenReturn(squads.subList(0, 3));

        List<ResponseSquadDTO> responseSquads = List.of(
                new ResponseSquadDTO(104L, "Os JEDPS")
        );

        when(squadService.mapToResponseSquads(classroom.getSquads())).thenReturn(responseSquads);

        // Execução do método a ser testado
        ResponseClassroomDTO response = classroomService.removeSquadsFromClassroom(classroomId, classroomDTO);

        // Verificação dos resultados esperados
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(1, response.getSquads().size());

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(squadService, times(1)).getAllSquadsById(classroomDTO.getGeneralUsersIds());
        verify(squadService, times(1)).attributeSquadsToClassroom(eq(null), eq(squads.subList(0, 3)));
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    public void testRemoveSquadsFromClassroom_ClassroomNotFound() {
        // Criação de dados simulados
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO();
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L, 3L));

        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.empty());

        // Execução do método a ser testado (deve lançar uma exceção)
        // classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);
        assertThrows(ResponseStatusException.class, () -> classroomService.removeSquadsFromClassroom(classroomId, classroomDTO));

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository).findById(classroomId);
        verify(squadService, never()).getAllSquadsById(any());
        verify(squadService, never()).attributeSquadsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }

    @Test
    public void testMapToResponseClassroom_WithNullStudentsInstructorsScrumMastersAndSquads_ShouldMapToResponseClassroomDTOWithEmptyLists() {
        // Arrange
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setTitle("Sample Classroom");
        classroom.setCoordinator(new Coordinator(new User("John", "Doe", "john.doe@mail.com", "john.doe")));

        when(coordinatorService.mapToResponseCoordinator(any(Coordinator.class))).thenReturn(
                new ResponseCoordinatorDTO(1L, new ResponseUserDTO(1L, "John", "Doe", "john.doe@mail.com"))
        );

        // Act
        ResponseClassroomDTO responseClassroomDTO = classroomService.mapToResponseClassroom(classroom);

        // Assert
        assertNotNull(responseClassroomDTO);
        assertEquals(1L, responseClassroomDTO.getId());
        assertEquals("Sample Classroom", responseClassroomDTO.getTitle());
        assertEquals("John", responseClassroomDTO.getCoordinator().getUser().getFirstName());
        assertEquals("Doe", responseClassroomDTO.getCoordinator().getUser().getLastName());
        assertEquals("john.doe@mail.com", responseClassroomDTO.getCoordinator().getUser().getEmail());
        assertNotNull(responseClassroomDTO.getStudents());
        assertTrue(responseClassroomDTO.getStudents().isEmpty());
        assertNotNull(responseClassroomDTO.getInstructors());
        assertTrue(responseClassroomDTO.getInstructors().isEmpty());
        assertNotNull(responseClassroomDTO.getScrumMasters());
        assertTrue(responseClassroomDTO.getScrumMasters().isEmpty());
        assertNotNull(responseClassroomDTO.getSquads());
        assertTrue(responseClassroomDTO.getSquads().isEmpty());
    }

    @Test
    public void testMapToResponseClassroom_WithNonNullStudentsInstructorsScrumMastersAndSquads_ShouldMapToResponseClassroomDTOWithNonEmptyLists() {
        // Arrange
        Classroom classroom = new Classroom();
        classroom.setId(2L);
        classroom.setTitle("Another Classroom");
        Coordinator coordinator = new Coordinator(new User("John", "Doe", "john.doe@mail.com", "john.doe"));
        classroom.setCoordinator(coordinator);

        ResponseCoordinatorDTO responseCoordinatorDTO = new ResponseCoordinatorDTO(coordinator.getId(),
                new ResponseUserDTO(
                        coordinator.getUser().getId(),
                        coordinator.getUser().getFirstName(),
                        coordinator.getUser().getLastName(),
                        coordinator.getUser().getEmail()
                )
        );

        List<Student> students = Arrays.asList(
                new Student(new User("Virginia", "Montana", "virginia.montana@mail.com", "viginia.montana")),
                new Student(new User("Bob", "Phill", "bob.phill@mail.com", "bob.phill"))
        );
        List<Instructor> instructors = Arrays.asList(
                new Instructor(new User("Eve", "William", "eve.william@mail.com", "eve.william")),
                new Instructor(new User("Michael", "Souza", "michael.souza@mail.com", "michael.souza"))
        );
        List<ScrumMaster> scrumMasters = Arrays.asList(
                new ScrumMaster(new User("Oscar", "Potter", "oscar.pottter@mail.com", "oscar.pottter")),
                new ScrumMaster(new User("Pamela", "Kratkovswky", "pamela.kratkovswky@mail.com", "pamela.kratkovswky"))
        );
        List<Squad> squads = Arrays.asList(new Squad(7L, "Squad A"), new Squad(8L, "Squad B"));

        classroom.setStudents(students);
        classroom.setInstructors(instructors);
        classroom.setScrumMasters(scrumMasters);
        classroom.setSquads(squads);

        // Mock the behavior of the service mapping methods
        when(coordinatorService.mapToResponseCoordinator(any(Coordinator.class))).thenReturn(responseCoordinatorDTO);

        when(studentService.mapToResponseStudents(students)).thenReturn(
                Arrays.asList(
                        new ResponseStudentDTO(1L, new ResponseUserDTO(1L, "Virginia", "Montana", "virginia.montana@mail.com")),
                        new ResponseStudentDTO(2L, new ResponseUserDTO(2L, "Bob", "Phill", "bob.phill@mail.com"))
                )
        );

        when(instructorService.mapToResponseInstructors(instructors)).thenReturn(
                Arrays.asList(
                        new ResponseInstructorDTO(3L, new ResponseUserDTO(3L, "Eve", "William", "eve.william@mail.com")),
                        new ResponseInstructorDTO(4L, new ResponseUserDTO(4L, "Michael", "Souza", "michael.souza@mail.com"))
                )
        );

        when(scrumMasterService.mapToResponseScrumMasters(scrumMasters)).thenReturn(
                Arrays.asList(
                        new ResponseScrumMasterDTO(5L, new ResponseUserDTO(5L, "Oscar", "Potter", "oscar.pottter@mail.com")),
                        new ResponseScrumMasterDTO(6L, new ResponseUserDTO(6L, "Pamela", "Kratkovswky", "pamela.kratkovswky@mail.com"))
                )
        );

        when(squadService.mapToResponseSquads(squads)).thenReturn(
                Arrays.asList(new ResponseSquadDTO(7L, "Squad A"), new ResponseSquadDTO(8L, "Squad B"))
        );

        // Act
        ResponseClassroomDTO responseClassroomDTO = classroomService.mapToResponseClassroom(classroom);

        // Assert
        assertNotNull(responseClassroomDTO);
        assertEquals(2L, responseClassroomDTO.getId());
        assertEquals("Another Classroom", responseClassroomDTO.getTitle());
        assertEquals(classroom.getCoordinator().getUser().getEmail(), responseClassroomDTO.getCoordinator().getUser().getEmail());
        assertNotNull(responseClassroomDTO.getStudents());
        assertEquals(2, responseClassroomDTO.getStudents().size());
        assertNotNull(responseClassroomDTO.getInstructors());
        assertEquals(2, responseClassroomDTO.getInstructors().size());
        assertNotNull(responseClassroomDTO.getScrumMasters());
        assertEquals(2, responseClassroomDTO.getScrumMasters().size());
        assertNotNull(responseClassroomDTO.getSquads());
        assertEquals(2, responseClassroomDTO.getSquads().size());
    }
}
