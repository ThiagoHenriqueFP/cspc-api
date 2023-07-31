package uol.compass.cspcapi;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadRepository;
import uol.compass.cspcapi.domain.Squad.SquadService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SquadServiceTest {

    @Mock
    private SquadRepository squadRepository;
    @InjectMocks
    private SquadService squadService;

    //GetById
    @Test
    public void testGetSquadById_Success() {
        Long squadId = 1L;
        Squad squad = new Squad("Test Squad");
        squad.setId(squadId);
        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        Squad result = squadService.getById(squadId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(squadId);
        assertThat(result.getName()).isEqualTo("Test Squad");
    }
    @Test
    public void testGetSquadById_Failure() {
        Long nonExistentSquadId = 100L;

        when(squadRepository.findById(nonExistentSquadId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.getById(nonExistentSquadId))
                .withMessageContaining("Squad not found");
    }

    //GetAll
    @Test
    public void testGetAllSquads_Success() {
        // Criar dados simulados para o teste
        List<Squad> squads = new ArrayList<>();
        squads.add(new Squad("Squad 1"));
        squads.add(new Squad("Squad 2"));
        squads.add(new Squad("Squad 3"));

        // Configurar o comportamento simulado do repositório para retornar a lista de squads
        when(squadRepository.findAll()).thenReturn(squads);

        // Executar o método sendo testado
        List<Squad> result = squadService.getAll();

        // Verificar se o método retornou a lista correta de squads
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getName()).isEqualTo("Squad 1");
        assertThat(result.get(1).getName()).isEqualTo("Squad 2");
        assertThat(result.get(2).getName()).isEqualTo("Squad 3");
    }


    //Delete
    @Test
    public void testDeleteSquad_Success() {
        Long squadId = 1L;
        Squad squad = new Squad("Test Squad");
        squad.setId(squadId);

        when(squadRepository.findById(squadId)).thenReturn(Optional.of(squad));

        boolean result = squadService.delete(squadId);

        assertThat(result).isTrue();

        verify(squadRepository).delete(squad);
    }

    @Test
    public void testDeleteSquad_Failure() {
        Long nonExistentSquadId = 100L;

        when(squadRepository.findById(nonExistentSquadId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> squadService.delete(nonExistentSquadId))
                .withMessageContaining("Squad not found");
    }







}
