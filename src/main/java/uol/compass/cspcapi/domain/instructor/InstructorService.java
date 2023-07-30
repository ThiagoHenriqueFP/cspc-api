package uol.compass.cspcapi.domain.instructor;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.student.dto.ResponseStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {
    private InstructorRepository instructorRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncrypt;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, UserService userService, PasswordEncoder passwordEncrypt) {
        this.instructorRepository = instructorRepository;
        this.userService = userService;
        this.passwordEncrypt = passwordEncrypt;
    }

    @Transactional
    public ResponseInstructorDTO save(CreateInstructorDTO instructor) {
        Optional<User> alreadyExists = userService.findByEmail(instructor.getUser().getEmail());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User user = new User(
                instructor.getUser().getFirstName(),
                instructor.getUser().getLastName(),
                instructor.getUser().getEmail(),
                passwordEncrypt.encoder().encode(instructor.getUser().getPassword())
        );

        Instructor newInstructor = new Instructor(user);
        Instructor instructorDb = instructorRepository.save(newInstructor);

        return mapToResponseInstructor(instructorDb);
    }

    public ResponseInstructorDTO getById(Long id) {
        return mapToResponseInstructor(instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "instructor not found"
                )
        ));
    }

    public List<ResponseInstructorDTO> getAll() {
        List<Instructor> instructors = instructorRepository.findAll();
        return mapToResponseInstructors(instructors);
    }

    @Transactional
    public ResponseInstructorDTO update(Long id, UpdateInstructorDTO instructorDTO) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        User user = instructor.getUser();

        user.setFirstName(instructorDTO.getUser().getFirstName());
        user.setLastName(instructorDTO.getUser().getLastName());
        user.setEmail(instructorDTO.getUser().getEmail());

        instructor.setUser(user);

        Instructor updatedInstructor = instructorRepository.save(instructor);

        return mapToResponseInstructor(updatedInstructor);
    }

    @Transactional
    public void deleteById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "instructor not found"
                )
        );

        instructorRepository.delete(instructor);
    }

    public List<Instructor> getAllInstructorsById(List<Long> instructorsIds) {
        return instructorRepository.findAllByIdIn(instructorsIds);
    }

    @Transactional
    public List<ResponseInstructorDTO> attributeInstructorsToClassroom(Classroom classroom, List<Instructor> instructors) {
        for (Instructor instructor : instructors) {
            instructor.setClassroom(classroom);
        }
        List<Instructor> updatedInstructors = instructorRepository.saveAll(instructors);
        return mapToResponseInstructors(updatedInstructors);
    }

    public ResponseInstructorDTO mapToResponseInstructor(Instructor instructor) {
        Long classroomId;

        if (instructor.getClassroom() == null) {
            classroomId = null;
        } else {
            classroomId = instructor.getClassroom().getId();
        }

        return new ResponseInstructorDTO(
                instructor.getId(),
                userService.mapToResponseUser(instructor.getUser()),
                classroomId
        );
    }

    public List<ResponseInstructorDTO> mapToResponseInstructors(List<Instructor> instructors) {
        List<ResponseInstructorDTO> instructorsNoPassword = new ArrayList<>();

        for (Instructor instructor : instructors) {
            instructorsNoPassword.add(mapToResponseInstructor(instructor));
        }
        return instructorsNoPassword;
    }
}
