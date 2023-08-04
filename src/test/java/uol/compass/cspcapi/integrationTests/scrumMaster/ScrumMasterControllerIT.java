package uol.compass.cspcapi.integrationTests.scrumMaster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uol.compass.cspcapi.application.api.auth.dto.LoginDTO;
import uol.compass.cspcapi.application.api.classroom.dto.CreateClassroomDTO;
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.instructor.Instructor;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ScrumMasterControllerIT {
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
    public void testCreateScrumMaster_Success() throws Exception {
        String firstName = "User1";
        String lastName = "Surname";
        String email = "user1surname@mail.com";
        String password = "password";

        CreateScrumMasterDTO scrumMasterDTO = new CreateScrumMasterDTO(
                new User(firstName, lastName, email, password)
        );

        String authToken = login();

        MvcResult result = this.mockMvc.perform(post("/scrummasters")
                        .header("Authorization", "Bearer " + authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(scrumMasterDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.firstName").value(firstName))
                .andExpect(jsonPath("$.user.lastName").value(lastName))
                .andExpect(jsonPath("$.user.email").value(email))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");

        assertEquals(firstName, responseFirstName);
        assertEquals(lastName, responseLastName);
        assertEquals(email, responseEmail);
    }

    @Disabled
    @Test
    public void testGetByIdScrumMaster_Success() throws Exception {
        String firstName = "Gilberto";
        String lastName = "Medeiros";
        String email = "gilberto.medeiros@compasso.com.br";
        Long scrumMasterId = 1L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/scrummasters/" + scrumMasterId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scrumMasterId))
                .andExpect(jsonPath("$.user.firstName").value(firstName))
                .andExpect(jsonPath("$.user.lastName").value(lastName))
                .andExpect(jsonPath("$.user.email").value(email))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer responseId = JsonPath.read(responseJson, "$.id");
        Long responseScrumMasterid = responseId.longValue();

        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");


        assertEquals(scrumMasterId, responseScrumMasterid);
        assertEquals(firstName, responseFirstName);
        assertEquals(lastName, responseLastName);
        assertEquals(email, responseEmail);
    }

    @Disabled
    @Test
    public void testGetAllScrumMasters_Success() throws Exception {
        User user_1 = new User("", "", "", "");
        User user_2 = new User("", "", "", "");

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/scrummasters")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.email").value(user_1.getEmail()))
                .andExpect(jsonPath("$[1].user.email").value(user_2.getEmail()))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        String responseEmail_1 = JsonPath.read(responseJson, "$[0].user.email");
        String responseEmail_2 = JsonPath.read(responseJson, "$[1].user.email");

        assertEquals(user_1.getEmail(), responseEmail_1);
        assertEquals(user_2.getEmail(), responseEmail_2);
    }

    @Disabled
    @Test
    public void testUpdateScrumMaster_Success() throws Exception {
        Long scrumMasterId = 2L;
        User updatedUser = new User("Update", "User", "updatedemail@mail.com", "udpatedpassword");
        UpdateScrumMasterDTO scrumMasterDTO = new UpdateScrumMasterDTO(updatedUser);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(put("/scrummasters/" + scrumMasterId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(scrumMasterDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.firstName").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.user.lastName").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.user.email").value(updatedUser.getEmail()))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");


        assertEquals(updatedUser.getFirstName(), responseFirstName);
        assertEquals(updatedUser.getLastName(), responseLastName);
        assertEquals(updatedUser.getEmail(), responseEmail);
    }

    @Disabled
    @Test
    public void testDeleteScrumMaster_Success() throws Exception {
        Long scrumMasterId = 2L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(delete("/scrummasters/" + scrumMasterId)
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
