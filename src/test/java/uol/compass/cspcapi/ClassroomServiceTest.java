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
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.classroom.ClassroomRepository;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
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
        // Dados de teste
        Long classroomId = 1L;
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO("Classroom 105", 1L);
        classroomDTO.setGeneralUsersIds(Arrays.asList(1L, 2L));

        Classroom existingClassroom = new Classroom("Classroom 101", new Coordinator(new User("Teste", "Gihs", "teste.gihs@mail.com", "teste.gihs")));

        List<Student> existingStudents = new ArrayList<>();
        existingStudents.add(new Student(new User("Victor", "Silva", "victor.silva@mail.com", "victor.silva")));
        existingStudents.add(new Student(new User("Gilberto", "Maderos", "gilberto.maderos", "gilberto.maderos")));
        existingClassroom.setStudents(existingStudents);

        List<Student> newStudents = new ArrayList<>();
        newStudents.add(new Student(new User("Amelie", "Watson", "amelie.watson@mail.com", "amelie.watson")));
        newStudents.add(new Student(new User("Bob", "William", "bob.william@mail.com", "bob.william")));

        // Mock do repositório para retornar a sala de aula existente
        when(classroomRepository.findById(classroomId)).thenReturn(java.util.Optional.of(existingClassroom));

        // Mock do serviço de estudantes para retornar os novos estudantes
        when(studentService.getAllStudentsById(classroomDTO.getGeneralUsersIds())).thenReturn(newStudents);

        // Mock do repositório para retornar a sala de aula após o save()
        when(classroomRepository.save(any())).thenReturn(existingClassroom);

//        existingStudents.addAll(newStudents);
//        existingClassroom.setStudents(existingStudents);
//        when(classroomService.addStudentsToClassroom(anyLong(), any(UpdateClassroomDTO.class))).thenReturn(new ResponseClassroomDTO(
//                existingClassroom.getId(),
//                existingClassroom.getTitle(),
//                existingClassroom.getCoordinator(),
//                new ResponseStudentDTO(existingClassroom.getId(), new ResponseUserDTO(existingClassroom.getStudents().get(0).getUser().getId(), )),
//                existingClassroom.getInstructors(),
//                existingClassroom.getScrumMasters(),
//                existingClassroom.getSquads()
//        ));

        // Executando o método
        ResponseClassroomDTO result = classroomService.addStudentsToClassroom(classroomId, classroomDTO);

        // Verificação
        verify(classroomRepository).findById(classroomId);
        verify(studentService).getAllStudentsById(classroomDTO.getGeneralUsersIds());

        // Captura os argumentos passados para a primeira chamada do método attributeStudentsToClassroom()
        ArgumentCaptor<Classroom> classroomCaptor1 = ArgumentCaptor.forClass(Classroom.class);
        ArgumentCaptor<List<Student>> studentsCaptor1 = ArgumentCaptor.forClass(List.class);
        verify(studentService, times(1)).attributeStudentsToClassroom(classroomCaptor1.capture(), studentsCaptor1.capture());

        // Verifica se a primeira chamada do método contém todos os estudantes existentes
        List<Student> studentsFirstCall = studentsCaptor1.getValue();
        assertTrue(studentsFirstCall.containsAll(existingStudents));

        // Captura os argumentos passados para a segunda chamada do método attributeStudentsToClassroom()
        ArgumentCaptor<Classroom> classroomCaptor2 = ArgumentCaptor.forClass(Classroom.class);
        ArgumentCaptor<List<Student>> studentsCaptor2 = ArgumentCaptor.forClass(List.class);
        verify(studentService, times(1)).attributeStudentsToClassroom(classroomCaptor2.capture(), studentsCaptor2.capture());

        // Verifica se a segunda chamada do método contém todos os estudantes existentes mais os novos estudantes
        List<Student> studentsSecondCall = studentsCaptor2.getValue();
        assertTrue(studentsSecondCall.containsAll(existingStudents));
        assertTrue(studentsSecondCall.containsAll(newStudents));

        verify(classroomRepository).save(existingClassroom);

        // Verifica se o resultado não é nulo
        assertNotNull(result);

        System.out.println(existingStudents.size() + "existing " + newStudents.size() + "new " + result.getStudents().size() + "actual");
        System.out.println(result);
        System.out.println(result.getStudents());

        // Verifica se a quantidade de estudantes na sala de aula no resultado está correta
        //assertEquals(existingStudents.size() + newStudents.size(), result.getStudents().size());


        /*
        verify(studentService).attributeStudentsToClassroom(existingClassroom, newStudents);

        verify(classroomRepository).save(existingClassroom);

        // Verifica se o resultado não é nulo
        assertNotNull(result);

        // Verifica se a quantidade de estudantes na sala de aula no resultado está correta
        assertEquals(existingStudents.size() + newStudents.size(), result.getStudents().size());

         */
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
}
