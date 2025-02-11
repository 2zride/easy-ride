package com.easyride.subway.client.sk;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sk")
public record SkProperty(String baseUrl, String apiKey) {
}
