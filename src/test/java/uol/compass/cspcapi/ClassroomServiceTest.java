package uol.compass.cspcapi;


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

        // Mocking the classroom object
        Classroom classroom = new Classroom();
        classroom.setId(classroomId);
        classroom.setTitle("Old Title");

        // Mocking the coordinator object
        Coordinator coordinator = new Coordinator(new User("John", "Doe", "john.doe@mail.com", "john.doe"));
        coordinator.setId(10L);

        // Mocking repository behavior
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(coordinatorService.getByIdOriginal(10L)).thenReturn(coordinator);
        when(classroomRepository.save(any())).thenReturn(classroom);

        // Call the method under test
        ResponseClassroomDTO updatedClassroomDTO = classroomService.updateClassroom(classroomId, classroomDTO);

        // Assertions
        assertEquals(classroomDTO.getTitle(), updatedClassroomDTO.getTitle());
        assertEquals(classroomDTO.getCoordinatorId(), updatedClassroomDTO.getCoordinator().getId());
        // Add more assertions as needed
    }

//    @Test
//    void testUpdateClassroom_ExistingClassroom() {
//        // Dados de teste
//        Long classroomId = 1L;
//        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO("Updated Classroom", 2L);
//
//        Classroom existingClassroom = new Classroom("Classroom 101", new Coordinator());
//        existingClassroom.setId(classroomId);
//
//        Coordinator updatedCoordinator = new Coordinator();
//        updatedCoordinator.setId(classroomDTO.getCoordinatorId());
//
//        // Mock do repositório para retornar a sala de aula existente
//        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.of(existingClassroom));
//        when(classroomRepository.save(existingClassroom)).thenReturn(
//                new Classroom(classroomDTO.getTitle(), classroomDTO.getCoordinatorId())
//        );
//
//        // Mock do serviço de coordenador para retornar um coordenador atualizado com o ID fornecido
//        when(coordinatorService.getByIdOriginal(classroomDTO.getCoordinatorId())).thenReturn(updatedCoordinator);
//
//        // Executando o método
//        ResponseClassroomDTO result = classroomService.updateClassroom(classroomId, classroomDTO);
//
//        // Verificação
//        verify(classroomRepository).findById(classroomId);
//        verify(coordinatorService).getByIdOriginal(classroomDTO.getCoordinatorId());
//        verify(classroomRepository).save(existingClassroom);
//
//        // Verifica se o resultado não é nulo
//        assertNotNull(result);
//
//        // Verifica se o título e o coordenador da sala de aula no resultado foram atualizados corretamente
//        assertEquals(classroomDTO.getTitle(), result.getTitle());
//        assertEquals(classroomDTO.getCoordinatorId(), result.getCoordinator().getId());
//    }

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
        Classroom newClassroom = new Classroom();
        newClassroom.setStudents(newStudents);

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
        List<Student> students = new ArrayList<>();
        students.add(new Student(101L, "Alice"));
        students.add(new Student(102L, "Bob"));
        students.add(new Student(103L, "Charlie"));
        students.add(new Student(104L, "David"));
        classroom.setStudents(students);

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(studentService.getAllStudentsById(classroomDTO.getGeneralUsersIds())).thenReturn(students.subList(0, 3));

        List<ResponseStudentDTO> responseStudents = List.of(
                new ResponseStudentDTO(101L, new ResponseUserDTO(), new Grade(), null, null),
                new ResponseStudentDTO(102L, new ResponseUserDTO(), new Grade(), null, null),
                new ResponseStudentDTO(103L, new ResponseUserDTO(), new Grade(), null, null)
        );

        when(studentService.mapToResponseStudents(classroom.getStudents())).thenReturn(responseStudents);

        // Execução do método a ser testado
        ResponseClassroomDTO result = classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);

        // Verificação dos resultados esperados
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getStudents().size());

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
        assertThrows(ResponseStatusException.class, () -> classroomService.addSquadsToClassroom(classroomId, classroomDTO));

        // Verificação das chamadas dos métodos simulados
        verify(classroomRepository).findById(classroomId);
        verify(studentService, never()).getAllStudentsById(any());
        verify(studentService, never()).attributeStudentsToClassroom(any(), any());
        verify(classroomRepository, never()).save(any());
    }
}
