package uol.compass.cspcapi.commons;

import uol.compass.cspcapi.application.api.scrumMaster.dto.ResponseScrumMasterDTO;
import uol.compass.cspcapi.application.api.user.dto.ResponseUserDTO;

public class ScrumMastersConstants {
    public static final ResponseUserDTO RESPONSE_USER_1 = new ResponseUserDTO(1L, "Scrum", "Master", "scrum.master1@mail.com");
    public static final ResponseScrumMasterDTO RESPONSE_SCRUMMASTER_1 = new ResponseScrumMasterDTO(1L, RESPONSE_USER_1);

    public static final ResponseUserDTO RESPONSE_USER_2 = new ResponseUserDTO(2L, "Scrum", "Master", "scrum.master2@mail.com");
    public static final ResponseScrumMasterDTO RESPONSE_SCRUMMASTER_2 = new ResponseScrumMasterDTO(2L, RESPONSE_USER_2);

    public static final ResponseUserDTO RESPONSE_USER_3 = new ResponseUserDTO(3L, "Scrum", "Master", "scrum.master3@mail.com");
    public static final ResponseScrumMasterDTO RESPONSE_SCRUMMASTER_3 = new ResponseScrumMasterDTO(3L, RESPONSE_USER_3);
}
