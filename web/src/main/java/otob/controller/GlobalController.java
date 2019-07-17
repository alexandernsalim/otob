package otob.controller;

import otob.response.Response;

public abstract class GlobalController {

    public <T> Response toResponse(T value){
        return Response.builder()
                .code("200")
                .message("Success")
                .data(value)
                .build();
    }

}
