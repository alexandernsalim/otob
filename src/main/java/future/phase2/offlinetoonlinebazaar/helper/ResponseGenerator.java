package future.phase2.offlinetoonlinebazaar.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGenerator<T> {
    private String code;
    private String message;
    private T data;
}
