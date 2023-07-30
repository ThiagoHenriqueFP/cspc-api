package uol.compass.cspcapi.application.api.coordinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.UpdateCoordinatorDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;

import java.util.List;

@RestController
@RequestMapping("/coordinators")
public class CoordinatorController {
    private CoordinatorService coordinatorService;

    @Autowired
    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @PostMapping
    public ResponseEntity<ResponseCoordinatorDTO> createCoordinator(@RequestBody CreateCoordinatorDTO coordinator) {
        return new ResponseEntity<>(
                coordinatorService.save(coordinator),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCoordinatorDTO> getCoordinatorById(@PathVariable Long id) {
        return new ResponseEntity<>(
                coordinatorService.getById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<ResponseCoordinatorDTO>> getAllCoordinators() {
        return new ResponseEntity<>(
                coordinatorService.getAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCoordinatorDTO> updateCoordinator(
            @PathVariable Long id,
            @RequestBody UpdateCoordinatorDTO coordinatorDTO
    ) {
        return new ResponseEntity<>(
                coordinatorService.update(id, coordinatorDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinatorById(@PathVariable Long id) {
        coordinatorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
