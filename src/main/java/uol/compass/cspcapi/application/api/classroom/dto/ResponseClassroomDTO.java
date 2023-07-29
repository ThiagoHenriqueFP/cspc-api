package uol.compass.cspcapi.application.api.classroom.dto;

public class ResponseClassroomDTO {

    private Long id;
    private String title;
    private Long coordinator;

    public ResponseClassroomDTO() {
    }

    public ResponseClassroomDTO(Long id, String title, Long coordinator) {
        this.id = id;
        this.title = title;
        this.coordinator = coordinator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Long coordinator) {
        this.coordinator = coordinator;
    }
}
