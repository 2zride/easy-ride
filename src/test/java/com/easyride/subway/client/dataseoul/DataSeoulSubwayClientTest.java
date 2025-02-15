package com.easyride.subway.client.dataseoul;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.easyride.global.config.BaseRestClientTest;
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
                        .map(SubwayPosition::getSubwayNumber)
                        .containsExactly("2390", "2413", "2373", "2344", "2389"),
                () -> mockServer.verify()
        );
    }
}
