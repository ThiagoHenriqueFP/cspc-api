package uol.compass.cspcapi.domain.classroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByTitle(String classroomTitle);

}
