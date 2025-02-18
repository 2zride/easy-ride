package com.easyride.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id", "direction", "nowStation", "endStation"})
public class Subway {

    private final String id;
    private final SubwayDirection direction;
    private final SubwayStation nowStation;
    private final SubwayStation endStation;

    public boolean isDifferentDirection(SubwayDirection direction) {
        return this.direction.isDifferent(direction);
    }

    public StationLine stationLine() {
        return this.nowStation.getLine();
    }
}
