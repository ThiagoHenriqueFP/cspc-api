package uol.compass.cspcapi.domain.classroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classrooms, Long> {

    Optional<Classrooms> findById(Long id);

    Classrooms deleteById(Classrooms classrooms);
}
