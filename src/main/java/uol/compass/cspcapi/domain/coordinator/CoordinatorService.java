package uol.compass.cspcapi.domain.coordinator;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.UpdateCoordinatorDTO;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinatorService {
    private CoordinatorRepository coordinatorRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncrypt;

    @Autowired
    public CoordinatorService(CoordinatorRepository coordinatorRepository, UserService userService, PasswordEncoder passwordEncrypt) {
        this.coordinatorRepository = coordinatorRepository;
        this.userService = userService;
        this.passwordEncrypt = passwordEncrypt;
    }

    @Transactional
    public ResponseCoordinatorDTO save(CreateCoordinatorDTO coordinator) {
        Optional<User> alreadyExists = userService.findByEmail(coordinator.getEmail());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User newUser = new User(
                coordinator.getFirstName(),
                coordinator.getLastName(),
                coordinator.getEmail(),
                passwordEncrypt.encoder().encode(coordinator.getPassword())
        );
        User savedUser = userService.saveUser(newUser);

        Coordinator newCoordinator = new Coordinator(
                savedUser
        );

        Coordinator coordinatorDb = coordinatorRepository.save(newCoordinator);
        return new ResponseCoordinatorDTO(
                coordinatorDb.getUser().getId(),
                coordinatorDb.getUser().getFirstName(),
                coordinatorDb.getUser().getLastName(),
                coordinatorDb.getUser().getEmail()
        );
    }

    public Coordinator getById(Long id) {
        return coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public List<Coordinator> getAll() {
        return coordinatorRepository.findAll();
    }

    @Transactional
    public ResponseCoordinatorDTO update(Long id, UpdateCoordinatorDTO coordinatorDTO) {
        Coordinator coordinator = coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "student not found"
                )
        );

        User user = coordinator.getUser();
        user.setFirstName(coordinatorDTO.getFirstName());
        user.setLastName(coordinatorDTO.getLastName());
        user.setEmail(coordinatorDTO.getEmail());

        coordinator.setUser(user);

        coordinatorRepository.save(coordinator);
        return new ResponseCoordinatorDTO(
                coordinator.getId(),
                coordinator.getUser().getFirstName(),
                coordinator.getUser().getLastName(),
                coordinator.getUser().getEmail()
        );
    }

    @Transactional
    public boolean deleteById(Long id) {
        Coordinator coordinator = coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );

        coordinatorRepository.delete(coordinator);
        return true;
    }
}
