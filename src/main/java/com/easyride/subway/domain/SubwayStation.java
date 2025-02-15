package com.easyride.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class SubwayStation {

    private final String id;
    private final String name;
    private final StationLine line;

    public static SubwayStation of(String id, String name, int lineNumber) {
        StationLine line = StationLine.asStationLine(lineNumber);
        return new SubwayStation(id, name, line);
    }
}
