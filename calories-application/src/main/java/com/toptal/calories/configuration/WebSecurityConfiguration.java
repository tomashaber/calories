package com.toptal.calories.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * Created by Tomas Haber on 8.7.2015
 */
@Configuration
@ComponentScan("com.toptal.calories.api")
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String PASSWORD_PARAMETER = "password";
    public static final String USERNAME_PARAMETER = "username";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setSaltSource(user -> null);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder("calories");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage(null);
        UsernamePasswordAuthenticationFilter loginFilter = new UsernamePasswordAuthenticationFilter();
        loginFilter.setAuthenticationManager(super.authenticationManager());
        loginFilter.setUsernameParameter(USERNAME_PARAMETER);
        loginFilter.setPasswordParameter(PASSWORD_PARAMETER);
        loginFilter.setFilterProcessesUrl("/auth/login");
        loginFilter.setPostOnly(true);

        LogoutFilter logoutFilter = new LogoutFilter("/",new CookieClearingLogoutHandler(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/auth/logout");
        http.
                antMatcher("/**").csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().
                authorizeRequests().antMatchers("/api/user/register").permitAll().and().
                authorizeRequests().antMatchers("/auth/*").permitAll().and().
                authorizeRequests().antMatchers("/api/**").authenticated().and().
                authorizeRequests().antMatchers("/**").permitAll().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                .addFilter(loginFilter).addFilter(logoutFilter);
    }

}
