package id.co.app.model.dto;

public class ErrorResponseDto extends ResponseApiDto{
    public ErrorResponseDto() {
        super();
    }

    public ErrorResponseDto(Object errors) {
        super();
        this.getResponse().setErrors(errors);
    }

    public void setErrors(Object errors) {
        this.getResponse().setErrors(errors);
    }
}
