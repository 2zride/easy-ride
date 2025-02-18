package com.easyride.subway.service;

import static com.easyride.subway.fixture.SubwayFixture.POSITION_2344;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2373;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2389;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2390;
import static com.easyride.subway.fixture.SubwayFixture.POSITION_2413;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.easyride.subway.client.dataseoul.DataSeoulSubwayClient;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.StationLine;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayPath;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import com.easyride.subway.service.dto.SubwayCarCongestionDetail;
import com.easyride.subway.service.dto.SubwayCarCongestionsResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

        NearSubwayStations nearSubwayStations = new NearSubwayStations();
        nearSubwayStations.addStations(List.of(SubwayStation.of("228", "서울대입구", 2)));
        nearSubwayStations.addStations(List.of(SubwayStation.of("230", "신림", 2)));
        given(odsaySubwayClient.fetchStationInfo("229"))
                .willReturn(nearSubwayStations);

        // when
        NearSubwayStationsResponse response = subwayService.findNearSubwayStations("봉천", 2);

        // then
        assertAll(
                () -> assertThat(response.stations()).hasSize(2),
                () -> assertThat(response.stations().get(0).name()).isEqualTo("서울대입구"),
                () -> assertThat(response.stations().get(1).name()).isEqualTo("신림")
        );
    }

    @Test
    void 호선과_이름을_기반으로_양옆의_지하철역을_조회할_때_종점일_경우() {
        // given
        given(odsaySubwayClient.searchStation(anyString()))
                .willReturn(new SubwayStations(List.of(SubwayStation.of("456", "오이도", 4))));

        NearSubwayStations nearSubwayStations = new NearSubwayStations();
        nearSubwayStations.addStations(List.of(SubwayStation.of("455", "정왕", 4)));
        given(odsaySubwayClient.fetchStationInfo("456"))
                .willReturn(nearSubwayStations);

        // when
        NearSubwayStationsResponse response = subwayService.findNearSubwayStations("오이도", 4);

        // then
        assertAll(
                () -> assertThat(response.stations()).hasSize(1),
                () -> assertThat(response.stations().get(0).name()).isEqualTo("정왕")
        );
    }

    @Disabled // TODO 임시
    @Test
    void 현재_지하철역과_다음_지하철역으로_지하철_칸별_혼잡도를_조회한다() {
        // given
        given(dataSeoulSubwayClient.fetchRealTimeSubwayPositions(StationLine.SEOUL_METRO_2))
                .willReturn(new Subways(
                        List.of(POSITION_2390, POSITION_2413, POSITION_2373, POSITION_2344, POSITION_2389)));

        Subway subway = new Subway("2390", null, null, null); // 잠실나루
        List<Integer> carCongestions = List.of(46, 38, 46, 31, 67, 68, 66, 78, 69, 63);
        given(skSubwayClient.fetchCongestion(any(Subway.class)))
                .willReturn(SubwayCongestion.of(subway, 57, carCongestions));

        SubwayStation targetStation = SubwayStation.of("229", "봉천", 2);
        SubwayStation nextStation = SubwayStation.of("230", "신림", 2);
        given(odsaySubwayClient.fetchSubwayPath(any(Subway.class)))
                .willReturn(new SubwayPath(
                        new Subway(null, null, targetStation, nextStation),
                        SubwayDirection.INNER,
                        List.of(nextStation)));

        // when
        SubwayCarCongestionsResponse response = subwayService.findSubwayCongestion(targetStation, nextStation);

        // then
        assertAll(
                () -> assertThat(response.station().line()).isEqualTo(2),
                () -> assertThat(response.station().name()).isEqualTo("봉천"),
                () -> assertThat(response.nextStation().name()).isEqualTo("신림"),
                () -> assertThat(response.carCongestions())
                        .map(SubwayCarCongestionDetail::carCongestion)
                        .hasSameElementsAs(carCongestions)
        );
    }
}
