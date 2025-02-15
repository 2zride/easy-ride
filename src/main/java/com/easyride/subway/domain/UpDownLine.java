package com.easyride.subway.domain;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.exception.SubwayErrorCode;

public enum UpDownLine {

    UP,
    DOWN,
    ;

    public static UpDownLine decideUpDownLine(SubwayStation startStation, SubwayStation endStation) {
        int start = Integer.parseInt(startStation.getId());
        int end = Integer.parseInt(endStation.getId());
        if (end - start > 0) {
            return DOWN;
        }
        if (end - start < 0) {
            return UP;
        }
        throw new EasyRideException(SubwayErrorCode.INVALID_UP_DOWN_LINE);
    }
}
