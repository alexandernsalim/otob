package otob.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import otob.enumerator.ErrorCode;
import otob.response.Response;

import javax.xml.bind.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ValidationException.class)
    public Response validationException(ValidationException ex){
        return Response.builder()
                .code(ErrorCode.BAD_REQUEST.getCode())
                .message(ErrorCode.BAD_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(value = CustomException.class)
    public Response customException(CustomException ex){
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
