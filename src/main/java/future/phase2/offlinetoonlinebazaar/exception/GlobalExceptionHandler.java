package future.phase2.offlinetoonlinebazaar.exception;

import future.phase2.offlinetoonlinebazaar.model.enumerator.ErrorCode;
import future.phase2.offlinetoonlinebazaar.model.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = ValidationException.class)
//    public Response validationException(ValidationException ex){
//        return Response.builder()
//                .code(ErrorCode.BAD_REQUEST.getCode())
//                .message(ErrorCode.BAD_REQUEST.getMessage())
//                .build();
//    }
//
//    @ExceptionHandler(value = ResourceNotFoundException.class)
//    public Response resourceNotFoundException(ResourceNotFoundException ex){
//        return Response.builder()
//                .code(ex.getErrorCode())
//                .message(ex.getMessage())
//                .build();
//    }
//
//    @ExceptionHandler(value = Exception.class)
//    public Response unknownException(Exception ex){
//        return Response.builder()
//                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
//                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
//                .build();
//    }

}
