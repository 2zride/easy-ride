package com.easyride.subway.entity;

import static com.easyride.subway.domain.StationLine.SEOUL_METRO_4;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SubwayStationRepositoryTest {

    @Autowired
    SubwayStationRepository stationRepository;

    @Test
    void 지하철역_이름과_호선을_기반으로_지하철역을_조회한다() {
        // given
        SubwayStationEntity expectedValue = new SubwayStationEntity(232L, "인덕원", "440", "440", "1455", SEOUL_METRO_4);

        // when
        Optional<SubwayStationEntity> actual = stationRepository.findByNameAndLine("인덕원", SEOUL_METRO_4);

        // then
        assertThat(actual)
                .hasValue(expectedValue);
    }
}
