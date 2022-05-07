package com.tdonuk.apigateway.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdonuk.apigateway.util.RouteValidator;
import com.tdonuk.apigateway.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthFilter implements GatewayFilter {

    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        try {
            if(routeValidator.isSecured.test(request)) {
                if(!request.getHeaders().containsKey("Authorization")) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return error(response, "Lütfen giriş yapınız");
                }

                String token = request.getHeaders().get("Authorization").get(0);

                jwtUtils.validate(token);
            }
        } catch(TokenExpiredException e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("expired", e.getMessage());
            return response.setComplete();
        } catch(Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);

            return error(response, e.getMessage());
        }

        return chain.filter(exchange);
    }

    private Mono<Void> error(ServerHttpResponse response, String error) {
        byte[] body;

        try {
            body = new ObjectMapper().writeValueAsString(error).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            error = "Bilinmeyen bir hata oluştu. Lütfen tekrar giriş yapınız";
            body = error.getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(body);

        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");

        return response.writeWith(Mono.just(buffer));
    }
}
