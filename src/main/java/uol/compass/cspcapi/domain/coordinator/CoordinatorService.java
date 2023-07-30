package uol.compass.cspcapi.domain.coordinator;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.UpdateCoordinatorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.ArrayList;
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
        Optional<User> alreadyExists = userService.findByEmail(coordinator.getUser().getEmail());

        if(alreadyExists.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        User user = new User(
                coordinator.getUser().getFirstName(),
                coordinator.getUser().getLastName(),
                coordinator.getUser().getEmail(),
                passwordEncrypt.encoder().encode(coordinator.getUser().getPassword())
        );

        Coordinator newCoordinator = new Coordinator(user);
        Coordinator coordinatorDb = coordinatorRepository.save(newCoordinator);

        return mapToResponseCoordinator(coordinatorDb);
    }

    public ResponseCoordinatorDTO getById(Long id) {
        return mapToResponseCoordinator(coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        ));
    }

    public Coordinator getByIdOriginal(Long id) {
        return coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );
    }

    public List<ResponseCoordinatorDTO> getAll() {
        List<Coordinator> coordinators = coordinatorRepository.findAll();
        List<ResponseCoordinatorDTO> coordinatorsNoPassword = new ArrayList<>();

        for (Coordinator coordinator : coordinators) {
            coordinatorsNoPassword.add(mapToResponseCoordinator(coordinator));
        }
        return coordinatorsNoPassword;
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

        user.setFirstName(coordinatorDTO.getUser().getFirstName());
        user.setLastName(coordinatorDTO.getUser().getLastName());
        user.setEmail(coordinatorDTO.getUser().getEmail());

        coordinator.setUser(user);

        Coordinator updatedCoordinator = coordinatorRepository.save(coordinator);

        return mapToResponseCoordinator(updatedCoordinator);
    }

    @Transactional
    public void deleteById(Long id) {
        Coordinator coordinator = coordinatorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "user not found"
                )
        );

        coordinatorRepository.delete(coordinator);
    }

    private ResponseCoordinatorDTO mapToResponseCoordinator(Coordinator coordinatorDb) {
        return new ResponseCoordinatorDTO(
                coordinatorDb.getId(),
                userService.mapToResponseUser(coordinatorDb.getUser())
        );
    }
}
