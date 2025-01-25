package com.easyride.bus.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.easyride.bus.config.OdsayProperty;
import com.easyride.bus.domain.GeoLocation;
import com.easyride.bus.domain.StationInfo;
import com.easyride.bus.dto.request.StationSearchRequest;
import com.easyride.bus.mapper.StationInfoMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Import(StationInfoMapper.class)
@RestClientTest(OdsayBusClient.class)
@EnableConfigurationProperties(OdsayProperty.class)
class OdsayBusClientTest {

    @Autowired
    protected MockRestServiceServer mockServer;

    @Autowired
    protected RestClient.Builder restClientBuilder;

    @Autowired
    private OdsayProperty property;

    @Autowired
    private StationInfoMapper stationInfoMapper;

    private OdsayBusClient odsayBusClient;

    @BeforeEach
    void setUp() {
        this.mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        this.odsayBusClient = new OdsayBusClient(property, restClientBuilder, stationInfoMapper);
    }

    @DisplayName("정류장 id와 정류장에 다니는 버스를 조회한다.")
    @Test
    void searchStationInfo() throws IOException {
        long expectedStationId = 185660L;
        List<String> expectedBusNumbers = List.of(
                "103", "83", "80", "1-1", "7002", "1303", "13", "06-2", "05-1", "10", "05", "06-1(A)", "06", "7200"
        );
        String stationSearchValue = "인덕원역";
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        StationSearchRequest request = new StationSearchRequest(stationSearchValue, stationGeoLocation);

        setMockServer(stationSearchValue, "odsay/success.json");

        StationInfo stationInfo = odsayBusClient.searchStationInfo(request);

        assertAll(
                () -> assertThat(stationInfo.getStationId()).isEqualTo(expectedStationId),
                () -> assertThat(stationInfo.getBusNumbers()).containsExactlyElementsOf(expectedBusNumbers),
                () -> mockServer.verify()
        );
    }

    @DisplayName("필수값 포멧 에러 시, 에러를 반환한다")
    @Test
    void errorCode8() throws IOException {
        String stationSearchValue = "_";
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        StationSearchRequest request = new StationSearchRequest(stationSearchValue, stationGeoLocation);

        setMockServer(stationSearchValue, "odsay/error8.json");

        assertAll(
                () -> assertThatThrownBy(() -> odsayBusClient.searchStationInfo(request))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessage("400 에러"),
                () -> mockServer.verify()
        );
    }

    @DisplayName("필수값 생략 시 에러를 반환한다")
    @Test
    void errorCode9() throws IOException {
        String stationSearchValue = "";
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        StationSearchRequest request = new StationSearchRequest(stationSearchValue, stationGeoLocation);

        setMockServer(stationSearchValue, "odsay/error9.json");

        assertAll(
                () -> assertThatThrownBy(() -> odsayBusClient.searchStationInfo(request))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessage("400 에러"),
                () -> mockServer.verify()
        );
    }

    @DisplayName("오디세이 서버 에러 시, 서버에러를 발생시킨다")
    @Test
    void errorCode500() throws IOException {
        String stationSearchValue = "";
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        StationSearchRequest request = new StationSearchRequest(stationSearchValue, stationGeoLocation);

        setMockServer(stationSearchValue, "odsay/error500.json");

        assertAll(
                () -> assertThatThrownBy(() -> odsayBusClient.searchStationInfo(request))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessage("서버 에러"),
                () -> mockServer.verify()
        );
    }

    private void setMockServer(String searchingStation, String responseClassPath) throws IOException {
        String requestUri = makeUri(searchingStation);
        String response = makeResponseByPath(responseClassPath);

        mockServer.expect(requestTo(requestUri))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
    }

    private String makeUri(String searchingStation) {
        String encodeStationName = URLEncoder.encode(searchingStation, StandardCharsets.UTF_8);
        return UriComponentsBuilder.fromHttpUrl(property.baseUrl())
                .queryParam("apiKey", property.apiKey())
                .queryParam("stationName", encodeStationName)
                .queryParam("stationClass", "1")
                .build()
                .toUriString();
    }

    private String makeResponseByPath(String path) throws IOException {
        return new String(Files.readAllBytes(
                new ClassPathResource(path).getFile().toPath())
        );
    }
}
