package uol.compass.cspcapi.application.api.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.classroom.Classrooms;
import uol.compass.cspcapi.domain.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassroomController {


    private ClassroomService classroomService;

    private UserService userService;

    @Autowired
    public ClassroomController(ClassroomService classroomService, UserService userService) {
        this.classroomService = classroomService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Classrooms> createClassroom(@RequestBody CreateClassroomDTO classroomDTO) {
        Long coordinatorId = 1L;

        Classrooms classroom = classroomService.saveClassroom(classroomDTO, coordinatorId);

        return ResponseEntity.ok(classroom);
    }

    @PostMapping("/{classroomId}/add-students")
    public ResponseEntity<Classrooms> addStudentsToClassroom(@PathVariable Long classroomId, @RequestBody List<Long> studentIds) {
        Classrooms classroom = classroomService.addStudentsToClassroom(classroomId, studentIds);
        return ResponseEntity.ok(classroom);
    }

    @GetMapping
    public List<Classrooms> list(){
        return classroomService.listClassroom();
    }

    @DeleteMapping
    public Classrooms delete(@PathVariable Classrooms classrooms){
        return classroomService.deleteClassromm(classrooms);
    }

}
