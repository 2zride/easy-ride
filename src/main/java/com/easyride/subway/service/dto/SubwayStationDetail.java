package com.easyride.subway.service.dto;

import com.easyride.subway.domain.StationLine;
import com.easyride.subway.domain.SubwayStation;

public record SubwayStationDetail(
        String id,
        String name,
        StationLine line // TODO enum 값 확인
) {

    public SubwayStationDetail(SubwayStation station) {
        this(station.getId(), station.getName(), station.getLine());
    }
}
