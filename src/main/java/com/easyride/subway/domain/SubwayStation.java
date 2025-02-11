package com.easyride.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubwayStation {

    private final String id;
    private final String name;
    private final StationLine line;

    public static SubwayStation of(String id, String name, int lineNumber) {
        StationLine line = StationLine.asStationLine(lineNumber);
        return new SubwayStation(id, name, line);
    }
}
