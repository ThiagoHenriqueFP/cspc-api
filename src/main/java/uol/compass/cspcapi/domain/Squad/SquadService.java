package uol.compass.cspcapi.domain.Squad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;

@Service
public class SquadService {

    private SquadRepository squadRepository;

    private StudentService studentService;

    @Autowired
    public SquadService(SquadRepository squadRepository, StudentService studentService) {
        this.squadRepository = squadRepository;
        this.studentService = studentService;
    }

    public Squad addStudentToSquad(Long squadId, Long studentId) {
        Squad squad = squadRepository.findById(squadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        Student student = studentService.getById(studentId);

        squad.getStudents().add(student);
        return squadRepository.save(squad);
    }

    public Squad getById(Long id){
        return squadRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public ResponseEntity<Long> deleteSquad(long squadId) {
        if (!squadRepository.existsById(squadId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "squad not found");
        }

        squadRepository.deleteById(squadId);
        return ResponseEntity.ok(squadId);
    }
}
