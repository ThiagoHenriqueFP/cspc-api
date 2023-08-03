package uol.compass.cspcapi.domain.coordinator;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.UpdateCoordinatorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static uol.compass.cspcapi.commons.ClassroomsConstants.VALID_COORDINATOR;
import static uol.compass.cspcapi.commons.CoordinatorsConstants.*;

@ExtendWith(MockitoExtension.class)
public class CoordinatorServiceTest {
    @Mock
    private CoordinatorRepository coordinatorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private PasswordEncoder passwordEncrypt;

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleService roleServiceMock;

    @InjectMocks
    private CoordinatorService coordinatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_Success() {
        User user = new User("John", "Doe", "johndoe@compass.com", "password");
        Coordinator coordinator = new Coordinator(user);

        CreateUserDTO userDTO = new CreateUserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO(userDTO);

        coordinator.setId(1L);

//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(coordinatorRepository.save(any(Coordinator.class))).thenReturn(coordinator);
//        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role("ROLE_COORDINATOR")));

        ResponseCoordinatorDTO response = coordinatorService.save(coordinatorDTO);

        assertEquals(coordinator.getId(), response.getId());
        assertEquals(coordinator.getUser().getFirstName(), response.getUser().getFirstName());
        assertEquals(coordinator.getUser().getLastName(), response.getUser().getLastName());
        assertEquals(coordinator.getUser().getEmail(), response.getUser().getEmail());

    }

    @Test
    public void testGetById_ExistingId_ReturnsResponseCoordinatorDTO() {
        Long coordinatorId = 1L;
        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        Coordinator existingCoordinator = new Coordinator(user_1);
        existingCoordinator.setId(coordinatorId);

        when(coordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(existingCoordinator));

        ResponseCoordinatorDTO result = coordinatorService.getById(coordinatorId);

        verify(coordinatorRepository).findById(coordinatorId);
        verify(coordinatorRepository, times(1)).findById(coordinatorId);

        assertNotNull(result);

        assertEquals(coordinatorId, result.getId());
    }

//    @Test
//    void testGetById_NonExistingId_ReturnsError() {
//        // Dados de teste
//        Long coordinatorId = 1L;
//
//        // Mock do repositório para retornar Optional vazio
//        when(coordinatorRepository.findById(coordinatorId)).thenReturn(Optional.empty());
//
//        // Verificação de exceção quando a sala de aula não é encontrada
//        assertThrows(ResponseStatusException.class, () -> coordinatorService.getById(coordinatorId));
//
//        // Verificação de chamada de método
//        verify(coordinatorRepository).findById(coordinatorId);
//    }

