package uol.compass.cspcapi.integrationTests.classroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.coordinator.dto.CreateCoordinatorDTO;
import uol.compass.cspcapi.application.api.generalPourposeDTO.ResponseDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
//@Sql(scripts = {"/import_classrooms.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = {"/remove_classrooms.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClassroomControllerIT {
    @Autowired
    private MockMvc mockMvc;

    public String login() throws Exception {
        String email = "admin@mail.com";
        String password = "12345678";

        LoginDTO loginDTO = new LoginDTO(email, password);

        MvcResult result = mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String token = JsonPath.read(responseJson, "$.data");

        return token;
    }

    @Disabled
    @Test
    public void testCreateClassroom_Success() throws Exception {
        // User user = new User();
        Long coordinatorId = 1L;
        Coordinator coordinator = new Coordinator();
        coordinator.setId(coordinatorId);

        String clasrroomTitle = "Spring Boot Maio";
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO(clasrroomTitle, coordinatorId);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(post("/classrooms")
                        .header("Authorization", "Bearer " + authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(classroomDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(clasrroomTitle))
                .andExpect(jsonPath("$.coordinator.id").value(2L))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseTitle = JsonPath.read(responseJson, "$.title");
        Integer responseId = JsonPath.read(responseJson, "$.coordinator.id");
        Long responseCoordinatorid = responseId.longValue();

        assertEquals(clasrroomTitle, responseTitle);
        assertEquals(coordinator.getId(), responseCoordinatorid);
    }

    @Disabled
    @Test
    public void testGetByIdClassroom_Success() throws Exception {
        // User user = new User();
        Long coordinatorId = 2L;
        Coordinator coordinator = new Coordinator();
        coordinator.setId(coordinatorId);

        String clasrroomTitle = "Spring Boot Maio";
        Long classroomId = 1L;
        CreateClassroomDTO classroomDTO = new CreateClassroomDTO(clasrroomTitle, coordinatorId);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/classrooms/" + classroomId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(clasrroomTitle))
                .andExpect(jsonPath("$.coordinator.id").value(coordinatorId))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseTitle = JsonPath.read(responseJson, "$.title");
        Integer responseId = JsonPath.read(responseJson, "$.coordinator.id");
        Long responseCoordinatorid = responseId.longValue();

        assertEquals(clasrroomTitle, responseTitle);
        assertEquals(coordinator.getId(), responseCoordinatorid);
    }

    @Disabled
    @Test
    public void testGetAllClassrooms_Success() throws Exception {
        // User user = new User();
        Long coordinatorId_1 = 2L;
        Coordinator coordinator_1 = new Coordinator();
        coordinator_1.setId(coordinatorId_1);

        String classroomTitle_1 = "Spring Boot Maio";
        Long classroomId_1 = 1L;

        Long coordinatorId_2 = 1L;
        Coordinator coordinator_2 = new Coordinator();
        coordinator_2.setId(coordinatorId_2);

        String classroomTitle_2 = "Spring Boot AWS Junho";
        Long classroomId_2 = 2L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/classrooms")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(classroomTitle_1))
                .andExpect(jsonPath("$[1].title").value(classroomTitle_2))
                .andExpect(jsonPath("$[0].coordinator.id").value(coordinatorId_1))
                .andExpect(jsonPath("$[1].coordinator.id").value(coordinatorId_2))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        String responseTitle_1 = JsonPath.read(responseJson, "$[0].title");
        Integer responseId_1 = JsonPath.read(responseJson, "$[0].coordinator.id");
        Long responseCoordinatorid_1 = responseId_1.longValue();

        String responseTitle_2 = JsonPath.read(responseJson, "$[1].title");
        Integer responseId_2 = JsonPath.read(responseJson, "$[1].coordinator.id");
        Long responseCoordinatorid_2 = responseId_2.longValue();

        assertEquals(classroomTitle_1, responseTitle_1);
        assertEquals(classroomTitle_2, responseTitle_2);
        assertEquals(coordinator_1.getId(), responseCoordinatorid_1);
        assertEquals(coordinator_2.getId(), responseCoordinatorid_2);
    }

    @Disabled
    @Test
    public void testUpdateClassroom_Success() throws Exception {
        Long coordinatorId = 2L;
        Coordinator coordinator = new Coordinator();
        coordinator.setId(coordinatorId);

        String classroomTitle = "Spring Boot Maio 123";
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(classroomTitle, coordinatorId);
        Long classroomId = 1L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(put("/classrooms/" + classroomId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(classroomTitle))
                .andExpect(jsonPath("$.coordinator.id").value(2L))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseTitle = JsonPath.read(responseJson, "$.title");
        Integer responseId = JsonPath.read(responseJson, "$.coordinator.id");
        Long responseCoordinatorid = responseId.longValue();

        assertEquals(classroomTitle, responseTitle);
        assertEquals(coordinator.getId(), responseCoordinatorid);
    }

    @Disabled
    @Test
    public void testDeleteClassroom_Success() throws Exception {
        Long coordinatorId = 1L;
        Coordinator coordinator = new Coordinator();
        coordinator.setId(coordinatorId);

        Long classroomId = 2L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(delete("/classrooms/" + classroomId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    // Helper method to convert object to JSON string
    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
