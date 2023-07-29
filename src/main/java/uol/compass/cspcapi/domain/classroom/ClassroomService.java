package uol.compass.cspcapi.domain.classroom;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.ResponseClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.Squad.SquadService;
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
    private SquadService squadService;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, CoordinatorService coordinatorService, StudentService studentService, ScrumMasterService scrumMasterService, InstructorService instructorService, SquadService squadService) {
        this.classroomRepository = classroomRepository;
        this.coordinatorService = coordinatorService;
        this.studentService = studentService;
        this.scrumMasterService = scrumMasterService;
        this.instructorService = instructorService;
        this.squadService = squadService;
    }

    //Criando uma nova classroom
    public ResponseClassroomDTO saveClassroom(CreateClassroomDTO classroomDTO, Long coordinatorId) {
        Optional<Classroom> alreadyExists = classroomRepository.findByTitle(classroomDTO.getTitle());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title already exists"
            );
        }

        Coordinator coordinator = coordinatorService.getById(coordinatorId);

        Classroom classroom = new Classroom(
                classroomDTO.getTitle(),
                coordinator
        );

        Classroom savedClassroom = classroomRepository.save(classroom);
        return new ResponseClassroomDTO(
                savedClassroom.getId(),
                savedClassroom.getTitle(),
                savedClassroom.getCoordinator().getId()
        );
    }

    //Jogando users dentro da minha classroom
    @Transactional
    public Classroom addStudentsToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "classroom not found"));

        List<Student> students = classroom.getStudents();
        List<Student> newStudents = studentService.getAllStudentsById(classroomDTO.getGeneralUsersIds());
        students.addAll(newStudents);

        studentService.attributeStudentsToClassroom(classroom, students);
        classroom.setStudents(students);
//        for (Long studentId : students.getStudentsIds()) {
//            Student student = studentService.getById(studentId);
//            classroom.getStudents().add(student);
//        }

        return classroomRepository.save(classroom);
    }

    public Classroom addScrumMastersToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<ScrumMaster> scrumMasters = classroom.getScrumMasters();
        List<ScrumMaster> newScrumMasters = scrumMasterService.getAllScrumMastersById(classroomDTO.getGeneralUsersIds());
        scrumMasters.addAll(newScrumMasters);

        scrumMasterService.attributeScrumMastersToClassroom(classroom, scrumMasters);
        classroom.setScrumMasters(scrumMasters);

        return classroomRepository.save(classroom);
    }

    public Classroom addInstructorsToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Instructor> instructors = classroom.getInstructors();
        List<Instructor> newInstructors = instructorService.getAllInstructorsById(classroomDTO.getGeneralUsersIds());
        instructors.addAll(newInstructors);

        instructorService.attributeInstructorsToClassroom(classroom, instructors);
        classroom.setInstructors(instructors);

        return classroomRepository.save(classroom);
    }

    public Classroom addSquadsToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Squad> squads = classroom.getSquads();
        List<Squad> newSquads = squadService.getAllSquadsById(classroomDTO.getGeneralUsersIds());
        squads.addAll(newSquads);

        squadService.attributeSquadsToClassroom(classroom, squads);
        classroom.setSquads(squads);

        return classroomRepository.save(classroom);
    }


    public Classroom removeStudentsFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        //classroom.getStudents().removeIf(student -> student.getId().equals(studentId));

        List<Student> students = classroom.getStudents();

        students.removeIf(
                student -> classroomDTO.getGeneralUsersIds().contains(student.getId())
        );

        List<Student> toRemoveStudents = studentService.getAllStudentsById(classroomDTO.getGeneralUsersIds());
        studentService.attributeStudentsToClassroom(null, toRemoveStudents);

        classroom.setStudents(students);

        return classroomRepository.save(classroom);
    }

    public Classroom removeScrumMastersFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<ScrumMaster> scrumMasters = classroom.getScrumMasters();

        scrumMasters.removeIf(
                scrumMaster -> classroomDTO.getGeneralUsersIds().contains(scrumMaster.getId())
        );

        List<ScrumMaster> toRemoveScrumMasters = scrumMasterService.getAllScrumMastersById(classroomDTO.getGeneralUsersIds());
        scrumMasterService.attributeScrumMastersToClassroom(null, toRemoveScrumMasters);

        classroom.setScrumMasters(scrumMasters);

        return classroomRepository.save(classroom);
    }

    public Classroom removeInstructorsFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Instructor> instructors = classroom.getInstructors();

        instructors.removeIf(
                instructor -> classroomDTO.getGeneralUsersIds().contains(instructor.getId())
        );

        List<Instructor> toRemoveInstructors = instructorService.getAllInstructorsById(classroomDTO.getGeneralUsersIds());
        instructorService.attributeInstructorsToClassroom(null, toRemoveInstructors);

        classroom.setInstructors(instructors);

        return classroomRepository.save(classroom);
    }

    public Classroom removeSquadsFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Squad> squads = classroom.getSquads();

        squads.removeIf(
                squad -> classroomDTO.getGeneralUsersIds().contains(squad.getId())
        );

        List<Squad> toRemoveSquads = squadService.getAllSquadsById(classroomDTO.getGeneralUsersIds());
        squadService.attributeSquadsToClassroom(null, toRemoveSquads);

        classroom.setSquads(squads);

        return classroomRepository.save(classroom);
    }

    public ResponseClassroomDTO updateClassroom(Long id, UpdateClassroomDTO classroomDTO) {
        Classroom classrooms = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        //classrooms.setTitle(classrooms.getTitle());
        classrooms.setTitle(classroomDTO.getTitle());
        classrooms.setCoordinator(coordinatorService.getById(classroomDTO.getCoordinatorId()));

        Classroom updatedClassroom = classroomRepository.save(classrooms);

        return mapToResponseDTO(updatedClassroom);
    }

    //Possivel jogar o metodo em package UTILS
    private ResponseClassroomDTO mapToResponseDTO(Classroom classroom) {
        ResponseClassroomDTO responseDTO = new ResponseClassroomDTO();
        responseDTO.setId(classroom.getId());
        responseDTO.setTitle(classroom.getTitle());
        responseDTO.setCoordinator(classroom.getCoordinator().getId());

        return responseDTO;
    }

    public Classroom getById(Long id){
        return classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Classroom not found"
                )
        );
    }

    public ResponseEntity<List<Classroom>> listClassroom() {
        List<Classroom> classrooms = classroomRepository.findAll();
        return ResponseEntity.ok(classrooms);
    }

    public ResponseEntity<Long> deleteClassroom(long classroomId) {
        if (!classroomRepository.existsById(classroomId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "classroom not found");
        }

        classroomRepository.deleteById(classroomId);
        return ResponseEntity.ok(classroomId);
    }
}


