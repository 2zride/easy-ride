package com.easyride.subway.domain;

import com.easyride.subway.entity.SubwayStationEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class SubwayStation {

    private final String id;
    private final String name;
    private final StationLine line;

    public static SubwayStation of(String id, String name, int lineNumber) {
        StationLine line = StationLine.asStationLine(lineNumber);
        return new SubwayStation(id, name, line);
    }

    public static SubwayStation of(String id, String name, StationLine line) {
        return new SubwayStation(id, name, line);
    }

    public static SubwayStation fromEntity(SubwayStationEntity entity) {
        return new SubwayStation(entity.getStationId(), entity.getName(), entity.getLine());
    }
}
