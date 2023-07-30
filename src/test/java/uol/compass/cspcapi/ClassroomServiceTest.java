package uol.compass.cspcapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.classroom.ClassroomRepository;
import uol.compass.cspcapi.domain.classroom.ClassroomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private ClassroomService classroomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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
    }

    @Test
    public void testListClassroom_EmptyList_Failure() {
        // Criar dados simulados para o teste (lista vazia de salas de aula)
        List<Classroom> classrooms = new ArrayList<>();

        // Configurar o comportamento simulado do repositório para retornar a lista de salas de aula vazia
        when(classroomRepository.findAll()).thenReturn(classrooms);

        // Executar o método sendo testado e verificar se a exceção correta é lançada
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.listClassroom())
                .withMessageContaining("No classrooms found"); // Esperado lançamento de exceção com a mensagem "No classrooms found"
    }

    //GetById
    @Test
    public void testGetClassroomById_Success() {
        Long classroomId = 1L;

        Classroom classroom = new Classroom("Test Classroom");

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));

        Classroom result = classroomService.getById(classroomId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(classroomId);
        assertThat(result.getTitle()).isEqualTo("Test Classroom");
    }

    @Test
    public void testGetClassroomById_Failure() {
        Long classroomId = 1L;

        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> classroomService.getById(classroomId))
                .withMessageContaining("Classroom not found");
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
