package uol.compass.cspcapi.application.api.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentRepository;
import uol.compass.cspcapi.domain.student.StudentService;
import uol.compass.cspcapi.domain.user.UserService;

@RestController
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student save(
            @RequestBody Student student
    ){
        return studentService.save(student);
    }


}
