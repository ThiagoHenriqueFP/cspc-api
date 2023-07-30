package uol.compass.cspcapi.application.api.instructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.domain.instructor.InstructorService;

import java.util.List;

@RestController
@RequestMapping("/instructors")
public class InstructorController {
    private InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public ResponseEntity<ResponseInstructorDTO> createInstructor(@RequestBody CreateInstructorDTO instructor) {
        return new ResponseEntity<>(
                instructorService.save(instructor),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseInstructorDTO> getInstructorById(@PathVariable Long id) {
        return new ResponseEntity<>(
                instructorService.getById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseInstructorDTO>> getAllInstructors() {
        return new ResponseEntity<>(
                instructorService.getAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseInstructorDTO> updateInstructor(
            @PathVariable Long id,
            @RequestBody UpdateInstructorDTO instructorDTO
    ) {
        return new ResponseEntity<>(
                instructorService.update(id, instructorDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructorById(@PathVariable Long id) {
        instructorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
