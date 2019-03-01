package future.phase2.offlinetoonlinebazaar.controller;

import future.phase2.offlinetoonlinebazaar.helper.ResponseGenerator;

public abstract class GlobalController {

    public <T> ResponseGenerator toResponse(T value){
        return ResponseGenerator.builder()
                .code("200")
                .message("Success")
                .data(value)
                .build();
    }

}
