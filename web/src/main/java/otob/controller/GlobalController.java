package otob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import otob.constant.Role;
import otob.constant.Status;
import otob.generator.RandomTextGenerator;
import otob.response.Response;
import otob.service.api.RoleAccessService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public abstract class GlobalController {

    @Autowired
    private RoleAccessService roleAccessService;

    @Autowired
    private RandomTextGenerator textGenerator;

    private HttpSession session;

    public boolean isAuthenticated(HttpServletRequest request) {
        session = request.getSession(true);

        if (session.getAttribute("isLogin") != null &&
                session.getAttribute("isLogin").equals(Status.LOGIN_TRUE)) {

            return true;
        } else {
            if (session.getAttribute("userId") == null) {
                session.setAttribute("userId", textGenerator.generateRandomUserId());
                session.setAttribute("role", Role.GUEST);
                session.setAttribute("isLogin", Status.LOGIN_FALSE);
            }

            return false;
        }
    }

    public boolean isAuthorized(HttpServletRequest request) {
        session = request.getSession(true);

        List<String> access = roleAccessService.getAccessByRole(session.getAttribute("role").toString());

        if (access.contains(request.getServletPath())) {
            return true;
        }

        return false;
    }

    public <T> Response toResponse(T value) {
        return Response.builder()
                .code("200")
                .message("Success")
                .data(value)
                .build();
    }

}
