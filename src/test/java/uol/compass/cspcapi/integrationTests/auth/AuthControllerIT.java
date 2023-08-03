package uol.compass.cspcapi.integrationTests.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uol.compass.cspcapi.application.api.auth.dto.LoginDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.jwt.JwtTokenProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Test
    public void testSaveAdminUserSuccess() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "admin123";

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName(firstName);
        createUserDTO.setLastName(lastName);
        createUserDTO.setEmail(email);
        createUserDTO.setPassword(password);

        User user = new User(firstName, lastName, email, password);
        Role adminRole = new Role("ROLE_ADMIN");
        user.getRoles().add(adminRole);

        when(roleService.findRoleByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value(firstName))
                .andExpect(jsonPath("$.data.lastName").value(lastName))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.password").value(password))
                .andExpect(jsonPath("$.data.roles[0].name").value("ROLE_ADMIN"));
    }

    @Test
    public void testSaveAdminUserError() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "admin123";

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setFirstName(firstName);
        createUserDTO.setLastName(lastName);
        createUserDTO.setEmail(email);
        createUserDTO.setPassword(password);

        when(roleService.findRoleByName("ROLE_ADMIN")).thenReturn(null);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserDTO)))
                .andExpect(status().isBadRequest());
                //.andExpect(jsonPath("$.error").value("Bad Request"))
                //.andExpect(jsonPath("$.message").value("Error creating admin user."));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String email = "test@example.com";
        String password = "test123";

        //todo authentication returning null with correct DTO

        LoginDTO loginDTO = new LoginDTO(email, password);

        String token = "test-token";
//        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(email, password));
//        when(jwtTokenProvider.createToken(any())).thenReturn(token);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(token));
    }

    @Test
    public void testLoginError() throws Exception {
        String email = "test@example.com";
        String password = "test123";

        LoginDTO loginDTO = new LoginDTO(email, password);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value("Invalid credentials"));
                //.andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    // Helper method to convert object to JSON string
    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

//    @Test
//    public void testLoginSuccess() throws Exception {
//        String email = "test@example.com";
//        String password = "test123";
//
//        LoginDTO loginDTO = new LoginDTO(email, password);
//
//        String token = "test-token";
//        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(email, password));
//        when(jwtTokenProvider.createToken(any())).thenReturn(token);
//
//        mockMvc.perform(post("/auth/signin")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(loginDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(token));
//    }
}
