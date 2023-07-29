package uol.compass.cspcapi.application.api.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.classroom.Classrooms;

import java.util.List;

@RestController
@RequestMapping("/classrooms")
public class ClassroomController {


    private ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    //Create Classroom
    @PostMapping
    public ResponseEntity<ResponseClassroomDTO> createClassroom(@RequestBody CreateClassroomDTO classroomDTO) {
        Long coordinatorId = classroomDTO.getCoordinatorId();
        return new ResponseEntity<>(
                classroomService.saveClassroom(classroomDTO, coordinatorId),
                HttpStatus.CREATED
        );
    }


    //Insert user (Students, Scrum Master, Instructor), in classroom
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

    @PostMapping("/{classroomId}/add-instructor")
    public ResponseEntity<Classrooms> addInstructorToClassroom(@PathVariable Long classroomId, @RequestBody List<Long> instructorId) {
        Classrooms updatedClassroom = classroomService.addInstructorToClassroom(classroomId, instructorId);
        return ResponseEntity.ok(updatedClassroom);
    }

    //Insert user (Students, Scrum Master, Instructor), in classroom
    @DeleteMapping("/{classroomId}/removeStudent")
    public ResponseEntity<Classrooms> removeStudentFromClassroom(@PathVariable Long classroomId, @RequestBody Long studentId) {
        Classrooms classroom = classroomService.removeScrumMasterFromClassroom(classroomId, studentId);
        return ResponseEntity.ok(classroom);
    }

    @DeleteMapping("/{classroomId}/removeScrumMaster")
    public ResponseEntity<Classrooms> removeScrumMasterFromClassroom(@PathVariable Long classroomId, @RequestBody Long scrumMasterId) {
        Classrooms classroom = classroomService.removeScrumMasterFromClassroom(classroomId, scrumMasterId);
        return ResponseEntity.ok(classroom);
    }

    @DeleteMapping("/{classroomId}/removeInstructor")
    public ResponseEntity<Classrooms> removeInstructorFromClassroom(@PathVariable Long classroomId, @RequestBody Long instructorId) {
        Classrooms classroom = classroomService.removeInstructorFromClassroom(classroomId, instructorId);
        return ResponseEntity.ok(classroom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classrooms> getClassroomById(@PathVariable Long id) {
        return new ResponseEntity<>(
                classroomService.getById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<Classrooms>> list() {
        return classroomService.listClassroom();
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseClassroomDTO> updateClassroom(@PathVariable Long id, @RequestBody UpdateClassroomDTO classroomDTO)
    {
        return new ResponseEntity<>(
                classroomService.updateClassroom(id, classroomDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{classroomId}")
    public ResponseEntity<Void> delete(@PathVariable Long classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{classroomId}/add-squad")
    public ResponseEntity<Classrooms> addSquadToClassroom(@PathVariable Long classroomId, @RequestBody CreateClassroomDTO classroomDTO) {
        Classrooms classroom = classroomService.addSquadToClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(classroom);
    }

}
