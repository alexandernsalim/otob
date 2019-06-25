package future.phase2.offlinetoonlinebazaar.model.enumerator;

public enum ErrorCode {
    NOT_FOUND("404", "Not Found"),
    BAD_REQUEST("400", "Bad Request"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),

    USER_NOT_FOUND("404", "User not found"),
    EMAIL_EXISTS("400", "Email already registered"),

    PRODUCT_NOT_FOUND("404", "Sorry, product not found"),
    STOCK_INSUFFICIENT("400", "Insufficient stock"),

    ORDER_NOT_FOUND("404", "Sorry, order not found"),
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
