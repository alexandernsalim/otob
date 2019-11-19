package otob.web.config;

import com.google.gson.Gson;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import otob.model.response.Response;
import otob.service.CustomUserDetailsService;
import otob.service.UserService;
import otob.web.config.jwt.JwtConfig;
import otob.web.config.jwt.JwtTokenAuthenticationFilter;
import otob.web.config.jwt.JwtUsernameAndPasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtConfig jwtConfig;

    private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    private static Gson gson = new Gson();
    private Response jsonResponse = Response.builder()
            .code("200")
            .message("Success")
            .build();

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(
                (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            )
            .and()
            //Add a filter to validate token every request
            .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)

            // Add a filter to validate user credentials and add token in the response header

            // What's the authenticationManager()?
            // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing user's credentials
            // The filter needs this auth manager to authenticate the user.
            .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig()))
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
            .anyRequest().authenticated();

//            .antMatchers(
//                "/api/users",
//                "/api/admin/register",
//                "/api/cashier/register",
//                "/api/products/{\\d+}",
//                "/api/products/batch"
//            ).hasRole("ADMIN")
//            .antMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
//            .antMatchers("/api/orders", "/api/orders/filter").hasAnyRole("ADMIN", "CASHIER")
//            .antMatchers(
//                "/api/orders/{\\w+}/accept",
//                "/api/orders/{\\w+}/reject",
//                "/api/orders/export"
//            ).hasAnyRole("CASHIER")
//            .antMatchers(
//                "/api/carts",
//                "/api/carts/{\\d+}",
//                "/api/carts/{\\d+}/{\\d+}",
//                "/api/carts/checkout",
//                "/api/orders/user"
//            ).hasRole("CUSTOMER")
//            .antMatchers(
//                "/api/users/change-password"
//            ).authenticated()
//            .antMatchers(HttpMethod.GET, "/api/products").permitAll()
//            .antMatchers(
//                "/api/users/customer/register",
//                "/api/orders/{\\w+}/search"
//            ).permitAll()
//            .antMatchers("/api/auth/**").permitAll();

//            .and()
//            .formLogin()
//                .loginProcessingUrl("/api/auth/login")
//                .successHandler((request, response, authentication) -> {
//                    logger.info("Login Success");
//
//                    String email = request.getParameter("username");
//                    HttpSession session = request.getSession(true);
//                    User user = userService.getUserByEmail(email);
//
//                    session.setAttribute("userId", email);
//                    session.setAttribute("userRole", user.getRoles().get(0).getRoleId());
//                    session.setAttribute("isLogin", true);
//
//                    setCookie(session, response, email, true);
//
//                    jsonResponse.setData("Accepted");
//                    response.setStatus(200);
//                    response.getWriter().append(gson.toJson(jsonResponse));
//                })
//                .failureHandler((request, response, exception) -> {
//                    logger.warn("Login Fail");
//
//                    String email = request.getParameter("username");
//                    HttpSession session = request.getSession(true);
//
//                    setCookie(session, response, email, false);
//
//                    jsonResponse.setData("Bad Request");
//                    response.setStatus(400);
//                    response.getWriter().append(gson.toJson(jsonResponse));
//                })
//            .and()
//            .logout()
//                .logoutUrl("/api/auth/logout")
//                .logoutSuccessHandler((request, response, authentication) -> {
//                    logger.info("Logout Success");
//
//                    jsonResponse.setData("OK");
//                    response.setStatus(200);
//                    response.getWriter().append(gson.toJson(jsonResponse));
//                })
//                .deleteCookies("user-id")
//                .deleteCookies("user-role")
//                .deleteCookies("is-login")
//                .deleteCookies("JSESSIONID")
//                .invalidateHttpSession(true)
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

    @Bean
    public JwtConfig jwtConfig(){
        return new JwtConfig();
    }

//    private void setCookie(HttpSession session, HttpServletResponse response, String email, boolean authenticated) {
//        User user = userService.getUserByEmail(email);
//
//        Cookie userId = new Cookie("user-id", authenticated ? email : session.getAttribute("userId").toString());
//        userId.setHttpOnly(false);
//        userId.setSecure(false);
//        userId.setPath("/");
//        userId.setMaxAge(3600);
//
//        Cookie userRole = new Cookie("user-role", authenticated ?
//                String.valueOf(user.getRoles().get(0).getRoleId()) : "4");
//        userRole.setHttpOnly(false);
//        userRole.setSecure(false);
//        userRole.setPath("/");
//        userRole.setMaxAge(3600);
//
//        Cookie isLogin = new Cookie("is-login", authenticated ? Status.LOGIN_TRUE : Status.LOGIN_FALSE);
//        isLogin.setHttpOnly(false);
//        isLogin.setSecure(false);
//        isLogin.setPath("/");
//        isLogin.setMaxAge(3600);
//
//        response.addCookie(userId);
//        response.addCookie(userRole);
//        response.addCookie(isLogin);
//    }

}
