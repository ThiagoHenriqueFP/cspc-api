package uol.compass.cspcapi.domain.Squad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;

import java.util.List;
import java.util.Optional;

@Service
public class SquadService {

    private SquadRepository squadRepository;

    private StudentService studentService;

    @Autowired
    public SquadService(SquadRepository squadRepository, StudentService studentService) {
        this.squadRepository = squadRepository;
        this.studentService = studentService;
    }

    public ResponseSquadDTO save(CreateSquadDTO squad) {
        Optional<Squad> alreadyExists = squadRepository.findByName(squad.getName());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Squad already exists"
            );
        }

        Squad newSquad = new Squad(
                squad.getName()
        );

        Squad squadDb = squadRepository.save(newSquad);
        return new ResponseSquadDTO(
                squadDb.getId(),
                squadDb.getName()
        );
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

    public ResponseSquadDTO updateSquad(Long id, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        //squad.setName(squad.getName());
        squad.setName(squadDTO.getName());

        Squad updatedSquad = squadRepository.save(squad);

        return mapToResponseDTO(updatedSquad);
    }

    //Possivel jogar o metodo em package UTILS
    private ResponseSquadDTO mapToResponseDTO(Squad squad) {
        ResponseSquadDTO responseDTO = new ResponseSquadDTO();
        responseDTO.setId(squad.getId());
        responseDTO.setName(squad.getName());

        return responseDTO;
    }

}
