package uol.compass.cspcapi.domain.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.instructor.InstructorService;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentService;


import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    //repositories
    private ClassroomRepository classroomRepository;


    //services
    private CoordinatorService coordinatorService;
    private StudentService studentService;
    private ScrumMasterService scrumMasterService;
    private InstructorService instructorService;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, CoordinatorService coordinatorService, StudentService studentService, ScrumMasterService scrumMasterService, InstructorService instructorService) {
        this.classroomRepository = classroomRepository;
        this.coordinatorService = coordinatorService;
        this.studentService = studentService;
        this.scrumMasterService = scrumMasterService;
        this.instructorService = instructorService;
    }

    //Criando uma nova classroom
    public ResponseClassroomDTO saveClassroom(CreateClassroomDTO classroomDTO, Long coordinatorId) {
        Optional<Classrooms> alreadyExists = classroomRepository.findByTitle(classroomDTO.getTitle());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title already exists"
            );
        }

        Coordinator coordinator = coordinatorService.getById(coordinatorId);

        Classrooms classroom = new Classrooms(
                classroomDTO.getTitle(),
                coordinator
        );

        Classrooms savedClassroom = classroomRepository.save(classroom);
        return new ResponseClassroomDTO(
                savedClassroom.getId(),
                savedClassroom.getTitle(),
                savedClassroom.getCoordinator().getId()
        );
    }

    //Jogando users dentro da minha classroom
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

        for (Long scrumMasterId : scrumMasterIds) {
            ScrumMaster scrumMaster = scrumMasterService.getById(scrumMasterId);
            classroom.getScrumMasters().add(scrumMaster);
        }

        return classroomRepository.save(classroom);
    }

    public Classrooms addInstructorToClassroom(Long classroomId, List<Long> instructorIds) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        for (Long instructorId : instructorIds) {
            Instructor instructor = instructorService.getById(instructorId);
            classroom.getInstructors().add(instructor);
        }

        return classroomRepository.save(classroom);
    }


    public Classrooms removeStudentFromClassroom(Long classroomId, Long studentId) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        classroom.getStudents().removeIf(student -> student.getId().equals(studentId));

        return classroomRepository.save(classroom);
    }

    public Classrooms removeScrumMasterFromClassroom(Long classroomId, Long scrumMasterId) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        classroom.getScrumMasters().removeIf(scrumMaster -> scrumMaster.getId().equals(scrumMasterId));

        return classroomRepository.save(classroom);
    }

    public Classrooms removeInstructorFromClassroom(Long classroomId, Long instructorId) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        classroom.getInstructors().removeIf(instructor -> instructor.getId().equals(instructorId));

        return classroomRepository.save(classroom);
    }

    public ResponseClassroomDTO updateClassroom(Long id, UpdateClassroomDTO classroomDTO) {
        Classrooms classrooms = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        //classrooms.setTitle(classrooms.getTitle());
        classrooms.setTitle(classroomDTO.getTitle());
        classrooms.setCoordinator(coordinatorService.getById(classroomDTO.getCoordinatorId()));

        Classrooms updatedClassroom = classroomRepository.save(classrooms);

        return mapToResponseDTO(updatedClassroom);
    }

    //Possivel jogar o metodo em package UTILS
    private ResponseClassroomDTO mapToResponseDTO(Classrooms classroom) {
        ResponseClassroomDTO responseDTO = new ResponseClassroomDTO();
        responseDTO.setId(classroom.getId());
        responseDTO.setTitle(classroom.getTitle());
        responseDTO.setCoordinator(classroom.getCoordinator().getId());

        return responseDTO;
    }

    public Classrooms getById(Long id){
        return classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Classroom not found"
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


    public Classrooms addSquadToClassroom(Long classroomId, CreateClassroomDTO classroomDTO) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        Squad newSquad = new Squad(classroomDTO.getTitle());
        classroom.getSquads().add(newSquad);

        return classroomRepository.save(classroom);
    }
}


