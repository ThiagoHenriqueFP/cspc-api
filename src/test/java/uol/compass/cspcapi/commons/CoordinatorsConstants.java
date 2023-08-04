package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.application.api.coordinator.dto.ResponseCoordinatorDTO;
import uol.compass.cspcapi.application.api.user.dto.CreateUserDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;
import uol.compass.cspcapi.domain.coordinator.Coordinator;
import uol.compass.cspcapi.domain.role.Role;
import uol.compass.cspcapi.domain.user.User;

public class CoordinatorsConstants {
    public static final User USER_1 = new User("User", "Surname", "user.surname1@mail.com", "user.surname1");
    public static final ResponseUserDTO RESPONSE_USER_1 = new ResponseUserDTO(1L, "User", "Surname", "user.surname1@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR_1 = new ResponseCoordinatorDTO(1L, RESPONSE_USER_1);

    public static final ResponseUserDTO RESPONSE_USER_2 = new ResponseUserDTO(2L, "User", "Surname", "user.surname2@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR_2 = new ResponseCoordinatorDTO(2L, RESPONSE_USER_2);

    public static final ResponseUserDTO RESPONSE_USER_3 = new ResponseUserDTO(3L, "User", "Surname", "user.surname3@mail.com");
    public static final ResponseCoordinatorDTO RESPONSE_COORDINATOR_3 = new ResponseCoordinatorDTO(3L, RESPONSE_USER_3);


    public static final User USER_4 = new User("primeiro", "segundo", "teste@mail.com", "12345678");
    public static final ResponseUserDTO USER_DTO_1 = new ResponseUserDTO(
            4L,
            USER_4.getFirstName(),
            USER_4.getLastName(),
            USER_4.getEmail()
    );
    public static final Coordinator COORDINATOR_1 = new Coordinator(USER_4);
    public static final ResponseCoordinatorDTO COORDINATOR_DTO_1 = new ResponseCoordinatorDTO(5L, USER_DTO_1);

    public static final User USER_5 = new User("primeiro", "segundo", "teste2@mail.com", "12345678");
    public static final Coordinator COORDINATOR_2 = new Coordinator(USER_5);

    public static final User USER_6 = new User("primeiro", "segundo", "teste3@mail.com", "12345678");
    public static final Coordinator COORDINATOR_3 = new Coordinator(USER_6);

    public static final Role ROLE_1 = new Role("ROLE_COORDINATOR");

    public static final CreateUserDTO USER_7 = new CreateUserDTO("firstName", "lastName", "test4@mail.com", "12345678");
}
