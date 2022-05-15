package com.tdonuk.apigateway.config;

import com.tdonuk.apigateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("USER-SERVICE", r -> r.path("/api/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://USER-SERVICE/"))

                .route("BOOK-SERVICE", r -> r.path("/api/book/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://BOOK-SERVICE/")
                )
                
                .route("SEARCH-SERVICE", r -> r.path("/api/search/**")
                		.filters(f -> f.filter(filter))
                		.uri("lb://SCRAPER-SERVICE"))
                
                .build();
    }
}
