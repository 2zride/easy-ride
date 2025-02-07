package com.easyride.subway.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.easyride.global.config.OdsayConfig;
import com.easyride.global.config.OdsayProperty;
import com.easyride.subway.client.dto.OdsaySearchStationResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    void 지하철역_이름으로_지하철역_상세정보를_조회한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/success/search-station.json");
        configureMockServer(responseBody);

        // when
        OdsaySearchStationResponse response = subwayClient.searchStation("오이도");

        // then
        List<Integer> types = response.stationTypes();
        assertAll(
                () -> assertThat(types).containsExactlyInAnyOrder(4, 116), // 4호선, 수인분당선
                () -> assertThat(response.isError()).isFalse(),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러500를_반환하면_에러응답을_반환한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/error/error500.json");
        configureMockServer(responseBody);

        // when
        OdsaySearchStationResponse response = subwayClient.searchStation("오이도");

        // then
        assertAll(
                () -> assertThat(response.isError()).isTrue(),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러8를_반환하면_에러응답을_반환한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/error/error8.json");
        configureMockServer(responseBody);

        // when
        OdsaySearchStationResponse response = subwayClient.searchStation("오이도");

        // then
        assertAll(
                () -> assertThat(response.isError()).isTrue(),
                () -> mockServer.verify()
        );
    }

    @Test
    void 오디세이가_에러9를_반환하면_에러응답을_반환한다() throws IOException {
        // given
        String responseBody = readResourceFile("odsay/error/error9.json");
        configureMockServer(responseBody);

        // when
        OdsaySearchStationResponse response = subwayClient.searchStation("오이도");

        // then
        assertAll(
                () -> assertThat(response.isError()).isTrue(),
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
