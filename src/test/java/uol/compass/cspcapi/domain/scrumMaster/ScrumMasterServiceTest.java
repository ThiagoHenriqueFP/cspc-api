package uol.compass.cspcapi.domain.scrumMaster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScrumMasterServiceTest {
    @Mock
    private ScrumMasterRepository scrumMasterRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private ScrumMasterService scrumMasterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveClassroom() {
        Long scrumMasterId = 1L;

        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);

        ScrumMaster existingScrumMaster = new ScrumMaster(user_1);
        existingScrumMaster.setId(scrumMasterId);

        CreateUserDTO userDTO_1 = new CreateUserDTO(user_1.getFirstName(), user_1.getLastName(), user_1.getEmail(), user_1.getPassword());
        CreateScrumMasterDTO scrumMasterDTO_1 = new CreateScrumMasterDTO(user_1);

        ResponseUserDTO responseUserDTO = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");

        when(scrumMasterRepository.save(any(ScrumMaster.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String encryptedPassword = "$a#10alsks";

        // when(passwordEncoder.encode(scrumMasterDTO_1.getUser().getPassword())).thenReturn("encryptedPassword");
        // when(roleService.findRoleByName(anyString())).thenReturn(new Role("ROLE_SCRUM_MASTER"));

        ResponseScrumMasterDTO result = scrumMasterService.save(scrumMasterDTO_1);

        // verify(userService).findByEmail(anyString());
        // verify(roleService, times(1)).findRoleByName(anyString());
        verify(scrumMasterRepository).save(any(ScrumMaster.class));

        assertNotNull(result);

        assertEquals(scrumMasterDTO_1.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(scrumMasterDTO_1.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(scrumMasterDTO_1.getUser().getEmail(), result.getUser().getEmail());
    }

    @Test
    public void testGetById_ExistingId_ReturnsResponseScrumMasterDTO() {
        // Dados de teste
        Long scrumMasterId = 1L;
        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        ScrumMaster existingScrumMaster = new ScrumMaster(user_1);
        existingScrumMaster.setId(scrumMasterId);

        // Mock do repositório
        when(scrumMasterRepository.findById(scrumMasterId)).thenReturn(Optional.of(existingScrumMaster));

        // Executando o método
        ResponseScrumMasterDTO result = scrumMasterService.getById(scrumMasterId);

        // Verificação
        verify(scrumMasterRepository).findById(scrumMasterId);
        verify(scrumMasterRepository, times(1)).findById(scrumMasterId);
        // Verifica se o resultado não é nulo
        assertNotNull(result);

        // Verifica se o ID da sala de aula no resultado é o mesmo que o fornecido na entrada
        assertEquals(scrumMasterId, result.getId());
    }

    @Test
    public void testGetById_NonExistingId_ThrowsResponseStatusException() {
        Long id = 999L;

        when(scrumMasterRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert - Usando try-catch para capturar a exceção esperada
        try {
            scrumMasterService.getById(id);
            fail("Expected ResponseStatusException, but it was not thrown.");
        } catch (ResponseStatusException e) {
            // Verificar a mensagem ou outros detalhes da exceção, se necessário
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertEquals("user not found", e.getReason());
        }
    }

    @Test
    public void testGetAllScrumMaster() {
        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        ScrumMaster scrumMaster_1 = new ScrumMaster(user_1);
        scrumMaster_1.setId(1L);

        User user_2 = new User("First", "Second", "first.second2@mail.com", "first.second2");
        user_1.setId(2L);
        ScrumMaster scrumMaster_2 = new ScrumMaster(user_2);
        scrumMaster_2.setId(2L);

        when(scrumMasterRepository.findAll()).thenReturn(Arrays.asList(scrumMaster_1, scrumMaster_2));

        List<ResponseScrumMasterDTO> result = scrumMasterService.getAll();

        verify(scrumMasterRepository).findAll();

        assertNotNull(result);

        assertEquals(2, result.size());

        assertEquals(scrumMaster_1.getId(), result.get(0).getId());
        assertEquals(scrumMaster_2.getId(), result.get(1).getId());
    }

    @Test
    void testUpdateScrumMaster_NonexistingScrumMaster() {
        // Dados de teste
        Long scrumMasterId = 1L;

        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        ScrumMaster coordinator_1 = new ScrumMaster(user_1);
        coordinator_1.setId(scrumMasterId);

        UpdateUserDTO userDTO_1 = new UpdateUserDTO(user_1.getFirstName(), user_1.getLastName(), user_1.getEmail(), user_1.getPassword());
        UpdateScrumMasterDTO scrumMasterDTO = new UpdateScrumMasterDTO(user_1);

        // Mock do repositório para retornar Optional vazio
        when(scrumMasterRepository.findById(scrumMasterId)).thenReturn(Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> scrumMasterService.update(scrumMasterId, scrumMasterDTO));

        // Verificação de chamada de método
        verify(scrumMasterRepository).findById(scrumMasterId);
        verify(scrumMasterRepository, never()).save(any(ScrumMaster.class));
    }

    @Test
    void testDeleteScrumMaster_existingScrumMaster() {
        // Dados de teste
        Long scrumMasterId = 1L;

        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        ScrumMaster existingScrumMaster = new ScrumMaster(user_1);
        existingScrumMaster.setId(scrumMasterId);

        // Mock do repositório para retornar a sala de aula existente
        when(scrumMasterRepository.findById(scrumMasterId)).thenReturn(Optional.of(existingScrumMaster));

        // Executando o método
        scrumMasterService.delete(scrumMasterId);

        // Verificação
        verify(scrumMasterRepository).findById(scrumMasterId);
        verify(scrumMasterRepository).delete(existingScrumMaster);
    }

    @Test
    void testDeleteScrumMaster_NonexistingScrumMaster() {
        // Dados de teste
        Long scrumMasterId = 1L;

        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        ScrumMaster existingScrumMaster = new ScrumMaster(user_1);
        existingScrumMaster.setId(scrumMasterId);

        // Mock do repositório para retornar Optional vazio
        when(scrumMasterRepository.findById(scrumMasterId)).thenReturn(Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> scrumMasterService.delete(scrumMasterId));

        // Verificação de chamada de método
        verify(scrumMasterRepository).findById(scrumMasterId);
        verify(scrumMasterRepository, never()).save(any(ScrumMaster.class));
        verify(scrumMasterRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testMapToResponseScrumMaster() {
        // Mock Coordinator object
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        user.setId(10L);
        ScrumMaster scrumMaster = new ScrumMaster(user);
        scrumMaster.setId(1L);

        ResponseUserDTO responseUserDTO = new ResponseUserDTO(10L, "First", "Second", "first.second@mail.com");

        //when(userService.mapToResponseUser(any(User.class))).thenReturn(responseUserDTO);

        ResponseScrumMasterDTO ResponseScrumMasterDTO = scrumMasterService.mapToResponseScrumMaster(scrumMaster);

        assertNotNull(ResponseScrumMasterDTO);
        assertEquals(1L, ResponseScrumMasterDTO.getId());
        //assertEquals(10L, ResponseScrumMasterDTO.getUser().getId());

        //verify(userService, times(1)).mapToResponseUser(any(User.class));
    }
}