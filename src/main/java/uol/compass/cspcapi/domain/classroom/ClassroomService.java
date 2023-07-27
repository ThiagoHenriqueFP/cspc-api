package uol.compass.cspcapi.domain.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterRepository;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentRepository;
import uol.compass.cspcapi.domain.student.StudentService;


import java.util.List;

@Service
public class ClassroomService {

    //repositories
    private ClassroomRepository classroomRepository;


    //services
    private CoordinatorService coordinatorService;
    private StudentService studentService;
    private ScrumMasterService scrumMasterService;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, CoordinatorService coordinatorService, StudentService studentService, ScrumMasterService scrumMasterService) {
        this.classroomRepository = classroomRepository;
        this.coordinatorService = coordinatorService;
        this.studentService = studentService;
        this.scrumMasterService = scrumMasterService;
    }

    public ResponseEntity<Classrooms> saveClassroom(CreateClassroomDTO classroomDTO, Long coordinatorId) {
        Coordinator coordinator = coordinatorService.getCoordinatorById(coordinatorId);

        Classrooms classroom = new Classrooms(
                classroomDTO.getTitle(),
                coordinator
        );

        Classrooms savedClassroom = classroomRepository.save(classroom);
        return ResponseEntity.ok(savedClassroom);
    }

    public Classrooms addStudentsToClassroom(Long classroomId, List<Long> studentIds) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "classroom not found"));

        for (Long studentId : studentIds) {
            Student student = studentService.getById(studentId);
            classroom.getStudents().add(student);
        }

        return classroomRepository.save(classroom);
    }

    public Classrooms addScrumMastersToClassroom(Long classroomId, List<Long> scrumMasterIds) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        //alterar repository para service
        for (Long scrumMasterId : scrumMasterIds) {
            ScrumMaster scrumMaster = scrumMasterService.getById(scrumMasterId);
            classroom.getScrumMasters().add(scrumMaster);
        }

        return classroomRepository.save(classroom);
    }

    public Classrooms getById(Long id){
        return classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public ResponseEntity<List<Classrooms>> listClassroom() {
        List<Classrooms> classrooms = classroomRepository.findAll();
        return ResponseEntity.ok(classrooms);
    }

    public ResponseEntity<Long> deleteClassroom(long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "classroom not found");
        }

        classroomRepository.deleteById(classroomId);
        return ResponseEntity.ok(classroomId);
    }


    public Classrooms addSquadToClassroom(Long classroomId, String squadName) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        Squad newSquad = new Squad(squadName);
        //jogar squad dentro de class
        classroom.getSquads().add(newSquad);
        return classroomRepository.save(classroom);
    }
}


