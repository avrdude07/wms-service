package id.co.app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class GeneralException  extends RuntimeException implements Serializable {
    private final transient Map<String, Object> errorMap;
}
