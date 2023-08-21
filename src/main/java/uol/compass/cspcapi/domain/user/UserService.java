package uol.compass.cspcapi.domain.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseUserDTO saveUser (CreateUserDTO userDTO){
        if(findByEmail(userDTO.email()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists");
        }

        User user = new User(
                userDTO.firstName(),
                userDTO.lastName(),
                userDTO.email(),
                passwordEncoder.encoder().encode(userDTO.password())
        );
        User savedUser = userRepository.save(user);

        return mapToResponseUser(savedUser);
    }

    @Transactional
    public User saveUser(User user){
        if(findByEmail(user.getEmail()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists");
        }
         User newUser = userRepository.save(new User(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                passwordEncoder.encoder().encode(user.getPassword())
        ));
        newUser.getRoles().addAll(user.getRoles());
        return newUser;
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

  public ResponseUserDTO mapToResponseUser(User user) {
        return new ResponseUserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
