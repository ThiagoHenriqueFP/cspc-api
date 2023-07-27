package uol.compass.cspcapi.application.api.squad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;

@RestController
@RequestMapping("/squad")
public class SquadController {

    private SquadService squadService;

    @Autowired
    public SquadController(SquadService squadService) {
        this.squadService = squadService;
    }

    @PostMapping("/{squadId}/addStudent")
    public ResponseEntity<Squad> addStudentToSquad(@PathVariable Long squadId, @RequestParam Long studentId) {
        Squad squad = squadService.addStudentToSquad(squadId, studentId);
        return ResponseEntity.ok(squad);
    }

}
