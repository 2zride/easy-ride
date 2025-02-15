package com.easyride.subway.service.dto;

import com.easyride.subway.domain.SubwayStation;

public record SubwayStationDetail(
        int stationLine,
        String stationName,
        String nextStationName
) {

    public SubwayStationDetail(SubwayStation nowStation, SubwayStation nextStation) {
        this(nowStation.getLine().getNumber(), nowStation.getName(), nextStation.getName());
    }
}
