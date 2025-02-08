package com.easyride.subway.domain;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NearSubwayStations {

    @Nullable
    private final SubwayStation prevStation;
    @Nullable
    private final SubwayStation nextStation;
}
