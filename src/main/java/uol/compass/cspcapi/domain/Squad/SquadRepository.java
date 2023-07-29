package uol.compass.cspcapi.domain.Squad;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SquadRepository extends JpaRepository<Squad, Long> {
    Optional<Squad> findByName(String squadName);

    List<Squad> findAllByIdIn(List<Long> squadsIds);
}
