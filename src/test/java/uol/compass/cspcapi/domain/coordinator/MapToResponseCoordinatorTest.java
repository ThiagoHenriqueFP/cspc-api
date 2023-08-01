package uol.compass.cspcapi.domain.coordinator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapToResponseCoordinatorTest {
    @Mock
    private CoordinatorRepository coordinatorRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private CoordinatorService coordinatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMapToResponseCoordinator() {
        // Mock Coordinator and User objects
        Coordinator coordinator = new Coordinator();
        coordinator.setId(1L);
        // Set coordinator's user to null
        coordinator.setUser(null);

        // Call the method to be tested
        ResponseCoordinatorDTO responseCoordinatorDTO = coordinatorService.mapToResponseCoordinator(coordinator);

        // Verify that userService.mapToResponseUser() was not called
        verify(userService, never()).mapToResponseUser(any());

        // Assertions
        assertEquals(coordinator.getId(), responseCoordinatorDTO.getId());
        assertNull(responseCoordinatorDTO.getUser());
    }

//    @Test
//    public void testMapToResponseCoordinator() {
//        // Mock Coordinator and User objects
//        Coordinator coordinator = new Coordinator();
//        coordinator.setId(1L);
//        User user = new User("First", "Second", "first.second@mail.com", "first.second");
//        user.setId(100L);
//        coordinator.setUser(user);
//
//        // Mock the behavior of userService.mapToResponseUser()
//        ResponseUserDTO responseUserDTO = new ResponseUserDTO(user.getId(), "John", "Doe", "john@example.com");
//        when(userService.mapToResponseUser(coordinator.getUser())).thenReturn(responseUserDTO);
//
//        // Call the method to be tested
//        ResponseCoordinatorDTO responseCoordinatorDTO = coordinatorService.mapToResponseCoordinator(coordinator);
//
//        // Verify that userService.mapToResponseUser() was called with the correct argument
//        verify(userService).mapToResponseUser(coordinator.getUser());
//
//        // Assertions
//        assertEquals(coordinator.getId(), responseCoordinatorDTO.getId());
//        assertEquals(responseUserDTO, responseCoordinatorDTO.getUser());
//    }

}
