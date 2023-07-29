package uol.compass.cspcapi.application.api.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.classroom.Classroom;

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
    @PatchMapping("/{classroomId}/add-students")
    public ResponseEntity<Classroom> addStudentsToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.addStudentsToClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    @PatchMapping("/{classroomId}/add-scrummasters")
    public ResponseEntity<Classroom> addScumMastersToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.addScrumMastersToClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    @PatchMapping("/{classroomId}/add-instructors")
    public ResponseEntity<Classroom> addInstructorsToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.addInstructorsToClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    @PatchMapping("/{classroomId}/add-squads")
    public ResponseEntity<Classroom> addSquadsToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.addSquadsToClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    //Insert user (Students, Scrum Master, Instructor), in classroom
    @PatchMapping("/{classroomId}/remove-students")
    public ResponseEntity<Classroom> removeStudentsFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.removeStudentsFromClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    @PatchMapping("/{classroomId}/remove-scrummasters")
    public ResponseEntity<Classroom> removeScrumMastersFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.removeScrumMastersFromClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    @PatchMapping("/{classroomId}/remove-instructors")
    public ResponseEntity<Classroom> removeInstructorsFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.removeInstructorsFromClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    @PatchMapping("/{classroomId}/remove-squads")
    public ResponseEntity<Classroom> removeSquadsFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        Classroom updatedClassroom = classroomService.removeSquadsFromClassroom(classroomId, classroomDTO);
        return ResponseEntity.ok(updatedClassroom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable Long id) {
        return new ResponseEntity<>(
                classroomService.getById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<Classroom>> list() {
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

}
