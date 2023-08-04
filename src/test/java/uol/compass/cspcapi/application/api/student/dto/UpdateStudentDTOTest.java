package uol.compass.cspcapi.application.api.student.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.Classroom;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UpdateStudentDTOTest {
    @Test
    public void testGetGrades() {
        UpdateGradeDTO grades = Mockito.mock(UpdateGradeDTO.class);
        UpdateStudentDTO student = new UpdateStudentDTO(grades);

        assertEquals(grades, student.getGrades());
    }

    @Test
    public void testGetUser() {
        UpdateUserDTO user = Mockito.mock(UpdateUserDTO.class);
        UpdateStudentDTO student = new UpdateStudentDTO(user);

        assertEquals(user, student.getUser());
    }

    @Test
    public void testGetSquad() {
        Squad squad = Mockito.mock(Squad.class);
        UpdateStudentDTO student = new UpdateStudentDTO(squad);

        assertEquals(squad, student.getSquad());
    }

    @Test
    public void testGetClassroom() {
        Classroom classroom = Mockito.mock(Classroom.class);
        UpdateStudentDTO student = new UpdateStudentDTO(classroom);

        assertEquals(classroom, student.getClassroom());
    }

    @Test
    public void testSetUser() {
        UpdateUserDTO user = Mockito.mock(UpdateUserDTO.class);
        UpdateStudentDTO student = new UpdateStudentDTO();

        assertNull(student.getUser());

        student.setUser(user);

        assertEquals(user, student.getUser());
    }

//    @Test
//    public void testNotBlankValidation() {
//        UpdateUserDTO user = new UpdateUserDTO(); // Assuming the object is created with no properties set
//        UpdateStudentDTO student = new UpdateStudentDTO(user);
//
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//
//        Set<ConstraintViolation<UpdateStudentDTO>> violations = validator.validate(student);
//        assertEquals(1, violations.size());
//
//        ConstraintViolation<UpdateStudentDTO> violation = violations.iterator().next();
//        assertEquals("user must not be empty", violation.getMessage());
//    }
}
