package id.co.app.model.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseApiDto {
    private int code;
    private String status;
    private Response response;

    public ResponseApiDto() {
        this.response = new Response();
    }

    public void setStatus(HttpStatus status) {
        this.code = status.value();
        this.status = status.getReasonPhrase();
    }
}
