package id.co.app.model.dto;

public class SuccessResponseDto extends ResponseApiDto{
    public SuccessResponseDto() {
        super();
    }

    public void setData(Object data) {
        this.getResponse().setData(data);
    }

    public void setPaging(Object paging) {
        this.getResponse().setPaging(paging);
    }

    public void setMessage(String message) {
        this.getResponse().setMessage(message);
    }
}
