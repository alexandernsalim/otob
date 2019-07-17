package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import otob.enumerator.Status;
import otob.generator.RandomTextGenerator;
import otob.response.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class GlobalController {

    @Autowired
    private RandomTextGenerator textGenerator;

    public boolean isAuthorized(HttpServletRequest request){
        HttpSession session = request.getSession();

        if(session.getAttribute("isLogin") != null &&
           session.getAttribute("isLogin").equals(Status.LOGIN_TRUE.getStatus())){

            return true;
        }else{
            if(session.getAttribute("userId") == null){
                session.setAttribute("userId", textGenerator.generateRandomUserId());
                session.setAttribute("isLogin", Status.LOGIN_FALSE.getStatus());
            }

            return false;
        }
    }

    public <T> Response toResponse(T value){
        return Response.builder()
                .code("200")
                .message("Success")
                .data(value)
                .build();
    }

}
