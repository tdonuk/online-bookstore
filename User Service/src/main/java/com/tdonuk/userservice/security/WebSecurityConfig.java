package com.tdonuk.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdonuk.userservice.filter.CustomAuthenticationFilter;
import com.tdonuk.userservice.filter.CustomAuthorizationFilter;
import com.tdonuk.userservice.model.repository.UserRepository;
import com.tdonuk.userservice.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/api/user/register", "/api/user/login", "/api/user/token/**").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(authFilter()).exceptionHandling().authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(401);
                    response.setHeader("Content-Type", "application/json; charset=UTF-8");
                    new ObjectMapper().writeValue(response.getOutputStream(), "Geçersiz kullanıcı adı veya parola");
                })
                .accessDeniedHandler((request, response, exception) -> {
                    response.addHeader("Content-Type", "application/json; charset=UTF-8");
                    response.setStatus(403);
                    new ObjectMapper().writeValue(response.getOutputStream(), "Erişim yetkiniz bulunmamaktadır.");
                })
                .and()
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public CustomAuthenticationFilter authFilter() throws Exception{
        CustomAuthenticationFilter authFilter = new CustomAuthenticationFilter(authenticationManagerBean(), userDetailsService);

        authFilter.setFilterProcessesUrl("/api/user/login");
        authFilter.setPostOnly(true);
        authFilter.setUsernameParameter("email");
        authFilter.setAllowSessionCreation(false);

        return authFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
