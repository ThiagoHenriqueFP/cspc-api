package uol.compass.cspcapi.domain.student;

import org.springframework.data.jpa.repository.JpaRepository;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByIdIn(List<Long> studentsIds);

    List<Student> findAllBySquadId(Long squadId);
}
