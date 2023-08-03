package uol.compass.cspcapi.integrationTests.classroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uol.compass.cspcapi.application.api.auth.dto.LoginDTO;
import uol.compass.cspcapi.application.api.classroom.ClassroomController;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseDTO;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.infrastructure.config.auth.SecurityConfig;
import uol.compass.cspcapi.infrastructure.config.jwt.JwtTokenFilter;
import uol.compass.cspcapi.infrastructure.config.jwt.JwtTokenProvider;
import uol.compass.cspcapi.infrastructure.config.userDetails.UserDetailsImpl;
import uol.compass.cspcapi.infrastructure.config.userDetails.UserDetilsServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@Import({SecurityConfig.class, UserDetailsImpl.class, UserDetilsServiceImpl.class})
@Import({JwtTokenFilter.class, UserDetailsImpl.class, UserDetilsServiceImpl.class, UserRepository.class})
//@SpringBootTest
@WebMvcTest(ClassroomController.class)
@ContextConfiguration(classes = SecurityConfig.class)
//@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
//@Sql(scripts = {"/import_classrooms.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = {"/remove_classrooms.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClassroomControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private ClassroomService classroomService;

//    @BeforeEach
//    public String login() throws Exception {
//        String email = "admin@mail.com";
//        String password = "12345678";
//
//        LoginDTO loginDTO = new LoginDTO(email, password);
//
//        MvcResult result = mockMvc.perform(post("/auth/signin")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(loginDTO)))
//                .andExpect(status().isOk())
//                .andReturn();
//                //.andExpect(jsonPath("$.data").isNotEmpty());
//
//        String responseJson = result.getResponse().getContentAsString();
//        String token = JsonPath.read(responseJson, "$.data");
//
//        System.out.println("Esse é o token: " + responseJson);
//        return token;
//    }

    @Test
    public void testCreateClassroom_Success() throws Exception {
        String firstName = "Julio";
        String lastName = "Soares";
        String email = "julio.soares@mail.com";
        String password = "julio.soares";

        User user = new User(firstName, lastName, email, password);
        Coordinator coordinator = new Coordinator(user);
        coordinator.setId(1L);

        String title = "Spring Boot Junho";
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO(title, coordinator.getId());

        // String authToken = login();

        MvcResult result = this.mockMvc.perform(post("classrooms")
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(classroomDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(title))
                .andExpect(jsonPath("$.data.coordinator.id").value(coordinator.getId()))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseTitle = JsonPath.read(responseJson, "$.data.title");
        String responseCoordinatorid = JsonPath.read(responseJson, "$.data.coordinator.id");

        assertEquals(title, responseTitle);
        assertEquals(coordinator.getId(), responseCoordinatorid);

//        mockMvc.perform(post("/classrooms")
//                        .header("Authorization", "Bearer " + authToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(classroomDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.title").value(title))
//                .andExpect(jsonPath("$.data.coordinator.id").value(coordinator.getId()));
    }



    // Helper method to convert object to JSON string
    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
