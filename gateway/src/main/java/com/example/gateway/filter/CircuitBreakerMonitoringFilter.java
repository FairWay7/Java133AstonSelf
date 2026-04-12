package com.example.gateway.filter;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CircuitBreakerMonitoringFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LogManager.getLogger(CircuitBreakerMonitoringFilter.class);
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerMonitoringFilter(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(this::registerEventListeners);
    }

    private void registerEventListeners(CircuitBreaker circuitBreaker) {
        circuitBreaker.getEventPublisher()
            .onSuccess(event -> logger.info("CB '{}' - Success. State: {}",
                circuitBreaker.getName(), circuitBreaker.getState()))
            .onError(event -> logger.warn("CB '{}' - Error: {}",
                circuitBreaker.getName(), event.getThrowable().getMessage()));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        String cbName = path.startsWith("/api/users") ? "userServiceCB" :
            (path.startsWith("/api/notification") ? "notificationServiceCB" : "default");

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(cbName);

        return chain.filter(exchange)
            .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
            .doOnError(throwable -> logger.error("Error in Circuit Breaker '{}': {}", cbName, throwable.getMessage()));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
