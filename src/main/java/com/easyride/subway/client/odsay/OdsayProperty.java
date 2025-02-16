package com.easyride.subway.client.odsay;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "odsay")
public record OdsayProperty(String baseUrl, String apiKey) {
}
