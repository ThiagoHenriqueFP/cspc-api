package uol.compass.cspcapi.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser (CreateUserDTO userDTO){
        User user = new User(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail()
        );

        Optional<User> userAlreadyExists = userRepository.findByEmail(userDTO.getEmail());

        if(userAlreadyExists.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "user already exists"
            );
        }

        return userRepository.save(user);
    }
}
