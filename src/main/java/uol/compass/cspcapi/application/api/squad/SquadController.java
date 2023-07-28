package uol.compass.cspcapi.application.api.squad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;

import java.util.List;

@RestController
@RequestMapping("/squads")
public class SquadController {

    private SquadService squadService;

    @Autowired
    public SquadController(SquadService squadService) {
        this.squadService = squadService;
    }

    @PostMapping
    public ResponseEntity<ResponseSquadDTO> createSquad(@RequestBody CreateSquadDTO squad) {
        return new ResponseEntity<>(
                squadService.save(squad),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}/addStudents")
    public ResponseEntity<Squad> addStudentsToSquad(@PathVariable Long id, @RequestBody UpdateSquadDTO studentsIds) {
        Squad squad = squadService.addStudentsToSquad(id, studentsIds);
        return ResponseEntity.ok(squad);
    }

    @PatchMapping("/{id}/removeStudents")
    public ResponseEntity<Squad> removeStudentsFromSquad(@PathVariable Long id, @RequestBody UpdateSquadDTO studentsIds) {
        Squad squad = squadService.removeStudentsFromSquad(id, studentsIds);
        return ResponseEntity.ok(squad);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Squad> getSquadById(@PathVariable Long id) {
        Squad squad = squadService.getById(id);
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
    public ResponseEntity<Boolean> removeSquadById(@PathVariable Long id) {
        return new ResponseEntity<>(
                squadService.delete(id),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseSquadDTO> updateSquad(@PathVariable Long id, @RequestBody UpdateSquadDTO squadDTO)
    {
        return new ResponseEntity<>(
                squadService.updateSquad(id, squadDTO),
                HttpStatus.OK
        );
    }



}
