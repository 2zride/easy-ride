package com.easyride.subway.service;

import static com.easyride.subway.fixture.SubwayFixture.SUBWAY_2344;
import static com.easyride.subway.fixture.SubwayFixture.SUBWAY_2373;
import static com.easyride.subway.fixture.SubwayFixture.SUBWAY_2389;
import static com.easyride.subway.fixture.SubwayFixture.SUBWAY_2390;
import static com.easyride.subway.fixture.SubwayFixture.SUBWAY_2413;
import static com.easyride.subway.fixture.SubwayFixture.SUBWAY_2490;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.domain.SubwayWithPath;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import com.easyride.subway.service.dto.SubwayCarCongestionDetail;
import com.easyride.subway.service.dto.SubwayCarCongestionsResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SubwayServiceTest {

    SubwayService subwayService;

    @Mock
    SubwayClientProcessor clientProcessor;

    @BeforeEach
    void setUp() {
        subwayService = new SubwayService(clientProcessor);
    }

    @Test
    void 호선과_이름을_기반으로_양옆의_지하철역을_조회한다() {
        // given
        given(clientProcessor.searchStation(anyString()))
                .willReturn(new SubwayStations(List.of(SubwayStation.of("229", "봉천", 2))));

        NearSubwayStations nearSubwayStations = new NearSubwayStations();
        nearSubwayStations.addStations(List.of(SubwayStation.of("228", "서울대입구", 2)));
        nearSubwayStations.addStations(List.of(SubwayStation.of("230", "신림", 2)));
        given(clientProcessor.fetchNearStations(any(SubwayStation.class)))
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
        given(clientProcessor.searchStation(anyString()))
                .willReturn(new SubwayStations(List.of(SubwayStation.of("456", "오이도", 4))));

        NearSubwayStations nearSubwayStations = new NearSubwayStations();
        nearSubwayStations.addStations(List.of(SubwayStation.of("455", "정왕", 4)));
        given(clientProcessor.fetchNearStations(any(SubwayStation.class)))
                .willReturn(nearSubwayStations);

        // when
        NearSubwayStationsResponse response = subwayService.findNearSubwayStations("오이도", 4);

        // then
        assertAll(
                () -> assertThat(response.stations()).hasSize(1),
                () -> assertThat(response.stations().get(0).name()).isEqualTo("정왕")
        );
    }

    @Test
    void 기준_지하철역과_다음_지하철역으로_지하철_칸별_혼잡도를_조회한다() {
        // given
        given(clientProcessor.fetchSubwayDirection(any(SubwayStation.class), any(SubwayStation.class)))
                .willReturn(SubwayDirection.INNER);

        Subways subways = new Subways();
        subways.add(SUBWAY_2390);
        subways.add(SUBWAY_2413);
        subways.add(SUBWAY_2373);
        subways.add(SUBWAY_2344);
        subways.add(SUBWAY_2389);
        given(clientProcessor.fetchRealTimeSubways(any(SubwayStation.class)))
                .willReturn(subways);

        List<Integer> carCongestions = List.of(46, 38, 46, 31, 67, 68, 66, 78, 69, 63);
        given(clientProcessor.fetchSubwayCongestion(any(Subway.class)))
                .willReturn(SubwayCongestion.of(SUBWAY_2390, 57, carCongestions));

        SubwayStation targetStation = SubwayStation.of("229", "봉천", 2);
        SubwayStation nextStation = SubwayStation.of("230", "신림", 2);
        given(clientProcessor.fetchSubwayPath(any(Subway.class)))
                .willReturn(new SubwayWithPath(SUBWAY_2490, SUBWAY_2490.getDirection(), List.of(targetStation)));

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
