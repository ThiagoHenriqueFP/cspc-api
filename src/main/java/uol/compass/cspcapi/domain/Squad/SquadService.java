package uol.compass.cspcapi.domain.Squad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.squad.dto.RespondeSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.domain.classroom.Classrooms;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;

import java.util.List;

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

    public Squad removeStudentFromSquad(Long squadId, Long studentId) {
        Squad squad = squadRepository.findById(squadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        squad.getStudents().removeIf(student -> student.getId().equals(studentId));

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

    public boolean delete(Long id){
        Squad squad = squadRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Squad not found"
                )
        );

        squadRepository.delete(squad);

        return true;
    }

    public List<Squad> getAll(){
        return squadRepository.findAll();
    }

    public RespondeSquadDTO updateSquad(Long id, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        squad.setName(squad.getName());

        Squad updatedSquad = squadRepository.save(squad);

        return mapToResponseDTO(updatedSquad);
    }

    //Possivel jogar o metodo em package UTILS
    private RespondeSquadDTO mapToResponseDTO(Squad squad) {
        RespondeSquadDTO responseDTO = new RespondeSquadDTO();
        responseDTO.setId(squad.getId());
        responseDTO.setName(squad.getName());

        return responseDTO;
    }

}
