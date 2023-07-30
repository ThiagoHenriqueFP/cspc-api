package uol.compass.cspcapi.application.api.generalPourposeDTO;

import java.util.Date;

public class ResponseDTO<T> {
    private Date timestamp;
    private T data;

    public ResponseDTO(T data) {
        this.timestamp = new Date();
        this.data = data;
    }

    public static <T> ResponseDTO<T> ok(T data){
        return new ResponseDTO<>(data);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }
}