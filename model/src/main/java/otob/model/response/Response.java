package otob.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Response<T> {
    private String code;
    private String message;
    private T data;

}
