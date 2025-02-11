package com.easyride.global.config;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withForbiddenRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@Import({ClientConfig.class})
public abstract class BaseRestClientTest {

    @Autowired
    protected RestClient.Builder restClientBuilder;

    protected MockRestServiceServer mockServer;

    protected void configure200MockServer(String requestUri, String responseBody) {
        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    }

    protected void configure400MockServer(String requestUri) {
        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());
    }

    protected void configure400MockServer(String requestUri, String responseBody) {
        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest().body(responseBody));
    }

    protected void configure403MockServer(String requestUri, String responseBody) {
        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withForbiddenRequest().body(responseBody));
    }

    protected String readResourceFile(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String resourcePath = classLoader.getResource(fileName).getPath();
        Path path = Path.of(resourcePath);
        return Files.readString(path);
    }
}
