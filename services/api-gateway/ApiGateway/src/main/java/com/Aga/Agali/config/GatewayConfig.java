package com.Aga.Agali.config;

import com.Aga.Agali.filter.JwtAuthFilter;
import com.Aga.Agali.filter.RoleAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthFilter authFilter;
    private final RoleAuthFilter roleFilter;

    public GatewayConfig(JwtAuthFilter authFilter, RoleAuthFilter roleFilter) {
        this.authFilter = authFilter;
        this.roleFilter = roleFilter;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()


                .route("user-service-auth", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:8081"))

                .route("user-service-user", r -> r
                        .path("/api/user/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://localhost:8081"))

                .route("test-service", r -> r
                        .path("/api/test/**")
                        .filters(f -> f
                                .filter(authFilter)
                                .filter(roleFilter.requireRole("STUDENT")))
                        .uri("http://localhost:8082"))

                .route("exam-service", r -> r
                        .path("/api/exam/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://localhost:8082"))

                .route("statistics-service", r -> r
                        .path("/api/statistics/**")
                        .filters(f -> f
                                .filter(authFilter)
                                .filter(roleFilter.requireAnyRole(
                                        "STUDENT", "TEACHER", "PARENT")))
                        .uri("http://localhost:8083"))

                .route("learning-plan-service", r -> r
                        .path("/api/learning/**")
                        .filters(f -> f
                                .filter(authFilter)
                                .filter(roleFilter.requireAnyRole(
                                        "STUDENT", "TEACHER")))
                        .uri("http://localhost:8084"))

                .route("notification-service", r -> r
                        .path("/api/notification/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://localhost:8085"))

                .route("permission-service", r -> r
                        .path("/api/permission/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://localhost:8086"))

                .route("payment-service", r -> r
                        .path("/api/payment/**")
                        .filters(f -> f
                                .filter(authFilter)
                                .filter(roleFilter.requireAnyRole(
                                        "PARENT", "TEACHER","ADMIN")))
                        .uri("http://localhost:8087"))

                .build();
    }
}