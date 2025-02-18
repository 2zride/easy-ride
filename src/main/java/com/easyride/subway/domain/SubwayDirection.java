package com.easyride.subway.domain;

public enum SubwayDirection {

    UP,
    DOWN,
    INNER,
    OUTER,
    ;

    public boolean isSame(SubwayDirection direction) {
        return this == direction;
    }
}
