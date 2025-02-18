package com.easyride.subway.domain;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.exception.SubwayErrorCode;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubwayStations {

    private final List<SubwayStation> stations;

    public boolean isEmpty() {
        return this.stations.isEmpty();
    }

    public SubwayStation filter(Predicate<SubwayStation> condition, SubwayErrorCode errorCodeIfNotExist) {
        return stations.stream()
                .filter(condition)
                .findAny()
                .orElseThrow(() -> new EasyRideException(errorCodeIfNotExist));
    }
}
