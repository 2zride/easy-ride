package com.easyride.subway.service;

import com.easyride.subway.client.OdsaySubwayClient;
import com.easyride.subway.domain.SubwayStation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubwayService {

    private final OdsaySubwayClient subwayClient;

    public void findAdjacentSubwayStations(String stationName, int stationLine) {
        List<SubwayStation> searchStations = subwayClient.searchStation(stationName);
        if (searchStations.isEmpty()) {
            throw new RuntimeException("유효하지 않은 이름의 지하철역입니다.");
        }
        String searchStationId = searchStations.stream()
                .filter(searchStation -> searchStation.getLine() == stationLine)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 호선의 지하철역입니다."))
                .getId();
    }
}
