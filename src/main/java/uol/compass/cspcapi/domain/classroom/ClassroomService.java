package uol.compass.cspcapi.domain.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.coordinator.CoordinatorService;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentRepository;


import java.util.List;

@Service
public class ClassroomService {

    private ClassroomRepository classroomRepository;
    private StudentRepository studentRepository;
    private CoordinatorService coordinatorService;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, StudentRepository studentRepository, CoordinatorService coordinatorService) {
        this.classroomRepository = classroomRepository;
        this.studentRepository = studentRepository;
        this.coordinatorService = coordinatorService;
    }

    public Classrooms saveClassroom(CreateClassroomDTO classroomDTO, Long coordinatorId) {
        Coordinator coordinator = coordinatorService.getCoordinatorById(coordinatorId);

        Classrooms classrooms = new Classrooms(
                classroomDTO.getTitle(),
                coordinator
        );

        return classroomRepository.save(classrooms);
    }


    public Classrooms addStudentsToClassroom(Long classroomId, List<Long> studentIds) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "classroom not found"));

        for (Long studentId : studentIds) {
            // Acessando o repositório do Student para obter cada estudante pelo ID
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));

            classroom.getStudents().add(student);
        }

        return classroomRepository.save(classroom);
    }


    public List<Classrooms> listClassroom(){

        return classroomRepository.findAll();
    }

    public Classrooms deleteClassromm(Classrooms classrooms){

        if (!classroomRepository.existsById(classrooms.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "classroom not found");
        }

        return classroomRepository.deleteById(classrooms);
    }

}
