package com.suyash.api_gateway.filter;

import com.suyash.api_gateway.util.ErrorResponseUtil;
import com.suyash.api_gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * A filter to authenticate requests by validating JWT tokens.
 * This filter intercepts requests and checks for the presence of an authorization header.
 * If the header is present, it validates the JWT token.
 * If the validation fails or the header is missing, it returns an unauthorized error response.
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final RouteValidator validator;

    /**
     * Constructs an AuthenticationFilter with the specified JwtUtil and RouteValidator.
     *
     * @param jwtUtil    the utility for JWT operations
     * @param validator  the validator to check if a route is secured
     */
    public AuthenticationFilter(JwtUtil jwtUtil, RouteValidator validator) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Applies the filter logic.
     * Checks if the request is to a secured route, validates the JWT token, and handles errors.
     *
     * @param config the configuration object
     * @return the gateway filter
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
                }

                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
                try {
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    LOGGER.error("Error in parsing token", e);
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    /**
     * Handles error responses by generating an error response with the specified message and status.
     *
     * @param exchange the server web exchange
     * @param message  the error message
     * @param status   the HTTP status
     * @return a Mono<Void> indicating when the error response is complete
     */
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        return ErrorResponseUtil.generateErrorResponse(exchange.getResponse(), status, message);
    }

    /**
     * Configuration class for the AuthenticationFilter.
     */
    public static class Config { }
}
