package com.tdonuk.userservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tdonuk.userservice.model.repository.UserRepository;
import com.tdonuk.userservice.service.UserDetailsImpl;
import com.tdonuk.userservice.service.UserDetailsServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE)
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final long ONE_HOUR = 1 * 1000 * 60 * 60;
    private static final long ONE_DAY = ONE_HOUR * 24;
    private static final long ONE_MONTH = ONE_DAY * 30;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userService;

    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager, final UserDetailsServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if ("POST".equalsIgnoreCase(request.getMethod()))
        {
            try {
                String email, password;

                String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                ObjectMapper mapper = new JsonMapper();
                JsonNode json = mapper.readTree(body);

                email = json.get("email").asText();
                password = json.get("password").asText();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

                return authenticationManager.authenticate(token);
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(400);
            }
        }
        response.sendError(400);
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("SUCCESS");

        UserDetailsImpl user = (UserDetailsImpl) authResult.getPrincipal();

        user.getUser().setLastLoginDate(new Date());

        userService.updateUserLastLoginDate(user.getUser().getUserId(), user.getUser().getLastLoginDate());

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 4*ONE_HOUR)) // 4 hours
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ONE_MONTH)) // 1 month
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", refreshToken);
        response.setHeader("Content-Type", "application/json; charset=UTF-8");


        new ObjectMapper().writeValue(response.getOutputStream(),user.getUser());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("FAILED: {}", failed.getMessage());

        response.setStatus(401);
        response.addHeader("Content-Type", "application/json; charset=UTF-8");

        new ObjectMapper().writeValue(response.getOutputStream(), "Geçersiz kullanıcı adı veya parola");
    }
}
