package uol.compass.cspcapi.infrastructure.config.userDetails;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetilsServiceImplTest {

    private static UserDetilsServiceImpl userDetilsService;
    private static UserRepository userRepository;

    @BeforeAll
    static void setUp(){
        userRepository = mock(UserRepository.class);
        userDetilsService = new UserDetilsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername() {
        String username = "test@mail.com";
        User user = new User(
                "first",
                "last",
                username,
                "12345678"
        );
        user.getRoles().add(new Role("ROLE_ADMIN"));

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        var response = userDetilsService.loadUserByUsername(username);

        assertInstanceOf(UserDetails.class, response);
    }

    @Test
    void loadUserByUsernameNotFound() {
        String username = "test@mail.com";
        User user = new User(
                "first",
                "last",
                username,
                "12345678"
        );
        user.getRoles().add(new Role("ROLE_ADMIN"));

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        var response = assertThrows(RuntimeException.class,
                () -> userDetilsService.loadUserByUsername(username)
        );

        assertInstanceOf(RuntimeException.class, response);
    }
}