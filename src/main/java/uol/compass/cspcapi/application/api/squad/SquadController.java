package uol.compass.cspcapi.application.api.squad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.squad.dto.RespondeSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;

import java.util.List;

@RestController
@RequestMapping("/squad")
public class SquadController {

    private SquadService squadService;

    @Autowired
    public SquadController(SquadService squadService) {
        this.squadService = squadService;
    }

    @PostMapping("/{id}/addStudent")
    public ResponseEntity<Squad> addStudentToSquad(@PathVariable Long squadId, @RequestBody Long studentId) {
        Squad squad = squadService.addStudentToSquad(squadId, studentId);
        return ResponseEntity.ok(squad);
    }

    @DeleteMapping("/{squadId}/removeStudent/{studentId}")
    public ResponseEntity<Squad> removeStudentFromSquad(@PathVariable Long id, @RequestBody Long studentId) {
        Squad squad = squadService.removeStudentFromSquad(id, studentId);
        return ResponseEntity.ok(squad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Squad> getSquadById(@PathVariable Long squadId) {
        Squad squad = squadService.getById(squadId);
        return ResponseEntity.ok(squad);
    }

    @GetMapping
    public ResponseEntity<List<Squad>> getAllSquads(){
        return new ResponseEntity<>(
                squadService.getAll(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> removeSquadById(@PathVariable Long squadId) {
        return new ResponseEntity<>(
                squadService.delete(squadId),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RespondeSquadDTO> updateSquad(@PathVariable Long id, @RequestBody UpdateSquadDTO squadDTO)
    {
        return new ResponseEntity<>(
                squadService.updateSquad(id, squadDTO),
                HttpStatus.OK
        );
    }



}
