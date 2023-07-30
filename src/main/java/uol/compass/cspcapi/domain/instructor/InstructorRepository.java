package uol.compass.cspcapi.domain.instructor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    List<Instructor> findAllByIdIn(List<Long> instructorsIds);
}
