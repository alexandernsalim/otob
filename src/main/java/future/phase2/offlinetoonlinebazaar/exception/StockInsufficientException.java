package future.phase2.offlinetoonlinebazaar.exception;

public class StockInsufficientException extends RuntimeException{
    private String errorCode;

    public StockInsufficientException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
