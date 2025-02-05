package com.easyride.subway.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.easyride.subway.client.dto.OdsaySearchStationSuccessResponse;
import com.easyride.subway.client.dto.OdsaySearchStationSuccessResponse.Station;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OdsaySubwayClientTest {

    @Autowired
    private OdsaySubwayClient subwayClient;

    @Test
    void 오디세이_지하철역명으로_지하철역정보를_가져온다() {
        // given
        OdsaySearchStationSuccessResponse response = subwayClient.searchStation("오이도");

        // when
        List<Station> stations = response.stations();

        // then
        List<Integer> types = stations.stream()
                .map(Station::type)
                .toList();
        assertThat(types).containsExactlyInAnyOrder(4, 116); // 4호선, 수인분당선
    }
}
