package org.bebra.apigateway;

import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ConvertHttpMethodFilter extends AbstractGatewayFilterFactory<ConvertHttpMethodFilter.Config> {
    public ConvertHttpMethodFilter() {
        super(Config.class);
    }

    static final String REPLACEMENT_KEY = "replacement";
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(REPLACEMENT_KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();

            var mutatedExchange = exchange.mutate().request(request.mutate().method(config.getReplacement()).build())
                    .build();
            return chain.filter(mutatedExchange);
        };
    }

    @Override
    public String name() {
        return "ConvertHttpMethod";
    }

    public static class Config {
        private HttpMethod replacement;

        public HttpMethod getReplacement() {
            return replacement;
        }

        public void setReplacement(String method) {
            this.replacement = HttpMethod.valueOf(method.toUpperCase());
        }
    }

}