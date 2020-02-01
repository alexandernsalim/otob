package otob.web.config;

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
import otob.service.CustomUserDetailsService;
import otob.web.config.jwt.JwtConfig;
import otob.web.config.jwt.JwtTokenAuthenticationFilter;
import otob.web.config.jwt.JwtUsernameAndPasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtConfig jwtConfig;

    //TODO Make sure security is working well
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
            .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
            .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig()))
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
            .antMatchers(
                "/api/users",
                "/api/admin/register",
                "/api/cashier/register",
                "/api/products/{\\d+}",
                "/api/products/batch"
            ).hasRole("ADMIN")
            .antMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
            .antMatchers("/api/orders", "/api/orders/filter").hasAnyRole("ADMIN", "CASHIER")
            .antMatchers(
                "/api/orders/{\\w+}/accept",
                "/api/orders/{\\w+}/reject",
                "/api/orders/export"
            ).hasAnyRole("CASHIER")
            .antMatchers(
                "/api/carts",
                "/api/carts/{\\d+}",
                "/api/carts/{\\d+}/{\\d+}",
                "/api/carts/checkout",
                "/api/orders/user"
            ).hasRole("CUSTOMER")
            .antMatchers(
                "/api/users/change-password"
            ).authenticated()
            .antMatchers(HttpMethod.GET, "/api/products").permitAll()
            .antMatchers(
                "/api/users/customer/register",
                "/api/orders/{\\w+}/search"
            ).permitAll()
            .antMatchers("/api/auth/**").permitAll();
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

}
