/*
package uol.compass.cspcapi;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import uol.compass.cspcapi.application.api.scrumMaster.dto.CreateScrumMasterDTO;
import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterRepository;
import uol.compass.cspcapi.domain.scrumMaster.ScrumMasterService;
import uol.compass.cspcapi.domain.user.User;
import uol.compass.cspcapi.domain.user.UserRepository;
import uol.compass.cspcapi.domain.user.UserService;
import uol.compass.cspcapi.infrastructure.config.passwordEncrypt.PasswordEncrypt;

@SpringBootTest
public class ScrumMasterServiceTest {

	private ScrumMasterService scrumMasterService;
	@Autowired
	private ScrumMasterRepository scrumMasterRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncrypt passwordEncrypt;
	private PasswordEncoder passwordEncoder;
	private List<Long> idList;

	@BeforeEach
	public void setup() {
		userService = new UserService(userRepository, passwordEncoder);
		scrumMasterService = new ScrumMasterService(scrumMasterRepository, userService, passwordEncrypt);
		idList = new ArrayList<>();
	}

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

//	@Test
//	public void saveScrumMasterFailureFieldClean() {
//		User user = new User("userTest3", "", "test3@mail", "");
//		CreateScrumMasterDTO scrumMaster = new CreateScrumMasterDTO(user);
//		ResponseScrumMasterDTO response = null;
//		boolean error = false;
//		try {
//			response = scrumMasterService.save(scrumMaster);
//		
//		} catch (ResponseStatusException e) {
//			error = true;
//		}
//		
//
//		Assertions.assertThat(error).isEqualTo(true);
//	}

}
*/