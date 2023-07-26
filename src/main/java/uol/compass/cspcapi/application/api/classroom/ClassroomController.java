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
        //Long coordinatorId = userService.getAuthenticatedUserId();
        Long coordinatorId = classroomDTO.getCoordinator();
        //todo veri already exists
        ResponseEntity<Classrooms> responseEntity = classroomService.saveClassroom(classroomDTO, coordinatorId);
        return responseEntity;
    }

    @PostMapping("/{classroomId}/add-students")
    public ResponseEntity<Classrooms> addStudentsToClassroom(@PathVariable Long classroomId, @RequestBody List<Long> studentIds) {
        Classrooms updatedClassroom = classroomService.addStudentsToClassroom(classroomId, studentIds);
        return ResponseEntity.ok(updatedClassroom);
    }

    @GetMapping
    public ResponseEntity<List<Classrooms>> list() {
        return classroomService.listClassroom();
    }

    @DeleteMapping("/{classroomId}")
    public ResponseEntity<Void> delete(@PathVariable Long classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }

}
