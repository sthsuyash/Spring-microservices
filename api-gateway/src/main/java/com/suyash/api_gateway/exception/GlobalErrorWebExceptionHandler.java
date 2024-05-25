package com.suyash.api_gateway.exception;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Global error handler for handling web exceptions in a reactive environment.
 */
@Configuration
@Order(-2)
public class GlobalErrorWebExceptionHandler extends ResponseStatusExceptionHandler {

    /**
     * Handles exceptions and generates a custom response for 404 Not Found errors.
     *
     * @param exchange The ServerWebExchange instance
     * @param ex       The Throwable instance
     * @return Mono<Void> indicating when exception handling is complete
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException exception = (ResponseStatusException) ex;
            if (exception.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                response.setStatusCode(HttpStatus.NOT_FOUND);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                String body = "{\"success\": false, \"data\": \"Not Found\", \"message\": \"The requested URL was not found.\"}";
                byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

                return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
            }
        }

        return super.handle(exchange, ex);
    }
}