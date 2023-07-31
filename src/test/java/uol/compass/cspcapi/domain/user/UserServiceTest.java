package uol.compass.cspcapi.domain.user;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final User USER_1 = new User(
            "primeiro",
            "segundo",
            "teste@mail.com",
            "12345678"
    );

    private static final User USER_2 = new User(
            "primeiro",
            "segundo",
            "teste2@mail.com",
            "12345678"
    );

    private static final Role ROLE_1 = new Role("ROLE_ADMIN");
    private static final CreateUserDTO USER_3 = new CreateUserDTO(
            "firstName",
            "lastName",
            "test4@mail.com",
            "12345678"
    );

    @MockBean
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        userRepository = createRepository();
        roleRepository = createRoleRepository();

        userService = new UserService(userRepository, new PasswordEncoder());
        roleService = new RoleService(roleRepository);
        reset(userRepository);
        reset(roleRepository);
    }


    @AfterEach
    void tearDown() {
        reset(userRepository);
        reset(roleRepository);
    }

    @Test
    void saveUserSuccess(){
        when(userRepository.save(any(User.class))).thenReturn(USER_1);

        User newUser = userService.saveUser(USER_1);

        assertEquals("teste@mail.com", newUser.getEmail());
    }

    @Test
    void saveWithSetters(){
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(ROLE_1));
        User user = new User();
        user.setFirstName("primeiro");
        user.setLastName("segundo");
        user.setEmail("segundo@mail.com");
        user.setPassword("password");
        user.getRoles().add(roleService.findRoleByName(anyString()));

        assertEquals(user.getRoles().get(0), ROLE_1);
    }


    @Test
    void saveWithSettersFailRole(){
//        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(ROLE_1))
        User user = new User();
        user.setFirstName("primeiro");
        user.setLastName("segundo");
        user.setEmail("segundo@mail.com");
        user.setPassword("password");

        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> user.getRoles().add(roleService.findRoleByName("ROLE_NOT_EXISTS"))
        );

        assertEquals(404, response.getStatusCode().value());
    }
    @Test
    void saveUserFailDuplicated(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(USER_1)).thenThrow(ResponseStatusException.class);
        ResponseStatusException response = assertThrows(ResponseStatusException.class,
                () -> {
                    userService.saveUser(USER_1);
                },
                "user already exists"
        );

        assertEquals(400, response.getStatusCode().value());
        assertEquals("user already exists", response.getReason());
    }

    @Test
    void testSaveUserDTO() {
        when(userRepository.save(any(User.class))).thenReturn(new User(
                USER_3.getFirstName(),
                USER_3.getLastName(),
                USER_3.getEmail(),
                USER_3.getPassword()
        ));
        ResponseUserDTO user = userService.saveUser(USER_3);

        assertEquals("test4@mail.com", user.getEmail());
    }

    @Test
    void findByEmailOk() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(USER_1));

        User user = userService.findByEmail("teste@mail.com").get();
        assertEquals("teste@mail.com", user.getEmail());
    }

    @Test
    void findByEmailFail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(USER_1));

        User user = userService.findByEmail("test2@mail.com").get();
        assertNotEquals("test2@mail.com", user.getEmail());
    }

//    @Test
//    void mapToResponseUser() {
//    }
    private UserRepository createRepository(){
        UserRepository mock = mock(UserRepository.class);
        when(mock.findByEmail("teste@mail.com")).thenReturn(Optional.of(USER_1));
        when(mock.findByEmail("teste2@mail.com")).thenReturn(Optional.of(USER_2));
        return mock;
    }

    private RoleRepository createRoleRepository() {
        RoleRepository mock = mock(RoleRepository.class);
        when(mock.findByName("ROLE_ADMIN")).thenReturn(Optional.of(ROLE_1));
        when(mock.findByName("ROLE_NOT_EXISTS")).thenReturn(Optional.of(ROLE_1)).thenThrow(ResponseStatusException.class);
        return mock;
    }
}