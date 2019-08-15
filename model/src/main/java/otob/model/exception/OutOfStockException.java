package otob.model.exception;

import java.util.List;

public class OutOfStockException extends RuntimeException {
    private String errorCode;
    private List<String> outOfStockProducts;

    public OutOfStockException(String errorCode, String message, List<String> outOfStockProducts) {
        super(message);
        this.errorCode = errorCode;
        this.outOfStockProducts = outOfStockProducts;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public List<String> getOutOfStockProducts() {
        return outOfStockProducts;
    }

}
