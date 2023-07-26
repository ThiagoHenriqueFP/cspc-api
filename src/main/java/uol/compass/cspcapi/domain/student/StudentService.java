package uol.compass.cspcapi.domain.student;

import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private StudentRepository studentRepository;

    public Student save(Student student) {
        return studentRepository.save(student);
    }
}
