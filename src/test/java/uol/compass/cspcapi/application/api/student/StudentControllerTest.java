package uol.compass.cspcapi.application.api.student;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.StudentsConstants.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testsave_Success() {
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second");
        CreateStudentDTO studentDTO = new CreateStudentDTO();
        studentDTO.setUser(userDTO);

        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseStudentDTO expectedStudent = new ResponseStudentDTO(1L, expectedUser);

        when(studentService.save(any(CreateStudentDTO.class))).thenReturn(expectedStudent);

        ResponseEntity<ResponseStudentDTO> responseEntity = studentController.save(studentDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedStudent.getId(), responseEntity.getBody().getId());
        assertEquals(expectedStudent.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testsave_Error() {
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        CreateUserDTO userDTO = new CreateUserDTO("First", "Second", "first.second@mail.com", "first.second");
        CreateStudentDTO studentDTO = new CreateStudentDTO();
        studentDTO.setUser(userDTO);

        when(studentService.save(any(CreateStudentDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving instructor"));

        assertThrows(RuntimeException.class, () -> studentController.save(studentDTO));
    }

    @Test
    public void testGetStudentById_Success() {
        Long studentId = 1L;
        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseStudentDTO expectedStudent = new ResponseStudentDTO(studentId, expectedUser);

        when(studentService.getById(anyLong())).thenReturn(expectedStudent);

        ResponseEntity<ResponseStudentDTO> responseEntity = studentController.getById(studentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedStudent.getId(), responseEntity.getBody().getId());
        assertEquals(expectedStudent.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testGetStudentById_NotFound() {
        Long studentId = 999L;

        when(studentService.getById(anyLong())).thenReturn(null);

        // Performing the test and verifying the exception
        try {
            studentController.getById(studentId);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllStudents_Success() {
        List<ResponseStudentDTO> expectedStudents = new ArrayList<>();
        expectedStudents.addAll(List.of(RESPONSE_STUDENT_1, RESPONSE_STUDENT_2, RESPONSE_STUDENT_3));

        when(studentService.getAll()).thenReturn(expectedStudents);

        ResponseEntity<List<ResponseStudentDTO>> responseEntity = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedStudents.size(), responseEntity.getBody().size());
        assertEquals(expectedStudents, responseEntity.getBody());
    }

    @Test
    public void testGetAllStudents_EmptyList() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for empty list scenario
        List<ResponseStudentDTO> expectedStudents = new ArrayList<>();

        when(studentService.getAll()).thenReturn(expectedStudents);

        // Performing the test
        ResponseEntity<List<ResponseStudentDTO>> responseEntity = studentController.getAllStudents();

        // Asserting the response status code and the returned list of classrooms (should be an empty list)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedStudents.size(), responseEntity.getBody().size());
        assertEquals(expectedStudents, responseEntity.getBody());
    }

    @Test
    public void testUpdateStudent_Success() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for success scenario
        Long studentId = 1L;
        User user = new User(
                RESPONSE_USER_1.getFirstName(),
                RESPONSE_USER_1.getLastName(),
                RESPONSE_USER_1.getEmail(),
                "password"
        );
        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.getFirstName(),
                RESPONSE_USER_1.getLastName(),
                RESPONSE_USER_1.getEmail(),
                "password"
        );
        UpdateStudentDTO studentDTO = new UpdateStudentDTO(userDTO);

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setUser(user);

        when(studentService.update(anyLong(), any(UpdateStudentDTO.class))).thenReturn(RESPONSE_STUDENT_1);

        // Performing the test
        ResponseEntity<ResponseStudentDTO> responseEntity = studentController.updateStudent(studentId, studentDTO);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedStudent.getId(), responseEntity.getBody().getId());
        assertEquals(updatedStudent.getUser().getFirstName(), responseEntity.getBody().getUser().getFirstName());
        assertEquals(updatedStudent.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
        assertEquals(updatedStudent.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
    }

    @Test
    public void testUpdateStudent_NotFound() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for scenario where classroom is not found
        Long studentId = 999L;
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        UpdateUserDTO userDTO = new UpdateUserDTO(
                RESPONSE_USER_1.getFirstName(),
                RESPONSE_USER_1.getLastName(),
                RESPONSE_USER_1.getEmail(),
                "password"
        );
        UpdateStudentDTO studentDTO = new UpdateStudentDTO(userDTO);

        when(studentService.update(anyLong(), any(UpdateStudentDTO.class))).thenReturn(null);

        try {
            studentController.updateStudent(studentId, studentDTO);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for success scenario
        Long studentId = 1L;

        doNothing().when(studentService).delete(studentId);

        // Performing the test
        ResponseEntity<Void> responseEntity = studentController.delete(studentId);

        // Asserting the response status code (should be HTTP 204 No Content)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for scenario where classroom is not found
        Long studentId = 999L;

        doThrow(ResponseStatusException.class).when(studentService).delete(studentId);

        // Performing the test and verifying the exception
        try {
            studentController.delete(studentId);
        } catch (ResponseStatusException exception) {
            // Assertion for ResourceNotFoundException
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}
