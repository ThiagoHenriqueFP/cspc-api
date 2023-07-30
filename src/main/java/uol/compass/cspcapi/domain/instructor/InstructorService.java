package uol.compass.cspcapi.domain.instructor;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.ResponseInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

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
        Optional<User> alreadyExists = userService.findByEmail(instructor.getEmail());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User newUser = new User(
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                passwordEncrypt.encoder().encode(instructor.getPassword())
        );
        User savedUser = userService.saveUser(newUser);

        Instructor newInstructor = new Instructor(
                savedUser
        );

        Instructor instructorDb = instructorRepository.save(newInstructor);
        return new ResponseInstructorDTO(
                instructorDb.getUser().getId(),
                instructorDb.getUser().getFirstName(),
                instructorDb.getUser().getLastName(),
                instructorDb.getUser().getEmail()
        );
    }

    public Instructor getById(Long id) {
        return instructorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public List<Instructor> getAll() {
        return instructorRepository.findAll();
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
        user.setFirstName(instructorDTO.getFirstName());
        user.setLastName(instructorDTO.getLastName());
        user.setEmail(instructorDTO.getEmail());

        instructor.setUser(user);

        instructorRepository.save(instructor);
        return new ResponseInstructorDTO(
                instructor.getId(),
                instructor.getUser().getFirstName(),
                instructor.getUser().getLastName(),
                instructor.getUser().getEmail()
        );
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
    public List<Instructor> attributeInstructorsToClassroom(Classroom classroom, List<Instructor> instructors) {
        for (Instructor instructor : instructors) {
            instructor.setClassroom(classroom);
        }
        return instructorRepository.saveAll(instructors);
    }
}
