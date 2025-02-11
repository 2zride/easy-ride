package com.easyride.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.easyride.global.config.BaseRestClientTest;
import com.easyride.subway.client.odsay.OdsayProperty;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.helper.OdsayUriGenerator;
import com.easyride.subway.helper.SkUriGenerator;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest({OdsaySubwayClient.class, OdsayUriGenerator.class, SkSubwayClient.class, SkUriGenerator.class})
class SubwayServiceTest extends BaseRestClientTest {

    SubwayService subwayService;

    OdsaySubwayClient subwayClient;

    @Autowired
    OdsayUriGenerator uriGenerator;

    @Autowired
    OdsayProperty odsayProperty;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        subwayClient = new OdsaySubwayClient(restClientBuilder, odsayProperty);
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
}
