package com.easyride.subway.domain;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.exception.SubwayErrorCode;
import java.util.List;

public class SubwayStations {

    private final List<SubwayStation> stations;

    public SubwayStations(List<SubwayStation> stations) {
        if (stations.isEmpty()) {
            throw new EasyRideException(SubwayErrorCode.INVALID_STATION);
        }
        this.stations = stations;
    }

    public String fetchStationIdByStationLine(int stationLine) {
        return stations.stream()
                .filter(station -> station.getLine() == stationLine)
                .findFirst()
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.INVALID_STATION))
                .getId();
    }
}
