package uol.compass.cspcapi;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.classroom.ClassroomRepository;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.student.StudentService;


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
}