    @Test
    public void testGetById_NonExistingId_ThrowsResponseStatusException() {
        Long id = 2L;

        when(coordinatorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert - Usando try-catch para capturar a exceção esperada
        try {
            coordinatorService.getById(id);
            fail("Expected ResponseStatusException, but it was not thrown.");
        } catch (ResponseStatusException e) {
            // Verificar a mensagem ou outros detalhes da exceção, se necessário
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertEquals("user not found", e.getReason());
        }
    }

    @Test
    public void testGetByIdOriginal_ExistingId_ReturnsCoordinator() {
        // Arrange
        Long id = 5L;
        COORDINATOR_1.setId(5L);

        when(coordinatorRepository.findById(id)).thenReturn(Optional.of(COORDINATOR_1));
//        when(userServiceMock.mapToResponseUser(any(User.class))).thenReturn(USER_DTO_1);
        // Act
        Coordinator actualResponse = coordinatorService.getByIdOriginal(id);

        // Assert
        assertEquals(COORDINATOR_1.getId(), actualResponse.getId());
        assertEquals(COORDINATOR_1, actualResponse);
        verify(coordinatorRepository, times(1)).findById(id);
    }

    @Test
    public void testGetByIdOriginal_NonExistingId_ThrowsResponseStatusException() {
        Long id = 2L;

        when(coordinatorRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert - Usando try-catch para capturar a exceção esperada
        try {
            coordinatorService.getByIdOriginal(id);
            fail("Expected ResponseStatusException, but it was not thrown.");
        } catch (ResponseStatusException e) {
            // Verificar a mensagem ou outros detalhes da exceção, se necessário
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertEquals("user not found", e.getReason());
        }
    }

    @Test
    public void testGetAllCoordinators() {
        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        Coordinator coordinator_1 = new Coordinator(user_1);
        coordinator_1.setId(1L);

        User user_2 = new User("First", "Second", "first.second2@mail.com", "first.second2");
        user_1.setId(2L);
        Coordinator coordinator_2 = new Coordinator(user_2);
        coordinator_1.setId(2L);

        when(coordinatorRepository.findAll()).thenReturn(Arrays.asList(coordinator_1, coordinator_2));

        List<ResponseCoordinatorDTO> result = coordinatorService.getAll();

        verify(coordinatorRepository).findAll();

        assertNotNull(result);

        assertEquals(2, result.size());

        assertEquals(coordinator_1.getId(), result.get(0).getId());
        assertEquals(coordinator_2.getId(), result.get(1).getId());
    }

    @Test
    public void testUpdateCoordinator_Success() {
        // Mocking input data
        Long coordinatorId = 1L;

        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        user.setId(1L);
        Coordinator coordinator = new Coordinator(user);
        coordinator.setId(coordinatorId);

        User newUser = new User("User1", "User2", "user@mail.com", "user1.user2");
        newUser.setId(1L);
        Coordinator newCoordinator = new Coordinator(newUser);
        newCoordinator.setId(coordinatorId);

        UpdateUserDTO userDTO = new UpdateUserDTO("User1", "User2", "user@mail.com", "user1.user2");
        UpdateCoordinatorDTO coordinatorDTO = new UpdateCoordinatorDTO(userDTO);

        when(coordinatorRepository.findById(coordinatorId)).thenReturn(Optional.of(coordinator));
        when(coordinatorRepository.save(any(Coordinator.class))).thenReturn(newCoordinator);

        ResponseCoordinatorDTO response = coordinatorService.update(coordinatorId, coordinatorDTO);

        assertEquals(coordinatorId, response.getId());
        assertEquals(coordinatorDTO.getUser().getFirstName(), response.getUser().getFirstName());
        assertEquals(coordinatorDTO.getUser().getLastName(), response.getUser().getLastName());
        assertEquals(coordinatorDTO.getUser().getEmail(), response.getUser().getEmail());
    }

    @Test
    void testUpdateCoordinator_NonExistingCoordinator() {
        // Dados de teste
        Long coordinatorId = 1L;

        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        Coordinator coordinator_1 = new Coordinator(user_1);
        coordinator_1.setId(coordinatorId);

        UpdateUserDTO userDTO_1 = new UpdateUserDTO(user_1.getFirstName(), user_1.getLastName(), user_1.getEmail(), user_1.getPassword());
        UpdateCoordinatorDTO coordinatorDTO = new UpdateCoordinatorDTO(userDTO_1);

        // Mock do repositório para retornar Optional vazio
        when(coordinatorRepository.findById(coordinatorId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> coordinatorService.update(coordinatorId, coordinatorDTO));

        // Verificação de chamada de método
        verify(coordinatorRepository).findById(coordinatorId);
        verify(coordinatorRepository, never()).save(any(Coordinator.class));
    }

    @Test
    void testDeleteCoordinator_ExistingCoordinator() {
        // Dados de teste
        Long coordinatorId = 1L;

        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        Coordinator existingCoordinator = new Coordinator(user_1);
        existingCoordinator.setId(coordinatorId);

        // Mock do repositório para retornar a sala de aula existente
        when(coordinatorRepository.findById(coordinatorId)).thenReturn(java.util.Optional.of(existingCoordinator));

        // Executando o método
        coordinatorService.deleteById(coordinatorId);

        // Verificação
        verify(coordinatorRepository).findById(coordinatorId);
        verify(coordinatorRepository).delete(existingCoordinator);
    }

    @Test
    void testDeleteCoordinator_NonExistingCoordinator() {
        // Dados de teste
        Long coordinatorId = 1L;

        User user_1 = new User("First", "Second", "first.second@mail.com", "first.second");
        user_1.setId(1L);
        Coordinator existingCoordinator = new Coordinator(user_1);
        existingCoordinator.setId(coordinatorId);

        // Mock do repositório para retornar Optional vazio
        when(coordinatorRepository.findById(coordinatorId)).thenReturn(java.util.Optional.empty());

        // Verificação de exceção quando a sala de aula não é encontrada
        assertThrows(ResponseStatusException.class, () -> coordinatorService.deleteById(coordinatorId));

        // Verificação de chamada de método
        verify(coordinatorRepository).findById(coordinatorId);
        verify(coordinatorRepository, never()).save(any(Coordinator.class));
        verify(coordinatorRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testMapToResponseCoordinator() {
        // Mock Coordinator object
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        user.setId(10L);
        Coordinator coordinator = new Coordinator(user);
        coordinator.setId(1L);

        ResponseUserDTO responseUserDTO = new ResponseUserDTO(10L, "First", "Second", "first.second@mail.com");

        //when(userService.mapToResponseUser(any(User.class))).thenReturn(responseUserDTO);

        ResponseCoordinatorDTO responseCoordinatorDTO = coordinatorService.mapToResponseCoordinator(coordinator);

        assertNotNull(responseCoordinatorDTO);
        assertEquals(1L, responseCoordinatorDTO.getId());
        //assertEquals(10L, responseCoordinatorDTO.getUser().getId());

        //verify(userService, times(1)).mapToResponseUser(any(User.class));
    }










    /*
    @MockBean
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @MockBean
    private CoordinatorRepository coordinatorRepository;
    @InjectMocks
    private CoordinatorService coordinatorService;

    @MockBean
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        userRepository = createUserRepository();
        coordinatorRepository = createCoordinatorRepository();
        roleRepository = createRoleRepository();

        userService = new UserService(userRepository, new PasswordEncoder());
        roleService = new RoleService(roleRepository);
        coordinatorService = new CoordinatorService(coordinatorRepository, userService, new PasswordEncoder(), roleService);
        reset(userRepository);
        reset(roleRepository);
        reset(coordinatorRepository);
    }

    @AfterEach
    void tearDown() {
        reset(userRepository);
        reset(roleRepository);
        reset(coordinatorRepository);
    }

//    @Test
//    void saveCoordinatorSuccess(){
//        when(coordinatorRepository.save(any(Coordinator.class))).thenReturn(COORDINATOR_1);
//        Optional<Role> foundRole = Optional.of(new Role("ROLE_COORDINATOR"));
//        when(roleRepository.findByName(anyString())).thenReturn(foundRole);
//        when(roleService.findRoleByName("ROLE_COORDINATOR")).thenReturn(new Role("ROLE_COORDINATOR"));
//
//        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO(
//                new CreateUserDTO(
//                        COORDINATOR_1.getUser().getFirstName(),
//                        COORDINATOR_1.getUser().getLastName(),
//                        COORDINATOR_1.getUser().getEmail(),
//                        COORDINATOR_1.getUser().getPassword()
//                )
//        );
//
//        ResponseCoordinatorDTO newCoordinator = coordinatorService.save(coordinatorDTO);
//
//        assertEquals("teste@mail.com", newCoordinator.getUser().getEmail());
//    }

    @Test
    void saveCoordinatorFailDuplicated(){
        when(coordinatorRepository.findByEmail(anyString())).thenReturn(Optional.of(COORDINATOR_1)).thenThrow(ResponseStatusException.class);

        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO(
                new CreateUserDTO(
                        COORDINATOR_1.getUser().getFirstName(),
                        COORDINATOR_1.getUser().getLastName(),
                        COORDINATOR_1.getUser().getEmail(),
                        COORDINATOR_1.getUser().getPassword()
                )
        );

        ResponseCoordinatorDTO response = coordinatorService.save(coordinatorDTO);
        assertThrows()
                assertThrows(ResponseStatusException.class,
                () -> {
                    coordinatorService.save(coordinatorDTO);
                },
                "user already exists"
        );

        assertEquals(400, response.getStatusCode().value());
        assertEquals("user already exists", response.getReason());
    }


    private UserRepository createUserRepository(){
        UserRepository mock = mock(UserRepository.class);
        when(mock.findByEmail("teste@mail.com")).thenReturn(Optional.of(USER_4));
        when(mock.findByEmail("teste2@mail.com")).thenReturn(Optional.of(USER_5));
        return mock;
    }

    private CoordinatorRepository createCoordinatorRepository(){
        CoordinatorRepository mock = mock(CoordinatorRepository.class);
        when(mock.findByEmail("teste@mail.com")).thenReturn(Optional.of(COORDINATOR_1));
        when(mock.findByEmail("teste2@mail.com")).thenReturn(Optional.of(COORDINATOR_2));
        return mock;
    }

    private RoleRepository createRoleRepository() {
        RoleRepository mock = mock(RoleRepository.class);
        when(mock.findByName("ROLE_COORDINATOR")).thenReturn(Optional.of(ROLE_1));
        when(mock.findByName("ROLE_NOT_EXISTS")).thenReturn(Optional.of(ROLE_1)).thenThrow(ResponseStatusException.class);
        return mock;
    }


     */
}
