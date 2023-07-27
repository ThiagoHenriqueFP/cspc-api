package uol.compass.cspcapi.application.api.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.classroom.Classrooms;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {


    private ClassroomService classroomService;
    private ScrumMasterService scrumMasterService;

    private UserService userService;

    @Autowired
    public ClassroomController(ClassroomService classroomService, ScrumMasterService scrumMasterService, UserService userService) {
        this.classroomService = classroomService;
        this.scrumMasterService = scrumMasterService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Classrooms> createClassroom(@RequestBody CreateClassroomDTO classroomDTO) {
        Long coordinatorId = classroomDTO.getCoordinator();
        ResponseEntity<Classrooms> responseEntity = classroomService.saveClassroom(classroomDTO, coordinatorId);
        return responseEntity;
    }

    @PostMapping("/{classroomId}/add-students")
    public ResponseEntity<Classrooms> addStudentsToClassroom(@PathVariable Long classroomId, @RequestBody List<Long> studentIds) {
        Classrooms updatedClassroom = classroomService.addStudentsToClassroom(classroomId, studentIds);
        return ResponseEntity.ok(updatedClassroom);
    }

    @PostMapping("/{classroomId}/add-scrumMaster")
    public ResponseEntity<Classrooms> addScumMasterToClassroom(@PathVariable Long classroomId, @RequestBody List<Long> scrumMasterIds) {
        Classrooms updatedClassroom = classroomService.addScrumMastersToClassroom(classroomId, scrumMasterIds);
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
