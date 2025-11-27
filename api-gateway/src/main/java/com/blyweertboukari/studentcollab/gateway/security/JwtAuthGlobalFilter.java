package com.blyweertboukari.studentcollab.gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Centralized JWT authentication filter for all downstream services.
 * Validates the Authorization Bearer token and forwards user context headers.
 */
@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String path = exchange.getRequest().getPath().value();

        if (
                path.startsWith("/student-service/auth/")
                || path.equals("/student-service/students")
                || path.equals("/help-request-service/help-requests")
                || path.startsWith("/help-request-service/v3/api-docs")
                        || path.startsWith("/student-service/v3/api-docs")
                        || path.startsWith("/recommendation-service/v3/api-docs")
        ) {
            return chain.filter(exchange);
        }

        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            final String token = authHeader.substring(7);
            Claims claims = jwtService.extractAllClaims(token);

            // Optional: basic expiration check
            if (jwtService.isTokenExpired(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String email = claims.getSubject();
            Object idObj = claims.get("id");
            String userId = null;

            if (idObj instanceof Integer) {
                userId = String.valueOf((int) idObj);
            } else if (idObj instanceof Long) {
                userId = String.valueOf((long) idObj);
            } else if (idObj instanceof Double) {
                userId = String.valueOf(((Double) idObj).longValue());
            } else if (idObj instanceof String) {
                userId = (String) idObj;
            }

            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
            if (email != null) {
                requestBuilder.header("X-User-Email", email);
            }
            if (userId != null) {
                requestBuilder.header("X-User-Id", userId);
            }

            return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        // Run early in the filter chain
        return -100;
    }
}
