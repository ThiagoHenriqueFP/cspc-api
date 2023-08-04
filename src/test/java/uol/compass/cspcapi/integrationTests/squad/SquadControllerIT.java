package uol.compass.cspcapi.integrationTests.squad;

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
import uol.compass.cspcapi.application.api.squad.dto.CreateSquadDTO;
import uol.compass.cspcapi.application.api.squad.dto.UpdateSquadDTO;
import uol.compass.cspcapi.domain.Squad.Squad;
import uol.compass.cspcapi.domain.student.Student;

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
public class SquadControllerIT {
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

    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }


    @Disabled
    @Test
    public void testCreateSquads_Success() throws Exception {
        CreateSquadDTO squad = new CreateSquadDTO();

        String squadName = "SpringForce";
        CreateSquadDTO squadDTO = new CreateSquadDTO(squadName);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(post("/squads")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(squadDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(squadName))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseName = JsonPath.read(responseJson, "$.name");

        assertEquals(squadName, responseName);
    }

    @Disabled
    @Test
    public void testGetByIdSquad_Success() throws Exception {
        String squadName = "Modern Bugs";
        Long squadId = 1L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/squads/" + squadId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(squadName))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseName = JsonPath.read(responseJson, "$.name");

        assertEquals(squadName, responseName);
    }

    @Disabled
    @Test
    public void testGetAllSquad_Success() throws Exception {
        String squadName_1 = "Modern bugs";
        String squadName_2 = "Modern bugs secundary";

        String authToken = login();

        MvcResult result = this.mockMvc.perform(get("/squads")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(squadName_1))
                .andExpect(jsonPath("$[1].name").value(squadName_2))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        String responseName_1 = JsonPath.read(responseJson, "$[0].name");
        String responseName_2 = JsonPath.read(responseJson, "$[1].name");

        assertEquals(squadName_1, responseName_1);
        assertEquals(squadName_2, responseName_2);
    }

    @Disabled
    @Test
    public void testUpdateSquad_Success() throws Exception {
        String squadName = "Spring force";
        UpdateSquadDTO squadDTO = new UpdateSquadDTO(squadName);
        Long squadId = 2L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(put("/squads/" + squadId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(squadDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(squadName))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String responseName = JsonPath.read(responseJson, "$.name");

        assertEquals(squadName, responseName);
    }

    @Disabled
    @Test
    public void testDeleteSquad_Success() throws Exception {
        Long squadId = 2L;

        String authToken = login();

        MvcResult result = this.mockMvc.perform(delete("/squads/" + squadId)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Disabled
    @Test
    public void testAddStudentsToSquad_Success() throws Exception {
        Student student_1 = new Student();
        student_1.setId(1L);
        Student student_2 = new Student();
        student_2.setId(2L);
        Student student_3 = new Student();
        student_3.setId(3L);

        Long squadId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateSquadDTO squadDTO = new UpdateSquadDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/squads/" + squadId + "/add-students")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(squadDTO)))
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
    public void testRemoveStudentsFromSquad_Success() throws Exception {
        Student student_1 = new Student();
        student_1.setId(1L);
        Student student_2 = new Student();
        student_2.setId(2L);
        Student student_3 = new Student();
        student_3.setId(3L);

        Long squadId = 1L;
        List<Long> generalUsersIds = Arrays.asList(1L, 2L, 3L);
        UpdateSquadDTO squadDTO = new UpdateSquadDTO(generalUsersIds);

        String authToken = login();

        MvcResult result = this.mockMvc.perform(patch("/squads/" + squadId + "/remove-students")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(squadDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.students").isEmpty())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        List studentJson = JsonPath.read(responseJson, "$.students");

        List empty = new ArrayList<>();
        assertEquals(empty, studentJson);
    }
}
