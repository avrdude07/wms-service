package id.co.app.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponseDto extends ResponseApiDto{
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object metaData;

    public SuccessResponseDto(Object data) {
        this.data = data;
    }
}
