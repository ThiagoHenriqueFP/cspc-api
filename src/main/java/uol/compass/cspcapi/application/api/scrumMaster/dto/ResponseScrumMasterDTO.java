package uol.compass.cspcapi.application.api.scrumMaster.dto;

public class ResponseScrumMasterDTO {
    private Long id;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    public ResponseScrumMasterDTO(Long id, String userFirstName, String userLastName, String userEmail) {
        this.id = id;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
