package id.co.app.constant;

public enum Constants {

    ERROR("error"),
    FAIL("fail"),
    SUCCESS("success"),
    RESPONSE("response");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
