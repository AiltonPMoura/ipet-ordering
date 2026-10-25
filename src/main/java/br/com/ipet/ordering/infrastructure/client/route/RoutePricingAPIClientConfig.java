package br.com.ipet.ordering.infrastructure.client.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RoutePricingAPIClientConfig {

    @Bean
    public RoutePricingAPIClient routePricingApiClient(@Value("${ipet.integrations.route.url}") String routePricingUrl) {
        var restClient = RestClient.builder().baseUrl(routePricingUrl).build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(RoutePricingAPIClient.class);

    }

}
