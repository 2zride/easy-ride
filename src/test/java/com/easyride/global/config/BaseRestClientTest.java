package com.easyride.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@Import({ClientConfig.class})
public abstract class BaseRestClientTest {

    @Autowired
    protected RestClient.Builder restClientBuilder;

    protected MockRestServiceServer mockServer;
}
