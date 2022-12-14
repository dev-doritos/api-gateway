package com.example.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * ApiGateway Custom Filter
 * */
@Component
@Slf4j
public class LogFilter extends AbstractGatewayFilterFactory<LogFilter.Config> {

    public LogFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        
        // 순서 적용 필터
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // request Pre Handler
            log.info("LogFilter Pre Handler > requestId={}", request.getId());

            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        // response Post Handler
                        log.info("LogFilter Post Handler > responseCode={}", response.getStatusCode());
                    }));

        }, Ordered.HIGHEST_PRECEDENCE);

        return filter;
    }

    public static class Config {
    }
}
