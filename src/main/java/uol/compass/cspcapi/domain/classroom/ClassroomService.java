package uol.compass.cspcapi.domain.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.student.StudentRepository;


import java.util.List;

@Service
public class ClassroomService {

    private ClassroomRepository classroomRepository;

    private StudentRepository studentRepository;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository, StudentRepository studentRepository) {
        this.classroomRepository = classroomRepository;
        this.studentRepository = studentRepository;
    }

    public Classrooms saveClassroom(CreateClassroomDTO classroomDTO){
        Classrooms classrooms = new Classrooms (
                classroomDTO.getTitle(),
                classroomDTO.getCoordinator());

        return classroomRepository.save(classrooms);
    }


    public Classrooms addStudentToClass(Long classroomId, Long studentId) {

        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "classroom not found"));

        //Acessando repositorio do Student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));

        classroom.getStudents().add(student);

        return classroomRepository.save(classroom);
    }

    public Classrooms addManyStudentsToClassroom(Long classroomId, List<Long> studentIds) {
        Classrooms classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "classroom not found"));

        //Acessando repositorio do Student
        List<Student> students = studentRepository.findAllById(studentIds);

        if (students.size() != studentIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some students were not found");
        }

        classroom.getStudents().addAll(students);

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
