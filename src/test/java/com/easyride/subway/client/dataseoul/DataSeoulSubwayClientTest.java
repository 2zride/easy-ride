package com.easyride.subway.client.dataseoul;

import static com.easyride.subway.fixture.SubwayFixture.POSITION_2344;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2373;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2389;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2390;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2413;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.easyride.global.config.BaseRestClientTest;
import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.SubwayPosition;
import com.easyride.subway.helper.DataSeoulUriGenerator;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest({DataSeoulSubwayClient.class, DataSeoulUriGenerator.class})
class DataSeoulSubwayClientTest extends BaseRestClientTest {

    DataSeoulSubwayClient subwayClient;

    @Autowired
    DataSeoulUriGenerator uriGenerator;

    @Autowired
    DataSeoulProperty property;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        subwayClient = new DataSeoulSubwayClient(restClientBuilder, property);
    }

    @Test
    void 지하철역_호선_이름으로_실시간_지하철_위치정보조회에_성공하면_도메인을_반환한다() throws IOException {
        // given
        String requestUri = uriGenerator.makeRealTimeTrainPositionUri("2호선");
        String responseBody = readResourceFile("dataseoul/success/real-time-train-position.json");
        configure200MockServer(requestUri, responseBody);

        // when
        List<SubwayPosition> subwayPositions = subwayClient.fetchRealTimeSubwayPositions("2호선");

        // then
        assertAll(
                () -> assertThat(subwayPositions)
                        .containsExactly(POSITION_2390, POSITION_2413, POSITION_2373, POSITION_2344, POSITION_2389),
                () -> mockServer.verify()
        );
    }

    @Test
    void 지하철역_호선_이름으로_실시간_지하철_위치정보조회에_실패하면_예외를_반환한다() throws IOException {
        // given
        String requestUri = uriGenerator.makeRealTimeTrainPositionUri("2호선");
        String responseBody = readResourceFile("dataseoul/error/error300.json");
        configure200MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.fetchRealTimeSubwayPositions("2호선"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("서울시 공공데이터 API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    @Test
    void 지하철역_호선_이름으로_실시간_지하철_위치정보조회시_빈결과라면_예외를_반환한다() throws IOException {
        // given
        String requestUri = uriGenerator.makeRealTimeTrainPositionUri("2호선");
        String responseBody = readResourceFile("dataseoul/success/info200.json");
        configure200MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.fetchRealTimeSubwayPositions("2호선"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("해당 호선에서 현재 운행 중인 지하철이 없습니다."),
                () -> mockServer.verify()
        );
    }
}
