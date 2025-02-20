package com.easyride.subway.service.dto;

import com.easyride.subway.domain.SubwayStation;

public record SubwayStationDetail(
        String id,
        String name,
        int line
) {

    public SubwayStationDetail(SubwayStation station) {
        this(station.getId(), station.getName(), station.getLine().getNumber());
    }
}
