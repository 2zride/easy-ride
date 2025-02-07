package com.easyride.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NearSubwayStations {

    private final SubwayStation prevStation;
    private final SubwayStation nextStation;
}
