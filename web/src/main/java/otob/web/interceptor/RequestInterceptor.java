package otob.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import otob.model.constant.Role;
import otob.model.constant.Status;
import otob.model.enumerator.ErrorCode;
import otob.model.exception.CustomException;
import otob.util.generator.RandomTextGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RandomTextGenerator textGenerator;

    private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkSession(request);

        if(!validClient(request)) {
            throw new CustomException(
                ErrorCode.INVALID_CLIENT.getCode(),
                ErrorCode.INVALID_CLIENT.getMessage()
            );
        }

        return true;
    }

    private void checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        if (session.isNew()) {
            logger.info("New client, init session");

            session.setAttribute("userId", textGenerator.generateRandomUserId());
            session.setAttribute("userRole", 4);
            session.setAttribute("isLogin", false);
        }
    }

    private boolean validClient(HttpServletRequest request) {
        boolean cond = true;
        HttpSession session = request.getSession(true);

        List<Cookie> cookies = Arrays.asList(request.getCookies());

        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("user-id")) {
                cond = session.getAttribute("userId").equals(cookie.getValue());
            } else if(cookie.getName().equals("user-role")) {
                cond = session.getAttribute("userRole").equals(cookie.getValue());
            } else if (cookie.getName().equals("is-login")) {
                cond = session.getAttribute("isLogin").equals(cookie.getValue());
            }
        }

        return cond;
    }

}
