package com.easyride.subway.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.easyride.global.config.OdsayConfig;
import com.easyride.global.config.OdsayProperty;
import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayStation;
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
        String requestUri = makeSearchStationUri("오이도");
        String responseBody = readResourceFile("odsay/success/search-station.json");
        configure200MockServer(requestUri, responseBody);

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
    void 지하철역_ID로_양옆_지하철역_세부정보조회에_성공하면_도메인을_반환한다() throws IOException {
        // given
        String requestUri = makeStationInfoUri("456"); // 오이도
        String responseBody = readResourceFile("odsay/success/station-info.json");
        configure200MockServer(requestUri, responseBody);

        // when
        NearSubwayStations nearStations = subwayClient.fetchStationInfo("456");
        SubwayStation prevStation = nearStations.getPrevStation();
        SubwayStation nextStation = nearStations.getNextStation();

        // then
        assertAll(
                () -> assertThat(prevStation).isNotNull(),
                () -> assertThat(nextStation).isNull(),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러_500를_반환하면_예외를_반환한다() throws IOException {
        // given
        String requestUri = makeSearchStationUri("오이도");
        String responseBody = readResourceFile("odsay/error/error500.json");
        configure200MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러_8를_반환하면_예외를_반환한다() throws IOException {
        // given
        String requestUri = makeSearchStationUri("오이도");
        String responseBody = readResourceFile("odsay/error/error8.json");
        configure200MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러_9를_반환하면_예외를_반환한다() throws IOException {
        // given
        String requestUri = makeSearchStationUri("오이도");
        String responseBody = readResourceFile("odsay/error/error9.json");
        configure200MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_400_에러를_반환하면_예외를_반환한다() {
        // given
        String requestUri = makeSearchStationUri("오이도");
        configure400MockServer(requestUri);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    private void configure200MockServer(String requestUri, String responseBody) {
        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    }

    private void configure400MockServer(String requestUri) {
        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());
    }

    private String makeSearchStationUri(String stationName) {
        return UriComponentsBuilder.fromUriString(odsayProperty.baseUrl())
                .path("/searchStation")
                .queryParam("apiKey", odsayProperty.apiKey())
                .queryParam("stationName", stationName)
                .queryParam("stationClass", 2)
                .toUriString();
    }

    private String makeStationInfoUri(String stationId) {
        return UriComponentsBuilder.fromUriString(odsayProperty.baseUrl())
                .path("/subwayStationInfo")
                .queryParam("apiKey", odsayProperty.apiKey())
                .queryParam("stationID", stationId)
                .toUriString();
    }

    private String readResourceFile(String fileName) throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        String resourcePath = classLoader.getResource(fileName).getPath();
        Path path = Path.of(resourcePath);
        return Files.readString(path);
    }
}
