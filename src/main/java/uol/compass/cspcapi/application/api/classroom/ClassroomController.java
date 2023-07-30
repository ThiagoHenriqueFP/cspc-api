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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseClassroomDTO> getClassroomById(@PathVariable Long id) {
        return new ResponseEntity<>(classroomService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseClassroomDTO>> getAllClassrooms() {
        return new ResponseEntity<>(classroomService.getAllClassrooms(), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseClassroomDTO> updateClassroom(@PathVariable Long id, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(classroomService.updateClassroom(id, classroomDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{classroomId}")
    public ResponseEntity<Void> delete(@PathVariable Long classroomId) {
        classroomService.deleteClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }

    //Insert user (Students, Scrum Master, Instructor), in classroom
    @PatchMapping("/{classroomId}/add-students")
    public ResponseEntity<ResponseClassroomDTO> addStudentsToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.addStudentsToClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{classroomId}/add-scrummasters")
    public ResponseEntity<ResponseClassroomDTO> addScumMastersToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.addScrumMastersToClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{classroomId}/add-instructors")
    public ResponseEntity<ResponseClassroomDTO> addInstructorsToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.addInstructorsToClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{classroomId}/add-squads")
    public ResponseEntity<ResponseClassroomDTO> addSquadsToClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.addSquadsToClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }

    //Insert user (Students, Scrum Master, Instructor), in classroom
    @PatchMapping("/{classroomId}/remove-students")
    public ResponseEntity<ResponseClassroomDTO> removeStudentsFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.removeStudentsFromClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{classroomId}/remove-scrummasters")
    public ResponseEntity<ResponseClassroomDTO> removeScrumMastersFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.removeScrumMastersFromClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{classroomId}/remove-instructors")
    public ResponseEntity<ResponseClassroomDTO> removeInstructorsFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.removeInstructorsFromClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{classroomId}/remove-squads")
    public ResponseEntity<ResponseClassroomDTO> removeSquadsFromClassroom(@PathVariable Long classroomId, @RequestBody UpdateClassroomDTO classroomDTO) {
        return new ResponseEntity<>(
                classroomService.removeSquadsFromClassroom(classroomId, classroomDTO),
                HttpStatus.OK
        );
    }
}
