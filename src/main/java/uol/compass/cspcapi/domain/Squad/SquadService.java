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

import java.util.ArrayList;
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

    @Transactional
    public ResponseSquadDTO save(CreateSquadDTO squad) {
        Optional<Squad> alreadyExists = squadRepository.findByName(squad.getName());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Squad already exists"
            );
        }

        Squad newSquad = new Squad(squad.getName());
        Squad squadDb = squadRepository.save(newSquad);

        return mapToResponseSquad(squadDb);
    }


    public ResponseSquadDTO getById(Long id){
        return mapToResponseSquad(squadRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "squad not found"
                )
        ));
    }

    public List<ResponseSquadDTO> getAll(){
        List<Squad> squads = squadRepository.findAll();
        return mapToResponseSquads(squads);
    }

    @Transactional
    public ResponseSquadDTO updateSquad(Long id, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        squad.setName(squadDTO.getName());

        Squad updatedSquad = squadRepository.save(squad);

        return mapToResponseSquad(updatedSquad);
    }

    @Transactional
    public void delete(Long id){
        Squad squad = squadRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Squad not found"
                )
        );

        List<Student> toRemoveStudents = squad.getStudents();
        if (toRemoveStudents != null) {
            studentService.attributeStudentsToSquad(null, toRemoveStudents);

            toRemoveStudents.removeIf(student -> true);
            squad.setStudents(toRemoveStudents);
        }

        squadRepository.save(squad);
        squadRepository.delete(squad);
    }


    /*
    public void delete(Long id){
        Squad squad = squadRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Squad not found"
                )
        );

        List<Student> toRemoveStudents = squad.getStudents();
        studentService.attributeStudentsToSquad(null, toRemoveStudents);

        toRemoveStudents.removeIf(student -> true);
        squad.setStudents(toRemoveStudents);

        squadRepository.save(squad);
        squadRepository.delete(squad);
    }
    */

    @Transactional
    public ResponseSquadDTO addStudentsToSquad(Long squadId, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(squadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        // Cria uma cópia mutável da lista de estudantes do esquadrão
        List<Student> students = new ArrayList<>(squad.getStudents());

        List<Student> newStudents = studentService.getAllStudentsById(squadDTO.getStudentsIds());
        students.addAll(newStudents);

        studentService.attributeStudentsToSquad(squad, students);
        squad.setStudents(students);
        Squad updatedSquad = squadRepository.save(squad);

        return mapToResponseSquad(updatedSquad);
    }
    /*
 @Transactional
    public ResponseSquadDTO addStudentsToSquad(Long squadId, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(squadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        List<Student> students = squad.getStudents();
        List<Student> newStudents = studentService.getAllStudentsById(squadDTO.getStudentsIds());
        students.addAll(newStudents);

        studentService.attributeStudentsToSquad(squad, students);
        squad.setStudents(students);
        Squad updatedSquad = squadRepository.save(squad);

        return mapToResponseSquad(updatedSquad);
    }
*/


    @Transactional
    public ResponseSquadDTO removeStudentsFromSquad(Long squadId, UpdateSquadDTO squadDTO) {
        Squad squad = squadRepository.findById(squadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Squad not found"));

        List<Student> students = squad.getStudents();

        students.removeIf(
                student -> squadDTO.getStudentsIds().contains(student.getId())
        );

        List<Student> toRemoveStudents = studentService.getAllStudentsById(squadDTO.getStudentsIds());
        studentService.attributeStudentsToSquad(null, toRemoveStudents);

        squad.setStudents(students);
        Squad updatedSquad = squadRepository.save(squad);

        return mapToResponseSquad(updatedSquad);
    }

    public List<Squad> getAllSquadsById(List<Long> squadsIds) {
        List<Squad> squads = squadRepository.findAllByIdIn(squadsIds);

        if (squads.size() != squadsIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more squads not found");
        }

        return squads;
    }

    @Transactional
    public List<Squad> attributeSquadsToClassroom(Classroom classroom, List<Squad> squads) {
        for (Squad squad : squads) {
            squad.setClassroom(classroom);
        }

        List<Squad> updatedSquads;
        try {
            updatedSquads = squadRepository.saveAll(squads);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving squads");
        }

        return updatedSquads;
    }

    public ResponseSquadDTO mapToResponseSquad(Squad squad) {
        if (squad.getStudents() == null) {
            squad.setStudents(new ArrayList<>());
        }
        return new ResponseSquadDTO(
                squad.getId(),
                squad.getName(),
                studentService.mapToResponseStudents(squad.getStudents())
        );
    }

    public List<ResponseSquadDTO> mapToResponseSquads(List<Squad> squads) {
        List<ResponseSquadDTO> squadsNoPassword = new ArrayList<>();

        for (Squad squad : squads) {
            squadsNoPassword.add(mapToResponseSquad(squad));
        }
        return squadsNoPassword;
    }
}
