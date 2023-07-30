package uol.compass.cspcapi.infrastructure.config.userDetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;

import java.util.Optional;

@Service
public class UserDetilsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetilsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            User user = userRepository.findByEmail(username).orElseThrow(
                    () -> new UsernameNotFoundException(
                            "username not found"
                    )
            );

            return new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getRoles());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
