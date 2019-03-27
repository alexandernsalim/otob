package future.phase2.offlinetoonlinebazaar.exception;

public class EmailExistsException extends RuntimeException {
    private String errorCode;

    public EmailExistsException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
