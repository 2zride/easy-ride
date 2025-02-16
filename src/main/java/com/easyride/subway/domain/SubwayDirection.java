package com.easyride.subway.domain;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.exception.SubwayErrorCode;

public enum SubwayDirection {

    UP,
    DOWN,
    INNER,
    OUTER,
    ;

    public boolean isSame(SubwayDirection direction) {
        return this == direction;
    }

    public static SubwayDirection decideDirection(SubwayStation fromStation, SubwayStation toStation) {
        int from = Integer.parseInt(fromStation.getId());
        int to = Integer.parseInt(toStation.getId());
        if (isSeoulMetro2Line(fromStation, toStation)) {
            return decideInnerOuter(from, to);
        }
        return decideUpDown(from, to);
    }

    private static boolean isSeoulMetro2Line(SubwayStation fromStation, SubwayStation toStation) {
        return fromStation.hasLine(StationLine.SEOUL_METRO_2) && toStation.hasLine(StationLine.SEOUL_METRO_2);
    }

    private static SubwayDirection decideInnerOuter(int from, int to) {
        if (from < to) {
            return INNER;
        }
        if (from > to) {
            return OUTER;
        }
        throw new EasyRideException(SubwayErrorCode.INVALID_DIRECTION);
    }

    private static SubwayDirection decideUpDown(int from, int to) {
        if (from > to) {
            return UP;
        }
        if (from < to) {
            return DOWN;
        }
        throw new EasyRideException(SubwayErrorCode.INVALID_DIRECTION);
    }
}
