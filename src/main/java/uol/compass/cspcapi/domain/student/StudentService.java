package uol.compass.cspcapi.domain.student;

public class StudentService {
    private StudentRepository studentRepository;

    public Student save(Student student) {
        return studentRepository.save(student);
    }
}
