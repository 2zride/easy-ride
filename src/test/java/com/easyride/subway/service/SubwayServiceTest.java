package com.easyride.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.easyride.global.config.OdsayConfig;
import com.easyride.subway.client.OdsaySubwayClient;
import com.easyride.subway.helper.OdsayUriGenerator;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

@RestClientTest({OdsayConfig.class, OdsayUriGenerator.class})
class SubwayServiceTest {

    @Autowired
    OdsayUriGenerator uriGenerator;

    @Autowired
    RestClient.Builder restClientBuilder;

    MockRestServiceServer mockServer;

    OdsaySubwayClient subwayClient;

    SubwayService subwayService;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        subwayClient = new OdsaySubwayClient(restClientBuilder);
        subwayService = new SubwayService(subwayClient);
    }

    @Test
    void 호선과_이름을_기반으로_양옆의_지하철역을_조회한다() throws IOException {
        // given
        configure200MockServer(
                uriGenerator.makeSearchStationUri("오이도"),
                readResourceFile("odsay/success/search-station.json"));

        configure200MockServer(
                uriGenerator.makeStationInfoUri("456"),
                readResourceFile("odsay/success/station-info.json"));

        // when
        NearSubwayStationsResponse response = subwayService.findNearSubwayStations("오이도", 4);

        // then
        assertAll(
                () -> assertThat(response.prevStationName()).isEqualTo("정왕"),
                () -> assertThat(response.nextStationName()).isEqualTo(""),
                () -> mockServer.verify()
        );
    }

    private void configure200MockServer(String requestUri, String responseBody) {
        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    }

    private String readResourceFile(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String resourcePath = classLoader.getResource(fileName).getPath();
        Path path = Path.of(resourcePath);
        return Files.readString(path);
    }
}
