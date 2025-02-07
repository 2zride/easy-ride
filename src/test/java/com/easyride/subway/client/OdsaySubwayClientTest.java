package com.easyride.subway.client;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.easyride.global.config.OdsayConfig;
import com.easyride.global.config.OdsayProperty;
import com.easyride.subway.domain.SubwayStations;
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
import org.springframework.web.util.UriComponentsBuilder;

@RestClientTest({OdsayConfig.class})
class OdsaySubwayClientTest {

    @Autowired
    OdsayProperty odsayProperty;

    @Autowired
    RestClient.Builder restClientBuilder;

    MockRestServiceServer mockServer;

    OdsaySubwayClient subwayClient;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        subwayClient = new OdsaySubwayClient(restClientBuilder);
    }

    @Test
    void 지하철역_이름으로_지하철역_상세정보조회에_성공하면_도메인을_반환한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/success/search-station.json");
        configureMockServer(responseBody);

        // when
        SubwayStations subwayStations = subwayClient.searchStation("오이도");

        // then
        assertAll(
                () -> assertDoesNotThrow(() -> subwayStations.fetchStationIdByStationLine(4)),
                () -> assertDoesNotThrow(() -> subwayStations.fetchStationIdByStationLine(116)),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러_500를_반환하면_예외를_반환한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/error/error500.json");
        configureMockServer(responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러_8를_반환하면_예외를_반환한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/error/error8.json");
        configureMockServer(responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러_9를_반환하면_예외를_반환한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/error/error9.json");
        configureMockServer(responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    private void configureMockServer(String responseBody) {
        mockServer.expect(requestTo(makeUri()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    }

    private String makeUri() {
        return UriComponentsBuilder.fromUriString(odsayProperty.baseUrl())
                .path("/searchStation")
                .queryParam("apiKey", odsayProperty.apiKey())
                .queryParam("stationName", "오이도")
                .queryParam("stationClass", 2)
                .toUriString();
    }

    private String readResourceFile(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String resourcePath = classLoader.getResource(fileName).getPath();
        Path path = Path.of(resourcePath);
        return Files.readString(path);
    }
}
