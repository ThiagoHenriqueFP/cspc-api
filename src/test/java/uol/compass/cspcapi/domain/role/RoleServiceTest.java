package uol.compass.cspcapi.domain.role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testFindRoleByName_ExistingRole() {
        // Arrange
        String roleName = "ADMIN";
        Role expectedRole = new Role(roleName);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(expectedRole));

        // Act
        Role resultRole = roleService.findRoleByName(roleName);

        // Assert
        assertEquals(expectedRole, resultRole);
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    void testFindRoleByName_NonExistingRole() {
        // Arrange
        String roleName = "NON_EXISTENT_ROLE";
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> roleService.findRoleByName(roleName));
        verify(roleRepository, times(1)).findByName(roleName);
    }
}
