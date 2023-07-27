package uol.compass.cspcapi.domain.Squad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SquadService {

    private SquadRepository squadRepository;

    @Autowired
    public SquadService(SquadRepository squadRepository) {
        this.squadRepository = squadRepository;
    }
}
