package uol.compass.cspcapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadRepository;
import uol.compass.cspcapi.domain.Squad.SquadService;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.User;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SquadServiceTest {

    @Mock
    private SquadRepository squadRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private SquadService squadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Save
    @Test
    public void testSaveSquad_Success() {
        // Criar um DTO simulado com o nome do esquadrão a ser criado
        CreateSquadDTO squadDTO = new CreateSquadDTO();
        squadDTO.setName("New Squad");

        // Configurar o comportamento simulado do repositório para retornar um Optional vazio, indicando que o esquadrão não existe
        when(squadRepository.findByName("New Squad")).thenReturn(Optional.empty());

        // Executar o método sendo testado
        ResponseSquadDTO result = squadService.save(squadDTO);

        // Verificar se o resultado não é nulo e se o nome do esquadrão no DTO de resposta é igual ao nome do esquadrão no DTO de entrada
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(squadDTO.getName());
    }

    //Update squads




    //Remove students in squads
    @Test
    public void testRemoveStudentsFromSquad_Success() {
        Long squadId = 1L;

        // Criar um esquadrão simulado com estudantes
        Squad squad = new Squad();
        squad.setId(squadId);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Student 1"));
        students.add(new Student(2L, "Student 2"));
        squad.setStudents(students);

        // Configurar o comportamento simulado do repositório para retornar o esquadrão simulado
        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        // Configurar o comportamento simulado do serviço de estudante para retornar os estudantes simulados
        List<Long> studentsIdsToRemove = new ArrayList<>();
        studentsIdsToRemove.add(1L);
        when(studentService.getAllStudentsById(studentsIdsToRemove)).thenReturn(students.subList(0, 1));

        // Executar o método sendo testado
        ResponseSquadDTO result = squadService.removeStudentsFromSquad(squadId, new UpdateSquadDTO(studentsIdsToRemove));

        // Verificar se os estudantes foram removidos corretamente
        assertTrue(squad.getStudents().size() == 1);
        assertEquals(2L, squad.getStudents().get(0).getId());

        // Verificar se o método save do repositório foi chamado corretamente com o esquadrão
        verify(squadRepository).save(squad);

        // Verificar se o resultado possui as informações corretas do esquadrão mapeado para o DTO
        assertEquals(squad.getId(), result.getId());
        // Verificar outras propriedades do DTO, se houver
    }
    @Test
    public void testRemoveStudentsFromSquad_Failure() {
        Long squadId = 1L;

        UpdateSquadDTO squadDTO = new UpdateSquadDTO();
        squadDTO.setStudentsIds(List.of(1001L));

        when(squadRepository.findById(squadId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.removeStudentsFromSquad(squadId, squadDTO))
                .withMessageContaining("Squad not found");
    }



    //Add squads in classrooms
    @Test
    public void testAttributeSquadsToClassroom_Success() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);

        Squad squad1 = new Squad();
        Squad squad2 = new Squad();

        List<Squad> squads = new ArrayList<>();
        squads.add(squad1);
        squads.add(squad2);

        when(squadRepository.saveAll(squads)).thenReturn(squads);

        List<Squad> result = squadService.attributeSquadsToClassroom(classroom, squads);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getClassroom()).isEqualTo(classroom);
        assertThat(result.get(1).getClassroom()).isEqualTo(classroom);
    }
    @Test
    public void testAttributeSquadsToClassroom_Failure() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);

        Squad squad1 = new Squad();
        Squad squad2 = new Squad();

        List<Squad> squads = new ArrayList<>();
        squads.add(squad1);
        squads.add(squad2);

        when(squadRepository.saveAll(squads)).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.attributeSquadsToClassroom(classroom, squads))
                .withMessageContaining("Classroom not found");
    }



    //GetById
    @Test
    public void testGetSquadById_Success() {
        Long squadId = 1L;
        Squad squad = new Squad("Test Squad");
        squad.setId(squadId);

        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        ResponseSquadDTO result = squadService.getById(squadId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(squadId);
        assertThat(result.getName()).isEqualTo("Test Squad");
    }
    @Test
    public void testGetSquadById_Failure() {
        Long squadId = 1L;

        when(squadRepository.findById(squadId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.getById(squadId))
                .withMessageContaining("squad not found");
    }



    //GetAll
    @Test
    public void testGetAll_Success() {
        List<Squad> squads = new ArrayList<>();
        squads.add(new Squad("Squad 1"));
        squads.add(new Squad("Squad 2"));

        when(squadRepository.findAll()).thenReturn(squads);

        List<ResponseSquadDTO> result = squadService.getAll();

        verify(squadRepository).findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetAllSquads_Failure() {
        Squad squad1 = new Squad("Squad 1");
        Squad squad2 = new Squad("Squad 2");

        when(squadRepository.findAll()).thenReturn(Arrays.asList(squad1, squad2));

        List<ResponseSquadDTO> result = squadService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Squad 1");
        assertThat(result.get(1).getName()).isEqualTo("Squad 2");
    }



    //Delete
    @Test
    public void testDeleteSquad_Success() {
        Long squadId = 1L;
        Squad squad = new Squad("Test Squad");
        squad.setId(squadId);

        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        squadService.delete(squadId);

        verify(squadRepository).delete(squad);
    }

    @Test
    public void testDeleteSquad_Failure() {
        Long squadId = 1L;

        when(squadRepository.findById(squadId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.delete(squadId))
                .withMessageContaining("Squad not found");
    }


    //Map response squad




}
