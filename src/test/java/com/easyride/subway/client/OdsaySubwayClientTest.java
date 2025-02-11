package com.easyride.subway.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.easyride.global.config.BaseRestClientTest;
import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.client.odsay.OdsayProperty;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.helper.OdsayUriGenerator;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest({OdsaySubwayClient.class, OdsayUriGenerator.class})
class OdsaySubwayClientTest extends BaseRestClientTest {

    OdsaySubwayClient subwayClient;

    @Autowired
    OdsayUriGenerator uriGenerator;

    @Autowired
    OdsayProperty property;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        subwayClient = new OdsaySubwayClient(restClientBuilder, property);
    }

    @Test
    void 지하철역_이름으로_지하철역_상세정보조회에_성공하면_도메인을_반환한다() throws IOException {
        // given
        String requestUri = uriGenerator.makeSearchStationUri("오이도");
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
        String requestUri = uriGenerator.makeStationInfoUri("456"); // 오이도
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
        String requestUri = uriGenerator.makeSearchStationUri("오이도");
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
        String requestUri = uriGenerator.makeSearchStationUri("오이도");
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
        String requestUri = uriGenerator.makeSearchStationUri("오이도");
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
        String requestUri = uriGenerator.makeSearchStationUri("오이도");
        configure400MockServer(requestUri);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.searchStation("오이도"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("오디세이 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }
}
