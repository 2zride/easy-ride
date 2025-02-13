package com.easyride.subway.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubwayPosition {

    private final String subwayNumber;
    private final UpDownLine upDownLine;
    private final SubwayStation nowStation;
    private final SubwayStation endStation;
}
