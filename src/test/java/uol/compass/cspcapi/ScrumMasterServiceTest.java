
package uol.compass.cspcapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMaster;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterRepository;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.domain.role.RoleRepository;
import uol.compass.cspcapi.domain.role.RoleService;

@SpringBootTest
public class ScrumMasterServiceTest {

	@Autowired
	private RoleRepository roleRepository;
	private ScrumMasterService scrumMasterService;
	@Autowired
	private ScrumMasterRepository scrumMasterRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	private PasswordEncoder passwordEncrypt;

	private PasswordEncoder passwordEncoder;
	private List<Long> idList;

//	@BeforeEach
//	public void setup() {
//		passwordEncrypt = new PasswordEncoder();
//		passwordEncoder = new PasswordEncoder();
//		roleService = new RoleService(roleRepository);
//		userService = new UserService(userRepository, passwordEncoder);
//		scrumMasterService = new ScrumMasterService(scrumMasterRepository, userService, passwordEncrypt, roleService);
//		idList = new ArrayList<>();
//	}

	@AfterEach
	public void clean() {
		for (int i = 0; i < idList.size(); i++) {
			if (!idList.isEmpty()) {
				scrumMasterService.delete(idList.get(i));
				userRepository.deleteById(idList.get(i));
			}
		}
		userRepository.deleteAll();
		scrumMasterRepository.deleteAll();
	}

	@Test
	public void saveScrumMasterSucess() {
		User user = new User("user2Test", "test", "test2@mail", "JHHGF1");
		CreateScrumMasterDTO scrumMaster = new CreateScrumMasterDTO(user);
		ResponseScrumMasterDTO response = scrumMasterService.save(scrumMaster);
		if (response != null) {
			idList.add(response.getId());
		}
		Assertions.assertThat(response).isNotEqualTo(null);
	}

	@Test
	public void saveScrumMasterFailureFieldClean() {
		User user = new User("userTest3", "", "test3@mail", "");
		CreateScrumMasterDTO scrumMaster = new CreateScrumMasterDTO(user);
		ResponseScrumMasterDTO response = null;
		try {
			response = scrumMasterService.save(scrumMaster);
			
		} catch (ResponseStatusException e) {
			Assertions.assertThat(response).isEqualTo(null);
		}
	}
	
	@Test
	public void deleteScrumMasterSucess() {
		User user = new User("user3Test", "test", "test3@mail", "JHHGF1");
		CreateScrumMasterDTO scrumMaster = new CreateScrumMasterDTO(user);
		ResponseScrumMasterDTO response = scrumMasterService.save(scrumMaster);
		scrumMasterService.delete(response.getId());
		Optional<ScrumMaster> scrunMaster = scrumMasterRepository.findById(response.getId());

		Assertions.assertThat(scrunMaster).isEqualTo(null);
	}
	
	@Test
	public void deleteScrumMasterFailure() {
		User user = new User("user2Test", "test", "test2@mail", "JHHGF1");
		CreateScrumMasterDTO scrumMaster = new CreateScrumMasterDTO(user);
		ResponseScrumMasterDTO response = scrumMasterService.save(scrumMaster);
		if (response != null) {
			idList.add(response.getId());
		}
		scrumMasterService.delete( - response.getId());
		ResponseScrumMasterDTO  aux = scrumMasterService.getById(response.getId());
		Assertions.assertThat(aux).isNotEqualTo(null);
	}
	
	@Test
	public void seachScrunMaster() {
		
	}

}
