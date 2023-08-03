package uol.compass.cspcapi.domain.role;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role("ADMIN");
    }

    @Test
    void testGetName() {
        // Arrange
        String expectedName = "ADMIN";

        // Act
        String roleName = role.getName();

        // Assert
        assertEquals(expectedName, roleName);
    }

    @Test
    void testSetName() {
        // Arrange
        String newName = "MODERATOR";

        // Act
        role.setName(newName);

        // Assert
        assertEquals(newName, role.getName());
    }

    @Test
    void testGetId() {
        // Arrange
        Long expectedId = 1L;
        role.setId(expectedId);

        // Act
        Long roleId = role.getId();

        // Assert
        assertEquals(expectedId, roleId);
    }

    @Test
    void testSetId() {
        // Arrange
        Long newId = 2L;

        // Act
        role.setId(newId);

        // Assert
        assertEquals(newId, role.getId());
    }
}
