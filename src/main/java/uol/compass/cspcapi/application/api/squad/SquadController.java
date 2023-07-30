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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseSquadDTO> getSquadById(@PathVariable Long id) {
        return new ResponseEntity<>(
                squadService.getById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseSquadDTO>> getAllSquads(){
        return new ResponseEntity<>(squadService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseSquadDTO> updateSquad(@PathVariable Long id, @RequestBody UpdateSquadDTO squadDTO) {
        return new ResponseEntity<>(squadService.updateSquad(id, squadDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSquadById(@PathVariable Long id) {
        squadService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/add-students")
    public ResponseEntity<ResponseSquadDTO> addStudentsToSquad(@PathVariable Long id, @RequestBody UpdateSquadDTO studentsIds) {
        return new ResponseEntity<>(
                squadService.addStudentsToSquad(id, studentsIds),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/remove-students")
    public ResponseEntity<ResponseSquadDTO> removeStudentsFromSquad(@PathVariable Long id, @RequestBody UpdateSquadDTO studentsIds) {
        return new ResponseEntity<>(
                squadService.removeStudentsFromSquad(id, studentsIds),
                HttpStatus.OK
        );
    }
}
