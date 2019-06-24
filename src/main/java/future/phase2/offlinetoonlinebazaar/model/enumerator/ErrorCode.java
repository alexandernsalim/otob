package future.phase2.offlinetoonlinebazaar.model.enumerator;

public enum ErrorCode {
    NOT_FOUND("404", "Not Found"),
    BAD_REQUEST("400", "Bad Request"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
    EMAIL_EXISTS("400", "Email already registered, please check your password in your email"),
    STOCK_INSUFFICIENT("400", "Stock not enough"),
    ORDER_PROCESSED("400", "Order already processed");

    private String code;
    private String message;

    ErrorCode(String code, String message){
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
