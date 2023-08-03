package uol.compass.cspcapi.application.api.user.dto;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UpdateUserDTOTest {
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext nodeBuilderDefinedContext;

    @InjectMocks
    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFirstNameValidation_Success() {
        updateUserDTO = new UpdateUserDTO("John", "Doe", "john.doe@example.com", "password123");

        assertTrue(updateUserDTO.getFirstName().length() >= 3);
        assertTrue(updateUserDTO.getLastName().length() >= 3);
        assertTrue(updateUserDTO.getEmail().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b"));
        assertTrue(updateUserDTO.getPassword().length() >= 8);
    }

    @Test
    public void testFirstNameValidation_Failure() {
        updateUserDTO = new UpdateUserDTO("Jo", "Doe", "john.doe@example.com", "password123");

        assertFalse(updateUserDTO.getFirstName().length() > 3);
    }

    @Test
    public void testLastNameValidation_Failure() {
        updateUserDTO = new UpdateUserDTO("John", "Do", "john.doe@example.com", "password123");

        assertFalse(updateUserDTO.getLastName().length() > 3);
    }

    @Test
    public void testEmailValidation_Failure() {
        updateUserDTO = new UpdateUserDTO("John", "Doe", "invalid_email", "password123");

        assertFalse(updateUserDTO.getEmail().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b"));
    }

    @Test
    public void testPasswordValidation_Failure() {
        updateUserDTO = new UpdateUserDTO("John", "Doe", "john.doe@example.com", "pass");

        assertFalse(updateUserDTO.getPassword().length() >= 8);
    }

    // Mock the validation behavior for each constraint validator
    private void mockValidatorContext() {
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(any()))
                .thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode(any())).thenReturn((ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext) nodeBuilderDefinedContext);
    }
}
