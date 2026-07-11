package br.com.ipet.ordering.infrastructure.client.shipping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ShippingAPIClientConfig {

    @Bean
    public ShippingAPIClient shippingApiClient(@Value("${ipet.integrations.shipping.url}") String shippingUrl) {
        var restClient = RestClient.builder().baseUrl(shippingUrl).build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(ShippingAPIClient.class);

    }

}
