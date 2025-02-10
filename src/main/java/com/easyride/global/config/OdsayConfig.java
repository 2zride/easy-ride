package com.easyride.global.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@EnableConfigurationProperties(OdsayProperty.class)
@RequiredArgsConstructor
@Configuration
public class OdsayConfig {

    private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(60);
    private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

    private final OdsayProperty property;

    @Bean
    public RestClient.Builder odsayRestClientBuilder() {
        return RestClient.builder()
                .baseUrl(baseUri())
                .requestFactory(requestFactory());
    }

    private String baseUri() {
        return UriComponentsBuilder.fromUriString(property.baseUrl())
                .queryParam("apiKey", property.apiKey())
                .toUriString();
    }

    private ClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
        requestFactory.setReadTimeout(DEFAULT_READ_TIMEOUT);
        return requestFactory;
    }
}
