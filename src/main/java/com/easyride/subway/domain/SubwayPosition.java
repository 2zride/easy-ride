package com.easyride.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"subwayNumber", "upDownLine", "nowStation", "endStation"})
public class SubwayPosition {

    private final String subwayNumber;
    private final UpDownLine upDownLine;
    private final SubwayStation nowStation;
    private final SubwayStation endStation;

    public boolean isSameDirection(UpDownLine direction) {
        return this.upDownLine == direction;
    }

    public boolean isBeforeEndStation(UpDownLine direction, SubwayStation targetStation) {
        if (direction == UpDownLine.UP) {
            return Integer.parseInt(targetStation.getId()) >= Integer.parseInt(endStation.getId());
        }
        return Integer.parseInt(targetStation.getId()) <= Integer.parseInt(endStation.getId());
    }

    public int distanceFrom(SubwayStation targetStation) {
        return Math.abs(Integer.parseInt(targetStation.getId()) - Integer.parseInt(nowStation.getId()));
    }

    public StationLine fetchStationLine() {
        return this.nowStation.getLine();
    }
}
