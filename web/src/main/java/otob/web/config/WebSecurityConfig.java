package otob.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import otob.model.constant.Status;
import otob.model.entity.User;
import otob.service.CustomUserDetailsService;
import otob.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(
                "/api/users",
                "/api/admin/register"
            ).hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/api/products").hasAnyRole("ADMIN", "CASHIER")
            .antMatchers(
                "/api/cashier/register",
                "/api/orders",
                "/api/orders/{\\w+}/accept",
                "/api/orders/{\\w+}/reject",
                "/api/products/{\\d+}",
                "/api/products/batch"
            ).hasAnyRole("ADMIN", "CASHIER")
            .antMatchers(
                "/api/"
            ).hasAnyRole("")
            .antMatchers(
                "/api/carts",
                "/api/carts/{\\d+}",
                "/api/carts/{\\d+}/{\\d+}",
                "/api/carts/checkout",
                "/api/orders/user",
                "/api/orders/{\\w+}/search"
            ).hasRole("CUSTOMER")
            .antMatchers(HttpMethod.GET, "/api/products").permitAll()
            .antMatchers(
                "/api/users/customer/register",
                "/api/products/id/{\\d+}",
                "/api/products/name/{\\w+}"
            ).permitAll()
            .and()
            .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successHandler((request, response, authentication) -> {
                    logger.info("Login Success");

                    String email = request.getParameter("username");
                    HttpSession session = request.getSession(true);

                    session.setAttribute("userId", email);
                    setCookie(session, response, email, true);
                })
                .failureHandler((request, response, exception) -> {
                    logger.info("Login Fail");

                    String email = request.getParameter("username");
                    HttpSession session = request.getSession(true);

                    setCookie(session, response, email, false);
                })
            .and()
            .logout()
                .logoutUrl("/api/auth/logout")
                .deleteCookies("user-id")
                .deleteCookies("user-role")
                .deleteCookies("is-login")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    private void setCookie(HttpSession session, HttpServletResponse response, String email, boolean authenticated) {
        User user = userService.getUserByEmail(email);
        List<String> roleList = new ArrayList<>(Arrays.asList("ROLE_ADMIN", "ROLE_CASHIER", "ROLE_CUSTOMER"));

        Cookie userId = new Cookie("user-id", authenticated ? email : session.getAttribute("userId").toString());
        userId.setHttpOnly(false);
        userId.setSecure(false);
        userId.setPath("/");
        userId.setMaxAge(3600);

        Cookie userRole = new Cookie("user-role", authenticated ?
                String.valueOf(roleList.indexOf(user.getRoles().get(0).getName())+1) : "4");
        userRole.setHttpOnly(false);
        userRole.setSecure(false);
        userRole.setPath("/");
        userRole.setMaxAge(3600);

        Cookie isLogin = new Cookie("is-login", authenticated ? Status.LOGIN_TRUE : Status.LOGIN_FALSE);
        isLogin.setHttpOnly(false);
        isLogin.setSecure(false);
        isLogin.setPath("/");
        isLogin.setMaxAge(3600);

        response.addCookie(userId);
        response.addCookie(userRole);
        response.addCookie(isLogin);
    }

}
