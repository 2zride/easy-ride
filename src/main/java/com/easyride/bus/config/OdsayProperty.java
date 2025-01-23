package com.easyride.bus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "odsay")
public record OdsayProperty(String baseUrl, String apiKey) {

}
