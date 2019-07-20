package otob.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import otob.constant.path.AuthApiPath;
import otob.constant.path.ProductApiPath;
import otob.enumerator.ErrorCode;
import otob.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    private List<String> excludedPath = Arrays.asList(
            AuthApiPath.BASE_PATH + AuthApiPath.LOGIN,
            ProductApiPath.BASE_PATH,
            ProductApiPath.BASE_PATH + ProductApiPath.GET_PRODUCT_BY_ID,
            ProductApiPath.BASE_PATH + ProductApiPath.GET_PRODUCT_BY_NAME
    );

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestPath = request.getServletPath();
//
//        logger.info(requestPath);
//        if(excludedPath.contains(requestPath)){
//            return true;
//        }else{
//            if(!isAuthenticated(request) || !isAuthorized(request)){
//                throw new CustomException(
//                        ErrorCode.UNAUTHORIZED.getCode(),
//                        ErrorCode.UNAUTHORIZED.getMessage()
//                );
//            }else{
//                return true;
//            }
//        }
//    }

}
