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


import java.util.ArrayList;
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
    @Transactional
    public ResponseClassroomDTO saveClassroom(CreateClassroomDTO classroomDTO, Long coordinatorId) {
        Optional<Classroom> alreadyExists = classroomRepository.findByTitle(classroomDTO.getTitle());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title already exists"
            );
        }

        Coordinator coordinator = coordinatorService.getByIdOriginal(coordinatorId);

        Classroom classroom = new Classroom(
                classroomDTO.getTitle(),
                coordinator
        );

        Classroom savedClassroom = classroomRepository.save(classroom);
        return mapToResponseClassroom(savedClassroom);
    }

    public ResponseClassroomDTO getById(Long id){
        return mapToResponseClassroom(classroomRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Classroom not found"
                )
        ));
    }

    public List<ResponseClassroomDTO> getAllClassrooms() {
        List<Classroom> classrooms = classroomRepository.findAll();
        List<ResponseClassroomDTO> classroomsNoPassword = new ArrayList<>();

        for (Classroom classroom : classrooms) {
            classroomsNoPassword.add(mapToResponseClassroom(classroom));
        }

        return classroomsNoPassword;
    }

    @Transactional
    public ResponseClassroomDTO updateClassroom(Long id, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        //classrooms.setTitle(classrooms.getTitle());
        classroom.setTitle(classroomDTO.getTitle());
        classroom.setCoordinator(coordinatorService.getByIdOriginal(classroomDTO.getCoordinatorId()));

        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public void deleteClassroom(Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Squad not found"
                )
        );
        List<Student> toRemoveStudents = classroom.getStudents();
        studentService.attributeStudentsToClassroom(null, toRemoveStudents);

        List<Instructor> toRemoveInstructors = classroom.getInstructors();
        instructorService.attributeInstructorsToClassroom(null, toRemoveInstructors);

        List<ScrumMaster> toRemoveScrumMasters = classroom.getScrumMasters();
        scrumMasterService.attributeScrumMastersToClassroom(null, toRemoveScrumMasters);

        List<Squad> toRemoveSquads = classroom.getSquads();
        squadService.attributeSquadsToClassroom(null, toRemoveSquads);

        toRemoveStudents.removeIf(student -> true);
        classroom.setStudents(toRemoveStudents);

        toRemoveInstructors.removeIf(instructor -> true);
        classroom.setStudents(toRemoveStudents);

        toRemoveScrumMasters.removeIf(scrumMaster -> true);
        classroom.setStudents(toRemoveStudents);

        toRemoveSquads.removeIf(squad -> true);
        classroom.setStudents(toRemoveStudents);

        classroomRepository.save(classroom);
        classroomRepository.deleteById(classroom.getId());
    }

    //Jogando users dentro da minha classroom
    @Transactional
    public ResponseClassroomDTO addStudentsToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "classroom not found"));

        List<Student> students = classroom.getStudents();
        List<Student> newStudents = studentService.getAllStudentsById(classroomDTO.getGeneralUsersIds());
        students.addAll(newStudents);

        studentService.attributeStudentsToClassroom(classroom, students);
        classroom.setStudents(students);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO addScrumMastersToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<ScrumMaster> scrumMasters = classroom.getScrumMasters();
        List<ScrumMaster> newScrumMasters = scrumMasterService.getAllScrumMastersById(classroomDTO.getGeneralUsersIds());
        scrumMasters.addAll(newScrumMasters);

        scrumMasterService.attributeScrumMastersToClassroom(classroom, scrumMasters);
        classroom.setScrumMasters(scrumMasters);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO addInstructorsToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Instructor> instructors = classroom.getInstructors();
        List<Instructor> newInstructors = instructorService.getAllInstructorsById(classroomDTO.getGeneralUsersIds());
        instructors.addAll(newInstructors);

        instructorService.attributeInstructorsToClassroom(classroom, instructors);
        classroom.setInstructors(instructors);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO addSquadsToClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Squad> squads = classroom.getSquads();
        List<Squad> newSquads = squadService.getAllSquadsById(classroomDTO.getGeneralUsersIds());
        squads.addAll(newSquads);

        squadService.attributeSquadsToClassroom(classroom, squads);
        classroom.setSquads(squads);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }


    @Transactional
    public ResponseClassroomDTO removeStudentsFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
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
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO removeScrumMastersFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<ScrumMaster> scrumMasters = classroom.getScrumMasters();

        scrumMasters.removeIf(
                scrumMaster -> classroomDTO.getGeneralUsersIds().contains(scrumMaster.getId())
        );

        List<ScrumMaster> toRemoveScrumMasters = scrumMasterService.getAllScrumMastersById(classroomDTO.getGeneralUsersIds());
        scrumMasterService.attributeScrumMastersToClassroom(null, toRemoveScrumMasters);

        classroom.setScrumMasters(scrumMasters);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO removeInstructorsFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Instructor> instructors = classroom.getInstructors();

        instructors.removeIf(
                instructor -> classroomDTO.getGeneralUsersIds().contains(instructor.getId())
        );

        List<Instructor> toRemoveInstructors = instructorService.getAllInstructorsById(classroomDTO.getGeneralUsersIds());
        instructorService.attributeInstructorsToClassroom(null, toRemoveInstructors);

        classroom.setInstructors(instructors);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    @Transactional
    public ResponseClassroomDTO removeSquadsFromClassroom(Long classroomId, UpdateClassroomDTO classroomDTO) {
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Classroom not found"));

        List<Squad> squads = classroom.getSquads();

        squads.removeIf(
                squad -> classroomDTO.getGeneralUsersIds().contains(squad.getId())
        );

        List<Squad> toRemoveSquads = squadService.getAllSquadsById(classroomDTO.getGeneralUsersIds());
        squadService.attributeSquadsToClassroom(null, toRemoveSquads);

        classroom.setSquads(squads);
        Classroom updatedClassroom = classroomRepository.save(classroom);

        return mapToResponseClassroom(updatedClassroom);
    }

    public ResponseClassroomDTO mapToResponseClassroom(Classroom classroom) {
        if (classroom.getStudents() == null) {
            classroom.setStudents(new ArrayList<>());
        }
        if (classroom.getInstructors() == null) {
            classroom.setInstructors(new ArrayList<>());
        }
        if (classroom.getScrumMasters() == null) {
            classroom.setScrumMasters(new ArrayList<>());
        }
        if (classroom.getSquads() == null) {
            classroom.setSquads(new ArrayList<>());
        }
        return new ResponseClassroomDTO(
                classroom.getId(),
                classroom.getTitle(),
                classroom.getCoordinator(),
                studentService.mapToResponseStudents(classroom.getStudents()),
                instructorService.mapToResponseInstructors(classroom.getInstructors()),
                scrumMasterService.mapToResponseScrumMasters(classroom.getScrumMasters()),
                squadService.mapToResponseSquads(classroom.getSquads())
        );
    }
}


