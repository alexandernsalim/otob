package otob.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import otob.constant.Role;
import otob.constant.Status;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;
import otob.generator.RandomTextGenerator;
import otob.service.api.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthService authService;

    @Autowired
    private RandomTextGenerator textGenerator;

    private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestPath = request.getServletPath();

        checkSession(request);
        logger.info("Requesting: " + requestPath);
        if (!authService.isAuthorized(request)) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED.getCode(),
                    ErrorCode.UNAUTHORIZED.getMessage()
            );
        } else {
            return true;
        }
    }

    private void checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        if (session.isNew()) {
            logger.info("Init new session");

            session.setAttribute("userId", textGenerator.generateRandomUserId());
            session.setAttribute("role", Role.GUEST);
            session.setAttribute("isLogin", Status.LOGIN_FALSE);
        }
    }

}
