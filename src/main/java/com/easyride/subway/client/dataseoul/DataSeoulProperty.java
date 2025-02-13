package com.easyride.subway.client.dataseoul;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("data-seoul")
public record DataSeoulProperty(String baseUrl, String apiKey) {
}
