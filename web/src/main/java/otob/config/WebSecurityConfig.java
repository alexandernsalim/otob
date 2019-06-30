package otob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import otob.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
//            .antMatchers(
//                    "/api/users/register/admin",
//                    "/api/users/register/cashier",
//                    "/api/users/delete/*",
//                    "/api/users",
//                    "/api/roles"
//            ).hasRole("ADMIN")
//            .antMatchers("/api/users/register/customer").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin();
//            .loginPage("/login.html")
//            .failureHandler(authenticationFailureHandler())
//            .and()
//            .logout()
//            .logoutUrl("/perform_logout")
//            .deleteCookies("JSESSIONID");
//            .logoutSuccessHandler(logoutSuccessHandler());
    }

//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//            .antMatchers(
//                    "/api/users/register/admin",
//                    "/api/users/register/cashier",
//                    "/api/users/delete/*",
//                    "/api/users",
//                    "/api/roles",
//                    "/api/products/*"
//            ).hasRole("CUSTOMER")
////            .antMatchers("/api/users/register/customer").permitAll()
////            .anyRequest().permitAll()
//            .and()
//            .formLogin().loginPage("http://localhost:8080/#/register").permitAll();
////            .failureHandler(authenticationFailureHandler())
////            .and()
////            .logout()
////            .logoutUrl("/perform_logout")
////            .deleteCookies("JSESSIONID");
////            .logoutSuccessHandler(logoutSuccessHandler());
//    }

//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers(
////                    "/api/users/register/admin",
////                    "/api/users/register/cashier",
////                    "/api/users/delete/*",
////                    "/api/users",
////                    "/api/roles",
//                    "/api/products/*"
//                ).permitAll()
//                .antMatchers("/api/users/register/customer").permitAll()
//                .and()
//                .formLogin().loginPage("http://localhost:8080/#/login")
//                .permitAll()
//                .and()
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
////            .failureHandler(authenticationFailureHandler())
////            .and()
////            .logout()
////            .logoutUrl("/perform_logout")
////            .deleteCookies("JSESSIONID");
////            .logoutSuccessHandler(logoutSuccessHandler());
//    }



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

}
