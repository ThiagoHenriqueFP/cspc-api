package uol.compass.cspcapi.domain.Squad;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SquadRepository extends JpaRepository<Squad, Long> {
    Optional<Squad> findByName(String squadName);
}
