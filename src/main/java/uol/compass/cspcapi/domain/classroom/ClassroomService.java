package uol.compass.cspcapi.domain.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
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

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, CoordinatorService coordinatorService, StudentService studentService) {
        this.classroomRepository = classroomRepository;
        this.coordinatorService = coordinatorService;
        this.studentService = studentService;
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

}
