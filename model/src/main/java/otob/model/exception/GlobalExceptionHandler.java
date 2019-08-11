package otob.model.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import otob.model.enumerator.ErrorCode;
import otob.model.response.Response;

import javax.xml.bind.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ValidationException.class)
    public Response validationException(ValidationException ex) {
        return Response.builder()
                .code(ErrorCode.BAD_REQUEST.getCode())
                .message(ErrorCode.BAD_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(value = CustomException.class)
    public Response customException(CustomException ex) {
        return Response.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = OutOfStockException.class)
    public Response customException(OutOfStockException ex) {
        return Response.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .data(ex.getOutOfStockProducts())
                .build();
    }

    @ExceptionHandler(value = Exception.class)
    public Response unknownException(Exception ex) {
        return Response.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

}
