package uol.compass.cspcapi.domain.Squad;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.ResponseSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
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

    @Transactional
    public Squad addStudentsToSquad(Long squadId, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(squadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        List<Student> students = squad.getStudents();
        List<Student> newStudents = studentService.getAllStudentsById(squadDTO.getStudentsIds());
        students.addAll(newStudents);

        studentService.attributeStudentsToSquad(squad, students);
        squad.setStudents(students);

        //squad.getStudents().add(student);

        return squadRepository.save(squad);
    }

    public Squad removeStudentsFromSquad(Long squadId, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(squadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        List<Student> students = squad.getStudents();

        students.removeIf(
                student -> squadDTO.getStudentsIds().contains(student.getId())
        );

        List<Student> toRemoveStudents = studentService.getAllStudentsById(squadDTO.getStudentsIds());
        studentService.attributeStudentsToSquad(null, toRemoveStudents);

        squad.setStudents(students);

        return squadRepository.save(squad);
    }

    public Squad getById(Long id){
        return squadRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Squad not found"
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

    public List<Squad> getAllSquadsById(List<Long> squadsIds) {
        return squadRepository.findAllByIdIn(squadsIds);
    }

    public List<Squad> attributeSquadsToClassroom(Classroom classroom, List<Squad> squads) {
        for (Squad squad : squads) {
            squad.setClassroom(classroom);
        }
        return squadRepository.saveAll(squads);
    }
}
