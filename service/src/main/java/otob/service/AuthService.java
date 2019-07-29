package otob.service;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    boolean login(String email, String password);

    boolean isAuthenticated(HttpServletRequest request);
    boolean isAuthorized(HttpServletRequest request);

}
