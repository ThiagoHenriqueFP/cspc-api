package uol.compass.cspcapi.integrationTests.student;

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
import uol.compass.cspcapi.application.api.classroom.dto.UpdateClassroomDTO;
import uol.compass.cspcapi.application.api.grade.dto.UpdateGradeDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.application.api.student.dto.CreateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTO;
import uol.compass.cspcapi.application.api.student.dto.UpdateStudentDTOTest;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.UpdateUserDTO;
import uol.compass.cspcapi.domain.grade.Grade;
import uol.compass.cspcapi.domain.student.Student;
import uol.compass.cspcapi.domain.user.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class StudentControllerIT {
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
    public void testCreateStudent_Success() throws Exception {
        String firstName = "User1";
        String lastName = "Surname";
        String email = "user1surname123@mail.com";
        String password = "password";

        CreateStudentDTO studentDTO = new CreateStudentDTO(
                new CreateUserDTO(firstName, lastName, email, password)
        );

        String authToken = login();

        MvcResult result = this.mockMvc.perform(post("/students")
                        .header("Authorization", "Bearer " + authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(studentDTO)))
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
    public void testGetByIdStudent_Success() throws Exception {
        String firstName = "Carolina";
        String lastName = "Koike";
        String email = "carolina.koike.pb@compasso.com.br";
        Long studentId = 4L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/students/" + studentId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.user.firstName").value(firstName))
                .andExpect(jsonPath("$.user.lastName").value(lastName))
                .andExpect(jsonPath("$.user.email").value(email))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Integer responseId = JsonPath.read(responseJson, "$.id");
        Long responseStudentid = responseId.longValue();

        String responseFirstName = JsonPath.read(responseJson, "$.user.firstName");
        String responseLastName = JsonPath.read(responseJson, "$.user.lastName");
        String responseEmail = JsonPath.read(responseJson, "$.user.email");


        assertEquals(studentId, responseStudentid);
        assertEquals(firstName, responseFirstName);
        assertEquals(lastName, responseLastName);
        assertEquals(email, responseEmail);
    }

    @Disabled
    @Test
    public void testGetAllStudents_Success() throws Exception {
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
    public void testUpdateStudent_Success() throws Exception {
        Long studentId = 2L;
        UpdateUserDTO updatedUser = new UpdateUserDTO("Update", "User", "updatedemail1@mail.com", "udpatedpassword");
        UpdateStudentDTO scrumMasterDTO = new UpdateStudentDTO(updatedUser);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(put("/students/" + studentId)
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
        Long studentId = 2L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(delete("/students/" + studentId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Disabled
    @Test
    public void testUpdateGradesFromStudent_Success() throws Exception {
        Double communication = 6.4;
        Double collaboration = 6.7;
        Double autonomy = 7.5;
        Double quiz = 6.8;
        Double individualChallenge = 8.7;
        Double squadChallenge = 10.00;
        Double finalGrade = ((communication + collaboration + autonomy + quiz + individualChallenge + squadChallenge) / 6 );

        UpdateGradeDTO grades = new UpdateGradeDTO(
                communication,
                collaboration,
                autonomy,
                quiz,
                individualChallenge,
                squadChallenge
        );

        Long studentId = 1L;
        UpdateStudentDTO studentDTO = new UpdateStudentDTO(grades);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/students/" + studentId + "/update-grades")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grades.communication").value(communication))
                .andExpect(jsonPath("$.grades.collaboration").value(collaboration))
                .andExpect(jsonPath("$.grades.autonomy").value(autonomy))
                .andExpect(jsonPath("$.grades.quiz").value(quiz))
                .andExpect(jsonPath("$.grades.individualChallenge").value(individualChallenge))
                .andExpect(jsonPath("$.grades.squadChallenge").value(squadChallenge))
                .andExpect(jsonPath("$.grades.finalGrade").value(finalGrade))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        Double studentGradeCommunication = JsonPath.read(responseJson, "$.grades.communication");
        Double studentGradeCollaboration = JsonPath.read(responseJson, "$.grades.collaboration");
        Double studentGradeAutonomy = JsonPath.read(responseJson, "$.grades.autonomy");
        Double studentGradeQuiz = JsonPath.read(responseJson, "$.grades.quiz");
        Double studentGradeIndividualChallenge = JsonPath.read(responseJson, "$.grades.individualChallenge");
        Double studentGradeSquadChallenge = JsonPath.read(responseJson, "$.grades.squadChallenge");
        Double studentGradeFinalGrade = JsonPath.read(responseJson, "$.grades.finalGrade");

        assertEquals(communication, studentGradeCommunication, 0.01);
        assertEquals(collaboration, studentGradeCollaboration, 0.01);
        assertEquals(autonomy, studentGradeAutonomy, 0.01);
        assertEquals(quiz, studentGradeQuiz, 0.01);
        assertEquals(individualChallenge, studentGradeIndividualChallenge, 0.01);
        assertEquals(squadChallenge, studentGradeSquadChallenge, 0.01);
        assertEquals(finalGrade, studentGradeFinalGrade, 0.01);
    }


    // Helper method to convert object to JSON string
    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
