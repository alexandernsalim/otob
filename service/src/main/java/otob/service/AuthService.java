package otob.service;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    boolean login(String email, String password);
    HttpStatus logout(HttpServletRequest request, HttpServletResponse response);
    boolean isAuthenticated(HttpServletRequest request);
    boolean isAuthorized(HttpServletRequest request);

}
