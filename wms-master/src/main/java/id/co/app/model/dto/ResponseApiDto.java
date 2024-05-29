package id.co.app.model.dto;

import lombok.Getter;
import org.slf4j.MDC;

@Getter
public class ResponseApiDto {
    protected final String requestId = MDC.get("requestId");
}
