package com.easyride.subway.domain;

import java.util.List;

public class SubwayStations {

    private final List<SubwayStation> stations;

    public SubwayStations(List<SubwayStation> stations) {
        if (stations.isEmpty()) {
            throw new RuntimeException("해당되는 지하철역이 없습니다.");
        }
        this.stations = stations;
    }

    public String fetchStationIdByStationLine(int stationLine) {
        return stations.stream()
                .filter(station -> station.getLine() == stationLine)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 호선의 지하철역입니다."))
                .getId();
    }
}
