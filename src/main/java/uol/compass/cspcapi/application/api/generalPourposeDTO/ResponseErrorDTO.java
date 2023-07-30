package uol.compass.cspcapi.application.api.generalPourposeDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseErrorDTO {
    private Date timestamp;
    private List<String> messages;

    public ResponseErrorDTO(List<String> messages) {
        this.messages = new ArrayList<>();
        this.messages.addAll(messages);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public List<String> getMessages() {
        return messages;
    }
}
