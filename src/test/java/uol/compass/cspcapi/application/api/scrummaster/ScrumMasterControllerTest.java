package uol.compass.cspcapi.application.api.scrummaster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import uol.compass.cspcapi.application.api.scrumMaster.ScrumMasterController;
import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static uol.compass.cspcapi.commons.ScrumMastersConstants.*;

@ExtendWith(MockitoExtension.class)
public class ScrumMasterControllerTest {
    @Mock
    private ScrumMasterService scrumMasterService;

    @InjectMocks
    private ScrumMasterController scrumMasterController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateScrumMaster_Success() {
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        CreateScrumMasterDTO scrumMasterDTO = new CreateScrumMasterDTO();
        scrumMasterDTO.setUser(user);

        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseScrumMasterDTO expectedScrumMaster = new ResponseScrumMasterDTO(1L, expectedUser);

        when(scrumMasterService.save(any(CreateScrumMasterDTO.class))).thenReturn(expectedScrumMaster);

        ResponseEntity<ResponseScrumMasterDTO> responseEntity = scrumMasterController.save(scrumMasterDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedScrumMaster.getId(), responseEntity.getBody().getId());
        assertEquals(expectedScrumMaster.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testCreateScrumMaster_Error() {
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        CreateScrumMasterDTO scrumMasterDTO = new CreateScrumMasterDTO();
        scrumMasterDTO.setUser(user);

        when(scrumMasterService.save(any(CreateScrumMasterDTO.class))).thenThrow(new RuntimeException("Error ocurred while saving scrum master"));

        assertThrows(RuntimeException.class, () -> scrumMasterController.save(scrumMasterDTO));
    }

    @Test
    public void testGetScrumMasterById_Success() {
        Long scrumMasterId = 1L;
        ResponseUserDTO expectedUser = new ResponseUserDTO(1L, "First", "Second", "first.second@mail.com");
        ResponseScrumMasterDTO expectedScrumMaster = new ResponseScrumMasterDTO(scrumMasterId, expectedUser);

        when(scrumMasterService.getById(anyLong())).thenReturn(expectedScrumMaster);

        ResponseEntity<ResponseScrumMasterDTO> responseEntity = scrumMasterController.getById(scrumMasterId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedScrumMaster.getId(), responseEntity.getBody().getId());
        assertEquals(expectedScrumMaster.getUser(), responseEntity.getBody().getUser());
    }

    @Test
    public void testGetScrumMasterById_NotFound() {
        Long scrumMasterId = 999L;

        when(scrumMasterService.getById(anyLong())).thenReturn(null);

        // Performing the test and verifying the exception
        try {
            scrumMasterController.getById(scrumMasterId);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testGetAllScrumMasters_Success() {
        List<ResponseScrumMasterDTO> expectedScrumMasters = new ArrayList<>();
        expectedScrumMasters.addAll(List.of(RESPONSE_SCRUMMASTER_1, RESPONSE_SCRUMMASTER_2, RESPONSE_SCRUMMASTER_3));

        when(scrumMasterService.getAll()).thenReturn(expectedScrumMasters);

        ResponseEntity<List<ResponseScrumMasterDTO>> responseEntity = scrumMasterController.getAllScrumMaster();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedScrumMasters.size(), responseEntity.getBody().size());
        assertEquals(expectedScrumMasters, responseEntity.getBody());
    }

    @Test
    public void testGetAllScrumMasters_EmptyList() {
        // Mocking the behavior of the ClassroomService.getAllClassrooms method for empty list scenario
        List<ResponseScrumMasterDTO> expectedScrumMasters = new ArrayList<>();

        when(scrumMasterService.getAll()).thenReturn(expectedScrumMasters);

        // Performing the test
        ResponseEntity<List<ResponseScrumMasterDTO>> responseEntity = scrumMasterController.getAllScrumMaster();

        // Asserting the response status code and the returned list of classrooms (should be an empty list)
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedScrumMasters.size(), responseEntity.getBody().size());
        assertEquals(expectedScrumMasters, responseEntity.getBody());
    }

    @Test
    public void testUpdateScrumMaster_Success() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for success scenario
        Long scrumMasterId = 1L;
        User user = new User(
                RESPONSE_USER_1.getFirstName(),
                RESPONSE_USER_1.getLastName(),
                RESPONSE_USER_1.getEmail(),
                "password"
        );
        UpdateScrumMasterDTO scrumMasterDTO = new UpdateScrumMasterDTO();
        scrumMasterDTO.setUser(user);

        ScrumMaster updatedScrumMaster = new ScrumMaster();
        updatedScrumMaster.setId(scrumMasterId);
        updatedScrumMaster.setUser(scrumMasterDTO.getUser());

        when(scrumMasterService.update(anyLong(), any(UpdateScrumMasterDTO.class))).thenReturn(RESPONSE_SCRUMMASTER_1);

        // Performing the test
        ResponseEntity<ResponseScrumMasterDTO> responseEntity = scrumMasterController.updateScrumMaster(scrumMasterId, scrumMasterDTO);

        // Asserting the response status code and the returned Classroom object
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedScrumMaster.getId(), responseEntity.getBody().getId());
        assertEquals(updatedScrumMaster.getUser().getFirstName(), responseEntity.getBody().getUser().getFirstName());
        assertEquals(updatedScrumMaster.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
        assertEquals(updatedScrumMaster.getUser().getLastName(), responseEntity.getBody().getUser().getLastName());
    }

    @Test
    public void testUpdateScrumMaster_NotFound() {
        // Mocking the behavior of the ClassroomService.updateClassroom method for scenario where classroom is not found
        Long scrumMasterId = 999L;
        User user = new User("First", "Second", "first.second@mail.com", "first.second");
        UpdateScrumMasterDTO scrumMasterDTO = new UpdateScrumMasterDTO();
        scrumMasterDTO.setUser(user);

        when(scrumMasterService.update(anyLong(), any(UpdateScrumMasterDTO.class))).thenReturn(null);

        try {
            scrumMasterController.updateScrumMaster(scrumMasterId, scrumMasterDTO);
        } catch (ResponseStatusException exception) {
            // Asserting the status code of the exception
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    public void testDelete_Success() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for success scenario
        Long scrumMasterId = 1L;

        doNothing().when(scrumMasterService).delete(scrumMasterId);

        // Performing the test
        ResponseEntity<Void> responseEntity = scrumMasterController.delete(scrumMasterId);

        // Asserting the response status code (should be HTTP 204 No Content)
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() {
        // Mocking the behavior of the ClassroomService.deleteClassroom method for scenario where classroom is not found
        Long scrumMasterId = 999L;

        doThrow(ResponseStatusException.class).when(scrumMasterService).delete(scrumMasterId);

        // Performing the test and verifying the exception
        try {
            scrumMasterController.delete(scrumMasterId);
        } catch (ResponseStatusException exception) {
            // Assertion for ResourceNotFoundException
            assertEquals(ResponseStatusException.class, exception.getClass());
        }
    }
}
