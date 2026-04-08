package com.Aga.Agali.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleAuthFilter {

    public GatewayFilter requireRole(String role) {
        return (exchange, chain) -> {
            String userRole = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-User-Role");

            if (!role.equals(userRole)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }

    public GatewayFilter requireAnyRole(String... roles) {
        return (exchange, chain) -> {
            String userRole = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-User-Role");

            List<String> allowed = Arrays.asList(roles);
            if (userRole == null || !allowed.contains(userRole)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }
}