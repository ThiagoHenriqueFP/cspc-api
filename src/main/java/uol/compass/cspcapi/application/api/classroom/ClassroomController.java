package uol.compass.cspcapi.application.api.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.classroom.Classrooms;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassroomController {


    private ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping
    public Classrooms save(@RequestBody CreateClassroomDTO classBody){
        return classroomService.saveClassroom(classBody);
    }

    @PostMapping("/{classroomId}/add-student/{studentId}")
    public ResponseEntity<Classrooms> addStudentToClassroom(@PathVariable Long classroomId, @PathVariable Long studentId){
        Classrooms updateClassroom = classroomService.addStudentToClass(classroomId, studentId);
        return ResponseEntity.ok(updateClassroom);
    }

    @PostMapping("/{classroomId}/add-students")
    public ResponseEntity<Classrooms> addManyStudentsToClassroom(@PathVariable Long classroomId, @RequestBody List<Long> studentIds) {
        Classrooms updatedClassroom = classroomService.addManyStudentsToClassroom(classroomId, studentIds);
        return ResponseEntity.ok(updatedClassroom);
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
