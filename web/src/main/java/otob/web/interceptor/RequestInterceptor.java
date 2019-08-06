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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RandomTextGenerator textGenerator;

    private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        checkSession(request);

        return true;
    }

    private void checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        if (session.isNew()) {
            logger.info("New client, init session");

            session.setAttribute("userId", textGenerator.generateRandomUserId());
        }
    }

}
