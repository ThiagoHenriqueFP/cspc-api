package uol.compass.cspcapi.domain.coordinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CoordinatorService {

    private CoordinatorRepository coordinatorRepository;

    @Autowired
    public CoordinatorService(CoordinatorRepository coordinatorRepository) {
        this.coordinatorRepository = coordinatorRepository;
    }

    public Coordinator getCoordinatorById(Long coordinatorId) {
        return coordinatorRepository.findById(coordinatorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "coordinator not found"));
    }
}
