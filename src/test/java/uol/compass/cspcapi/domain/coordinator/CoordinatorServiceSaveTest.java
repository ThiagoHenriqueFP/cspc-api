package uol.compass.cspcapi.domain.coordinator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class CoordinatorServiceSaveTest {
    private static CoordinatorRepository coordinatorRepository;
    private static UserRepository userRepository;
    private static RoleRepository roleRepository;
    private static UserService userService;
    private static RoleService roleService;
    private static CoordinatorService coordinatorService;
    private static UserService mockUserService;
    private static CoordinatorService mockCoordinatorService;

    @BeforeAll()
    public static void setUp(){
        userRepository = mock(UserRepository.class);
        coordinatorRepository = mock(CoordinatorRepository.class);
        roleRepository = mock(RoleRepository.class);
        mockUserService = mock(UserService.class);
        mockCoordinatorService = mock(CoordinatorService.class);

        userService = new UserService(userRepository, new PasswordEncoder());
        roleService = new RoleService(roleRepository);
//        coordinatorService = new coordinatorService(instructorRepository, userService, new PasswordEncoder(), roleService);
        coordinatorService = new CoordinatorService(coordinatorRepository, mockUserService, new PasswordEncoder(), roleService);
    }

    @AfterEach()
    void down(){
        reset(userRepository);
        reset(roleRepository);
        reset(coordinatorRepository);
        reset(mockCoordinatorService);
    }

    @Test
    public void testSave_Success() {
        User user = new User("John", "Doe", "johndoe@compass.com", "password");
        Coordinator coordinator = new Coordinator(user);
        CreateUserDTO userDTO = new CreateUserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO(userDTO);

        coordinator.setId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(coordinatorRepository.save(any(Coordinator.class))).thenReturn(coordinator);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role("ROLE_INSTRUCTOR")));

        ResponseCoordinatorDTO result = coordinatorService.save(coordinatorDTO);

        assertEquals(coordinator.getId(), result.getId());
        assertEquals(coordinator.getUser().getFirstName(), result.getUser().getFirstName());
        assertEquals(coordinator.getUser().getLastName(), result.getUser().getLastName());
        assertEquals(coordinator.getUser().getEmail(), result.getUser().getEmail());
    }

    @Test
    public void testSave_Failure() {
        CreateCoordinatorDTO coordinatorDTO = new CreateCoordinatorDTO();
        CreateUserDTO userDTO = new CreateUserDTO("John", "Doe", "johndoe@compass.com", "password");
        coordinatorDTO.setUser(userDTO);

        User existingUser = new User("Jane", "Smith", "johndoe@compass.com", "hashed_password");

        when(userService.findByEmail("johndoe@compass.com")).thenReturn(Optional.of(existingUser));

        assertThrows(ResponseStatusException.class, () -> coordinatorService.save(coordinatorDTO));
    }

}
