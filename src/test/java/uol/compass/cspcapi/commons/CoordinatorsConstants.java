package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.user.User;

public class CoordinatorsConstants {
    public static final User USER_1 = new User("User", "Surname", "user.surname1@mail.com", "user.surname1");
    public static final ResponseUserDTO RESPONSE_USER_1 = new ResponseUserDTO(1L, "User", "Surname", "user.surname1@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR_1 = new ResponseCoordinatorDTO(1L, RESPONSE_USER_1);

    public static final ResponseUserDTO RESPONSE_USER_2 = new ResponseUserDTO(2L, "User", "Surname", "user.surname2@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR_2 = new ResponseCoordinatorDTO(2L, RESPONSE_USER_2);

    public static final ResponseUserDTO RESPONSE_USER_3 = new ResponseUserDTO(3L, "User", "Surname", "user.surname3@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR_3 = new ResponseCoordinatorDTO(3L, RESPONSE_USER_3);
}
