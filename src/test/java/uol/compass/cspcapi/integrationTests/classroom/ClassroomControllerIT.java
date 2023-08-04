package uol.compass.cspcapi.integrationTests.classroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
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
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.classroom.ClassroomService;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.infrastructure.config.auth.SecurityConfig;
import uol.compass.cspcapi.infrastructure.config.jwt.JwtTokenFilter;
import uol.compass.cspcapi.infrastructure.config.jwt.JwtTokenProvider;
import uol.compass.cspcapi.infrastructure.config.userDetails.UserDetailsImpl;
import uol.compass.cspcapi.infrastructure.config.userDetails.UserDetilsServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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

    @Disabled
    @Test
    public void testAddStudentsToClassroom_Success() throws Exception {
        Student student_1 = new Student();
        student_1.setId(1L);
        Student student_2 = new Student();
        student_2.setId(2L);
        Student student_3 = new Student();
        student_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/add-students")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students[0].id").value(generalUsersIds.get(0)))
                .andExpect(jsonPath("$.students[1].id").value(generalUsersIds.get(1)))
                .andExpect(jsonPath("$.students[2].id").value(generalUsersIds.get(2)))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer studentJson_1 = JsonPath.read(responseJson, "$.students[0].id");
        Long studentId_1 = studentJson_1.longValue();
        Integer studentJson_2 = JsonPath.read(responseJson, "$.students[1].id");
        Long studentId_2 = studentJson_2.longValue();
        Integer studentJson_3 = JsonPath.read(responseJson, "$.students[2].id");
        Long studentId_3 = studentJson_3.longValue();

        assertEquals(generalUsersIds.get(0), studentId_1);
        assertEquals(generalUsersIds.get(1), studentId_2);
        assertEquals(generalUsersIds.get(2), studentId_3);
    }

    @Disabled
    @Test
    public void testRemoveStudentsFromClassroom_Success() throws Exception {
        Student student_1 = new Student();
        student_1.setId(1L);
        Student student_2 = new Student();
        student_2.setId(2L);
        Student student_3 = new Student();
        student_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/remove-students")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students").isEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        List studentJson = JsonPath.read(responseJson, "$.students");

        List empty = new ArrayList<>();
        assertEquals(empty, studentJson);
    }

    @Disabled
    @Test
    public void testAddInstructorsToClassroom_Success() throws Exception {
        Instructor instructor_1 = new Instructor();
        instructor_1.setId(1L);
        Instructor instructor_2 = new Instructor();
        instructor_2.setId(2L);
        Instructor instructor_3 = new Instructor();
        instructor_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/add-instructors")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructors[0].id").value(generalUsersIds.get(0)))
                .andExpect(jsonPath("$.instructors[1].id").value(generalUsersIds.get(1)))
                .andExpect(jsonPath("$.instructors[2].id").value(generalUsersIds.get(2)))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer instructorJson_1 = JsonPath.read(responseJson, "$.instructors[0].id");
        Long instructorId_1 = instructorJson_1.longValue();
        Integer instructorJson_2 = JsonPath.read(responseJson, "$.instructors[1].id");
        Long instructorId_2 = instructorJson_2.longValue();
        Integer instructorJson_3 = JsonPath.read(responseJson, "$.instructors[2].id");
        Long instructorId_3 = instructorJson_3.longValue();

        assertEquals(generalUsersIds.get(0), instructorId_1);
        assertEquals(generalUsersIds.get(1), instructorId_2);
        assertEquals(generalUsersIds.get(2), instructorId_3);
    }

    @Disabled
    @Test
    public void testRemoveInstructorsFromClassroom_Success() throws Exception {
        Instructor instructor_1 = new Instructor();
        instructor_1.setId(1L);
        Instructor instructor_2 = new Instructor();
        instructor_2.setId(2L);
        Instructor instructor_3 = new Instructor();
        instructor_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/remove-instructors")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instructors").isEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        List instructorJson = JsonPath.read(responseJson, "$.instructors");

        List empty = new ArrayList<>();
        assertEquals(empty, instructorJson);
    }

    @Disabled
    @Test
    public void testAddScrumMastersToClassroom_Success() throws Exception {
        ScrumMaster scrumMaster_1 = new ScrumMaster();
        scrumMaster_1.setId(1L);
        ScrumMaster scrumMaster_2 = new ScrumMaster();
        scrumMaster_2.setId(2L);
        ScrumMaster scrumMaster_3 = new ScrumMaster();
        scrumMaster_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/add-scrummasters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scrumMasters[0].id").value(generalUsersIds.get(0)))
                .andExpect(jsonPath("$.scrumMasters[1].id").value(generalUsersIds.get(1)))
                .andExpect(jsonPath("$.scrumMasters[2].id").value(generalUsersIds.get(2)))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer scrumMasterJson_1 = JsonPath.read(responseJson, "$.scrumMasters[0].id");
        Long scrumMasterId_1 = scrumMasterJson_1.longValue();
        Integer scrumMasterJson_2 = JsonPath.read(responseJson, "$.scrumMasters[1].id");
        Long scrumMasterId_2 = scrumMasterJson_2.longValue();
        Integer scrumMasterJson_3 = JsonPath.read(responseJson, "$.scrumMasters[2].id");
        Long scrumMasterId_3 = scrumMasterJson_3.longValue();

        assertEquals(generalUsersIds.get(0), scrumMasterId_1);
        assertEquals(generalUsersIds.get(1), scrumMasterId_2);
        assertEquals(generalUsersIds.get(2), scrumMasterId_3);
    }

    @Disabled
    @Test
    public void testRemoveScrumMastersFromClassroom_Success() throws Exception {
        ScrumMaster scrumMaster_1 = new ScrumMaster();
        scrumMaster_1.setId(1L);
        ScrumMaster scrumMaster_2 = new ScrumMaster();
        scrumMaster_2.setId(2L);
        ScrumMaster scrumMaster_3 = new ScrumMaster();
        scrumMaster_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/remove-scrummasters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scrumMasters").isEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        List scrumMasterJson = JsonPath.read(responseJson, "$.scrumMasters");

        List empty = new ArrayList<>();
        assertEquals(empty, scrumMasterJson);
    }

    @Disabled
    @Test
    public void testAddSquadsToClassroom_Success() throws Exception {
        Squad squad_1 = new Squad();
        squad_1.setId(1L);
        Squad squad_2 = new Squad();
        squad_2.setId(2L);
        Squad squad_3 = new Squad();
        squad_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/add-squads")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.squads[0].id").value(generalUsersIds.get(0)))
                .andExpect(jsonPath("$.squads[1].id").value(generalUsersIds.get(1)))
                .andExpect(jsonPath("$.squads[2].id").value(generalUsersIds.get(2)))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer squadJson_1 = JsonPath.read(responseJson, "$.squads[0].id");
        Long squadId_1 = squadJson_1.longValue();
        Integer squadJson_2 = JsonPath.read(responseJson, "$.squads[1].id");
        Long squadId_2 = squadJson_2.longValue();
        Integer squadJson_3 = JsonPath.read(responseJson, "$.squads[2].id");
        Long squadId_3 = squadJson_3.longValue();

        assertEquals(generalUsersIds.get(0), squadId_1);
        assertEquals(generalUsersIds.get(1), squadId_2);
        assertEquals(generalUsersIds.get(2), squadId_3);
    }

    @Disabled
    @Test
    public void testRemoveSquadsFromClassroom_Success() throws Exception {
        Squad squad_1 = new Squad();
        squad_1.setId(1L);
        Squad squad_2 = new Squad();
        squad_2.setId(2L);
        Squad squad_3 = new Squad();
        squad_3.setId(3L);

        Long classroomId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateClassroomDTO classroomDTO = new UpdateClassroomDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/classrooms/" + classroomId + "/remove-squads")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(classroomDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.squads").isEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        List squadsJson = JsonPath.read(responseJson, "$.squads");

        List empty = new ArrayList<>();
        assertEquals(empty, squadsJson);
    }


    // Helper method to convert object to JSON string
    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
