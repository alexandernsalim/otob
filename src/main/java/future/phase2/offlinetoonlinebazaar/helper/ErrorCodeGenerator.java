package future.phase2.offlinetoonlinebazaar.helper;

public enum  ErrorCodeGenerator {
    NOT_FOUND("404", "Not Found"),
    BAD_REQUEST("400", "Bad Request"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

    private String code;
    private String message;

    ErrorCodeGenerator(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
