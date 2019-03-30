package future.phase2.offlinetoonlinebazaar.exception;

import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public Response resourceNotFoundException(ResourceNotFoundException ex){
        return Response.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(value = Exception.class)
    public Response unknownException(Exception ex){
        return Response.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
