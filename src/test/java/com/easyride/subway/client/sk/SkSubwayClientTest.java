package com.easyride.subway.client.sk;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.easyride.global.config.BaseRestClientTest;
import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.StationLine;
import com.easyride.subway.helper.SkUriGenerator;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest({SkSubwayClient.class, SkUriGenerator.class})
public class SkSubwayClientTest extends BaseRestClientTest {

    SkSubwayClient subwayClient;

    @Autowired
    SkUriGenerator uriGenerator;

    @Autowired
    SkProperty property;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        subwayClient = new SkSubwayClient(restClientBuilder, property);
    }

    @Test
    void 지하철역_호선과_열차번호로_실시간_지하철_혼잡도_조회에_성공한다() throws IOException {
        // given
        String requestUri = uriGenerator.makeRealTimeCongestionUri(2, "2413");
        String responseBody = readResourceFile("sk/success/real-time-congestion.json");
        configure200MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertDoesNotThrow(() -> subwayClient.fetchCongestion(StationLine.SEOUL_METRO_2, "2413")),
                () -> mockServer.verify()
        );
    }

    @Test
    void SK_API가_에러_403를_반환하면_예외를_반환한다() throws IOException {
        // given
        String requestUri = uriGenerator.makeRealTimeCongestionUri(2, "2413");
        String responseBody = readResourceFile("sk/error/error403.json");
        configure403MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.fetchCongestion(StationLine.SEOUL_METRO_2, "2413"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("SK Open API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }

    @Test
    void SK_API가_데이터_조회_실패_응답을_반환하면_예외를_반환한다() throws IOException {
        // given
        String requestUri = uriGenerator.makeRealTimeCongestionUri(1, "2413");
        String responseBody = readResourceFile("sk/error/error101.json");
        configure400MockServer(requestUri, responseBody);

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> subwayClient.fetchCongestion(StationLine.SEOUL_METRO_1, "2413"))
                        .isInstanceOf(EasyRideException.class)
                        .hasMessage("SK Open API 호출 과정에서 예외가 발생했습니다."),
                () -> mockServer.verify()
        );
    }
}
