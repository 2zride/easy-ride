package com.easyride.subway.service;

import static com.easyride.subway.fixture.SubwayFixture.POSITION_2344;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2373;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2389;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2390;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2413;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.easyride.subway.client.dataseoul.DataSeoulSubwayClient;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import com.easyride.subway.service.dto.SubwayCarCongestionDetail;
import com.easyride.subway.service.dto.SubwayCarCongestionsResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SubwayServiceTest { // TODO ServiceTest 생성

    SubwayService subwayService;

    @Mock
    OdsaySubwayClient odsaySubwayClient;

    @Mock
    DataSeoulSubwayClient dataSeoulSubwayClient;

    @Mock
    SkSubwayClient skSubwayClient;

    @BeforeEach
    void setUp() {
        subwayService = new SubwayService(odsaySubwayClient, dataSeoulSubwayClient, skSubwayClient);
    }

    @Test
    void 호선과_이름을_기반으로_양옆의_지하철역을_조회한다() {
        // given
        given(odsaySubwayClient.searchStation("봉천"))
                .willReturn(new SubwayStations(List.of(SubwayStation.of("229", "봉천", 2))));

        given(odsaySubwayClient.fetchStationInfo("229"))
                .willReturn(new NearSubwayStations(
                        SubwayStation.of("228", "서울대입구", 2),
                        SubwayStation.of("230", "신림", 2)));

        // when
        NearSubwayStationsResponse response = subwayService.findNearSubwayStations("봉천", 2);

        // then
        assertAll(
                () -> assertThat(response.prevStationName()).isEqualTo("서울대입구"),
                () -> assertThat(response.nextStationName()).isEqualTo("신림")
        );
    }

    @Test
    void 호선과_이름을_기반으로_양옆의_지하철역을_조회할_때_종점일_경우_빈_문자열을_반환한다() {
        // given
        given(odsaySubwayClient.searchStation(anyString()))
                .willReturn(new SubwayStations(List.of(SubwayStation.of("456", "오이도", 4))));

        given(odsaySubwayClient.fetchStationInfo("456"))
                .willReturn(new NearSubwayStations(SubwayStation.of("455", "정왕", 4), null));

        // when
        NearSubwayStationsResponse response = subwayService.findNearSubwayStations("오이도", 4);

        // then
        assertAll(
                () -> assertThat(response.prevStationName()).isEqualTo("정왕"),
                () -> assertThat(response.nextStationName()).isEqualTo("")
        );
    }

    @Test
    void 현재_지하철역과_다음_지하철역으로_지하철_칸별_혼잡도를_조회한다() {
        // given
        given(dataSeoulSubwayClient.fetchRealTimeSubwayPositions("2호선"))
                .willReturn(List.of(POSITION_2390, POSITION_2413, POSITION_2373, POSITION_2344, POSITION_2389));

        List<Integer> carCongestions = List.of(46, 38, 46, 31, 67, 68, 66, 78, 69, 63);
        given(skSubwayClient.fetchRealTimeCongestion(2, "2390")) // 잠실나루
                .willReturn(SubwayCongestion.of(2, "2390", 57, carCongestions));

        // when
        SubwayCarCongestionsResponse response = subwayService.findSubwayCongestion(
                SubwayStation.of("229", "봉천", 2),
                SubwayStation.of("230", "신림", 2)); // 봉천 -> 신림 (DOWN)

        // then
        assertAll(
                () -> assertThat(response.station().stationLine()).isEqualTo(2),
                () -> assertThat(response.station().stationName()).isEqualTo("봉천"),
                () -> assertThat(response.station().nextStationName()).isEqualTo("신림"),
                () -> assertThat(response.carCongestions())
                        .map(SubwayCarCongestionDetail::carCongestion)
                        .hasSameElementsAs(carCongestions)
        );
    }
}
