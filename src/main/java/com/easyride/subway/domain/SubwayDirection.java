package com.easyride.subway.domain;

public enum SubwayDirection {

    UP,
    DOWN,
    INNER,
    OUTER,
    ;

    public boolean isDifferent(SubwayDirection direction) {
        return this != direction;
    }
}
