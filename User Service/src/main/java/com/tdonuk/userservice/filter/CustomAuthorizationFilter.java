package com.tdonuk.userservice.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tdonuk.userservice.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
	private final List<String> secureList = List.of(
			"/api/user/register",
			"/api/user/login",
			"/api/user/check",
			"/api/user/token/refresh"
			);
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(secureList.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String tokenHeader = request.getHeader(AUTHORIZATION);
            if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                try {
                    JwtUtils.validate(tokenHeader);
                    filterChain.doFilter(request, response);
                } catch(Exception e) {
                    log.error(e.getMessage());
                    response.setHeader("Content-Type", "application/json; charset=UTF-8");
                    response.setStatus(401);
                    new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
