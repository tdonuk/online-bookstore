package com.tdonuk.apigateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> publicEndpoints = List.of(
      "/api/user/login",
      "/api/user/register",
      "/api/user/token/**"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> publicEndpoints.stream().noneMatch(url -> request.getURI().getPath().contains(url));
}
