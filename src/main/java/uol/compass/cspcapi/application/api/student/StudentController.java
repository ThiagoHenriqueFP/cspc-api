package uol.compass.cspcapi.application.api.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ResponseStudentDTO> save(
            @RequestBody CreateStudentDTO student
    ){
        return new ResponseEntity<>(
                studentService.save(student),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStudentDTO> getById(@PathVariable Long id){
        return new ResponseEntity<>(
                studentService.getById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseStudentDTO>> getAllStudents(){
        return new ResponseEntity<>(
                studentService.getAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseStudentDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody UpdateStudentDTO studentDTO
    ) {
        return new ResponseEntity<>(
                studentService.update(id, studentDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        studentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{studentId}/update-grades")
    public ResponseEntity<ResponseStudentDTO> updateGradesFromStudent(@PathVariable Long studentId, @RequestBody UpdateStudentDTO studentDTO) {
        return new ResponseEntity<>(studentService.updateGradesFromStudent(studentId, studentDTO), HttpStatus.OK);
    }

}
