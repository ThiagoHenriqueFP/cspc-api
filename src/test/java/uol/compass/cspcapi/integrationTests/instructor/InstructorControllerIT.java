package uol.compass.cspcapi.integrationTests.instructor;

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
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;
import uol.compass.cspcapi.application.api.instructor.dto.CreateInstructorDTO;
import uol.compass.cspcapi.application.api.instructor.dto.UpdateInstructorDTO;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class InstructorControllerIT {
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
    public void testCreateInstructor_Success() throws Exception {
        String firstName = "User1";
        String lastName = "Surname";
        String email = "user1surname367@mail.com";
        String password = "password";

        CreateInstructorDTO instructorDTO = new CreateInstructorDTO(
                new User(firstName, lastName, email, password)
        );

        String authToken = login();

        MvcResult result = this.mockMvc.perform(post("/instructors")
                        .header("Authorization", "Bearer " + authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(instructorDTO)))
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
    public void testGetByIdInstructor_Success() throws Exception {
        String firstName = "User1";
        String lastName = "Surname";
        String email = "user1surname367@mail.com";
        Long instructorId = 4L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/instructors/" + instructorId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(instructorId))
                .andExpect(jsonPath("$.user.firstName").value(firstName))
                .andExpect(jsonPath("$.user.lastName").value(lastName))
                .andExpect(jsonPath("$.user.email").value(email))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer responseId = JsonPath.read(responseJson, "$.id");
        Long responseInstructorid = responseId.longValue();

        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");


        assertEquals(instructorId, responseInstructorid);
        assertEquals(firstName, responseFirstName);
        assertEquals(lastName, responseLastName);
        assertEquals(email, responseEmail);
    }

    @Disabled
    @Test
    public void testGetAllInstructors_Success() throws Exception {
        User user_1 = new User("", "", "", "");
        User user_2 = new User("", "", "", "");

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/students")
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
    public void testUpdateInstructor_Success() throws Exception {
        Long instructorId = 2L;
        User updatedUser = new User("Update", "User", "update21demail1@mail.com", "udpatedpassword");
        UpdateInstructorDTO instructorDTO = new UpdateInstructorDTO(updatedUser);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(put("/instructors/" + instructorId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(instructorDTO)))
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
    public void testDeleteInstructor_Success() throws Exception {
        Long instructorId = 1L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(delete("/instructors/" + instructorId)
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
