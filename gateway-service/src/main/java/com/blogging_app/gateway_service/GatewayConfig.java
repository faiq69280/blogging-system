package com.blogging_app.gateway_service;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/auth/**", "/user/**")
                        .uri("http://localhost:8081"))
                .route("article-api", r -> r.path("/post/**")
                        .uri("http://localhost:8082"))
                .build();
    }
}
