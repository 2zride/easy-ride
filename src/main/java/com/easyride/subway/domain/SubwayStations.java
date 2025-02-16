package com.easyride.subway.domain;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.exception.SubwayErrorCode;
import java.util.List;

public class SubwayStations {

    private final List<SubwayStation> stations;

    public SubwayStations(List<SubwayStation> stations) {
        validate(stations);
        this.stations = stations;
    }

    private void validate(List<SubwayStation> stations) {
        if (stations.isEmpty()) {
            throw new EasyRideException(SubwayErrorCode.INVALID_STATION);
        }
    }

    public SubwayStation findStationByStationLine(int stationLineNumber) {
        StationLine stationLine = StationLine.asStationLine(stationLineNumber);
        return stations.stream()
                .filter(station -> station.hasLine(stationLine))
                .findAny()
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.INVALID_STATION));
    }
}
