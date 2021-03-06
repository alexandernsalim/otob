package otob.model.enumerator;

public enum ErrorCode {
    INVALID_CLIENT("400", "Invalid client"),
    NOT_FOUND("404", "Not Found"),
    BAD_REQUEST("400", "Bad Request"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error"),

    USER_NOT_FOUND("404", "User not found"),
    EMAIL_EXISTS("400", "Email already registered"),
    PASSWORD_NOT_MATCH("400", "Password not match"),

    PRODUCT_NOT_FOUND("404", "Product not found"),
    STOCK_INSUFFICIENT("400", "Insufficient productStock"),

    SOME_PRODUCTS_INVALID("400", "Insufficient productStock of one or more product"),
    ORDER_NOT_FOUND("404", "Order not found"),
    ORDER_PROCESSED("400", "Order already processed"),

    GENERATE_ID_FAIL("500", "Failed to generate id"),
    EXCEL_FORMAT_ERROR("400", "Wrong format in file");

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
