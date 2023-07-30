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
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {
    private InstructorRepository instructorRepository;
    private final UserService userService;
    private final PasswordEncrypt passwordEncrypt;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository, UserService userService, PasswordEncrypt passwordEncrypt) {
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
                        "user not found"
                )
        ));
    }

    public List<ResponseInstructorDTO> getAll() {
        List<Instructor> instructors = instructorRepository.findAll();
        List<ResponseInstructorDTO> instructorsNoPassword = new ArrayList<>();

        for (Instructor instructor : instructors) {
            instructorsNoPassword.add(mapToResponseInstructor(instructor));
        }
        return instructorsNoPassword;
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

        Instructor updatedInstructors = instructorRepository.save(instructor);

        return mapToResponseInstructor(updatedInstructors);
    }

    @Transactional
    public boolean deleteById(Long id) {
        Instructor instructor = instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );

        instructorRepository.delete(instructor);
        return true;
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
        List<ResponseInstructorDTO> instructorsNoPassword = new ArrayList<>();

        for (Instructor instructor : updatedInstructors) {
            instructorsNoPassword.add(mapToResponseInstructor(instructor));
        }
        return instructorsNoPassword;
    }

    public ResponseInstructorDTO mapToResponseInstructor(Instructor instructor) {
        return new ResponseInstructorDTO(
                instructor.getId(),
                userService.mapToResponseUser(instructor.getUser())//, instructor.getClassroom()
        );
    }
}
