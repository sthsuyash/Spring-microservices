package com.suyash.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> publicUrls = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/verify-token",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> publicUrls
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
