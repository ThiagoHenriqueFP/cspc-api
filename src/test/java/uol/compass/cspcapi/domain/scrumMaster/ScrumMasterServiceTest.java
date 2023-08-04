
package uol.compass.cspcapi.domain.scrumMaster;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.tool.schema.spi.SqlScriptException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.UpdateScrumMasterDTO;
import uol.compass.cspcapi.domain.classroom.Classroom;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class ScrumMasterServiceTest {

	private static final User USER_1 = new User("first", "last", "test@mail.com", "12345678");
	private static final ScrumMaster SCRUM_MASTER_1 = new ScrumMaster(USER_1);
	private static final Role ROLE_1 = new Role("ROLE_SCRUM_MASTER");

	private static final Classroom CLASSROOM_1 = new Classroom("classroom");

	private static UserRepository userRepository;
	private static RoleRepository roleRepository;
	private static ScrumMasterRepository scrumMasterRepository;
	private static ScrumMasterService scrumMasterService;

	@BeforeAll
	public static void setup() {
		ROLE_1.setId(1L);

		USER_1.setId(1L);
		USER_1.getRoles().add(ROLE_1);

		SCRUM_MASTER_1.setId(1L);

		PasswordEncoder passwordEncoder = new PasswordEncoder();

		userRepository = mock(UserRepository.class);
		scrumMasterRepository = mock(ScrumMasterRepository.class);
		roleRepository = mock(RoleRepository.class);

		RoleService roleService = new RoleService(roleRepository);
		UserService userService = new UserService(userRepository, passwordEncoder);
		scrumMasterService = new ScrumMasterService(scrumMasterRepository, userService, passwordEncoder, roleService);
	}

	@AfterEach
	public void clean() {
		reset(userRepository);
		reset(roleRepository);
		reset(scrumMasterRepository);
	}

	@Test
	public void saveScrumMasterSuccess() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(roleRepository.findByName(anyString())).thenReturn(Optional.of(ROLE_1));
		when(scrumMasterRepository.save(any(ScrumMaster.class))).thenReturn(SCRUM_MASTER_1);

		ResponseScrumMasterDTO response = scrumMasterService.save(createScrumMasterDTO());

		assertNotNull(response);
		assertEquals(USER_1.getFirstName(), response.getUser().getFirstName());
		assertEquals(SCRUM_MASTER_1.getId(), response.getId());
	}

	@Test
	void saveScrumMasterFailUserAlreadyExists(){
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(USER_1));

		ResponseStatusException response = assertThrows(ResponseStatusException.class,
				() -> scrumMasterService.save(createScrumMasterDTO())
				);

		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());
		assertEquals("user already exists", response.getReason());
	}

	@Test
	void saveScrumMasterFailRoleNotExists(){
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

		ResponseStatusException response = assertThrows(ResponseStatusException.class,
				() -> scrumMasterService.save(createScrumMasterDTO())
		);

		assertNotNull(response);
		assertEquals(404, response.getStatusCode().value());
		assertEquals("this role ROLE_SCRUM_MASTER not exists", response.getReason());
	}

	@Test
	void getByIdSuccess() {
		when(scrumMasterRepository.findById(anyLong())).thenReturn(Optional.of(SCRUM_MASTER_1));

		ResponseScrumMasterDTO response = scrumMasterService.getById(anyLong());

		verify(scrumMasterRepository).findById(anyLong());
		assertNotNull(response);
		assertEquals(SCRUM_MASTER_1.getId(), response.getId());
	}

	@Test
	void getByIdFail() {
		when(scrumMasterRepository.findById(anyLong())).thenReturn(Optional.empty());

		ResponseStatusException response = assertThrows(ResponseStatusException.class,
				() -> scrumMasterService.getById(anyLong())
		);

		verify(scrumMasterRepository).findById(anyLong());
		assertNotNull(response);
		assertEquals(404, response.getStatusCode().value());
		assertEquals("scrum master not found", response.getReason());
	}

	@Test
	void getAllSuccess() {
		// duplicated only to compare size
		List<ScrumMaster> list = List.of(SCRUM_MASTER_1, SCRUM_MASTER_1);
		when(scrumMasterRepository.findAll()).thenReturn(list);

		List<ResponseScrumMasterDTO> response = scrumMasterService.getAll();

		assertNotNull(response);
		assertEquals(list.size(), response.size());
	}

	@Test
	void getAllFail() {
		// duplicated only to compare size
		List<ScrumMaster> list = List.of(SCRUM_MASTER_1, SCRUM_MASTER_1);
		when(scrumMasterRepository.findAll()).thenReturn(List.of(SCRUM_MASTER_1));

		List<ResponseScrumMasterDTO> response = scrumMasterService.getAll();

		assertNotNull(response);
		assertNotEquals(list.size(), response.size());
	}

	@Test
	void updateSuccess() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(roleRepository.findByName(anyString())).thenReturn(Optional.of(ROLE_1));
		when(scrumMasterRepository.findById(anyLong())).thenReturn(Optional.of(SCRUM_MASTER_1));
		when(scrumMasterRepository.save(any(ScrumMaster.class))).thenReturn(SCRUM_MASTER_1);

		ResponseScrumMasterDTO response = scrumMasterService.update(1L, updateScrumMasterDTO());

		assertNotNull(response);
		assertEquals(USER_1.getFirstName(), response.getUser().getFirstName());
		assertEquals(SCRUM_MASTER_1.getId(), response.getId());
	}

	@Test
	void updateFailNotFound() {
		when(scrumMasterRepository.findById(anyLong())).thenReturn(Optional.empty());

		ResponseStatusException response = assertThrows(ResponseStatusException.class,
				() ->  scrumMasterService.update(1L, updateScrumMasterDTO())
				);


		assertNotNull(response);
		assertEquals(404, response.getStatusCode().value());
	}

	@Test
	void updateFail() {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		when(roleRepository.findByName(anyString())).thenReturn(Optional.of(ROLE_1));
		when(scrumMasterRepository.findById(anyLong())).thenReturn(Optional.of(SCRUM_MASTER_1));
		when(scrumMasterRepository.save(any(ScrumMaster.class))).thenThrow(SqlScriptException.class);

		SqlScriptException response = assertThrows(SqlScriptException.class,
				() -> scrumMasterService.update(1L, updateScrumMasterDTO())
				);

		assertNotNull(response);
	}

	@Test
	void delete() {
		when(scrumMasterRepository.findById(anyLong())).thenReturn(Optional.of(SCRUM_MASTER_1));

		scrumMasterRepository.delete(SCRUM_MASTER_1);

		verify(scrumMasterRepository).delete(SCRUM_MASTER_1);
	}

	@Test
	void deleteFailNotFound() {
		when(scrumMasterRepository.findById(anyLong())).thenReturn(Optional.empty());

		ResponseStatusException response = assertThrows(ResponseStatusException.class,
				() ->  scrumMasterService.delete(1L)
		);

		verify(scrumMasterRepository).findById(anyLong());
		assertEquals(404, response.getStatusCode().value());
	}

	@Test
	void getAllScrumMastersById() {
		List<Long> list = List.of(1L);
		List<ScrumMaster> scrumMasters = List.of(SCRUM_MASTER_1);
		when(scrumMasterRepository.findAllByIdIn(list)).thenReturn(scrumMasters);

		List<ScrumMaster> response = scrumMasterService.getAllScrumMastersById(list);

		assertNotNull(response);
		assertEquals(scrumMasters.size(), response.size());
	}

	@Test
	void getAllScrumMastersByIdFail() {
		List<Long> list = List.of(1L);
		when(scrumMasterRepository.findAllByIdIn(list)).thenReturn(Collections.emptyList());

		ResponseStatusException response = assertThrows(ResponseStatusException.class,
				() ->  scrumMasterService.getAllScrumMastersById(list)
		);

		assertNotNull(response);
		assertEquals(400, response.getStatusCode().value());
		assertEquals("one or more scrum masters not exists", response.getReason());
	}

	@Test
	void attributeScrumMastersToClassroom() {
		List<ScrumMaster> scrumMasters = List.of(SCRUM_MASTER_1);
		when(scrumMasterRepository.saveAll(scrumMasters)).thenReturn(scrumMasters);

		List<ResponseScrumMasterDTO> response = scrumMasterService.attributeScrumMastersToClassroom(CLASSROOM_1, scrumMasters);

		verify(scrumMasterRepository).saveAll(scrumMasters);
		assertNotNull(response);
		assertFalse(response.isEmpty());
	}

	@Test
	void attributeScrumMastersToClassroomFail() {
		List<ScrumMaster> scrumMasters = List.of(SCRUM_MASTER_1);
		when(scrumMasterRepository.saveAll(scrumMasters)).thenReturn(Collections.emptyList());

		List<ResponseScrumMasterDTO> response = scrumMasterService.attributeScrumMastersToClassroom(CLASSROOM_1, scrumMasters);

		verify(scrumMasterRepository).saveAll(scrumMasters);
		assertTrue(response.isEmpty());
	}

	@Test
	void mapToResponseScrumMaster() {
		ResponseScrumMasterDTO response = scrumMasterService.mapToResponseScrumMaster(SCRUM_MASTER_1);

		assertInstanceOf(ResponseScrumMasterDTO.class, response);
	}

	@Test
	void mapToResponseScrumMasters() {
		List<ResponseScrumMasterDTO> response = scrumMasterService.mapToResponseScrumMasters(List.of(SCRUM_MASTER_1));

		assertInstanceOf(List.class, response);
	}

	CreateScrumMasterDTO createScrumMasterDTO() {
		return new CreateScrumMasterDTO(
				USER_1
		);
	}

	UpdateScrumMasterDTO updateScrumMasterDTO() {
		return new UpdateScrumMasterDTO(
				USER_1,
				CLASSROOM_1
		);
	}
}
