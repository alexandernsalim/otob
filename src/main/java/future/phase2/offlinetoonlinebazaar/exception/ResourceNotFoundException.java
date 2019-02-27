package future.phase2.offlinetoonlinebazaar.exception;

public class ResourceNotFoundException extends RuntimeException {
    private String errorCode;

    public ResourceNotFoundException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
