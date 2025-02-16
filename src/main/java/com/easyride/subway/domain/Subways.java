package com.easyride.subway.domain;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.exception.SubwayErrorCode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Subways {

    private final List<Subway> subways;

    public Subway findApproachingSubway(SubwayStation targetStation, SubwayStation nextStation) {
        SubwayDirection direction = SubwayDirection.decideDirection(targetStation, nextStation);
        return subways.stream()
                .filter(subway -> subway.isSameDirection(direction))
                .filter(subway -> subway.willPass(targetStation))
                .min(Comparator.comparingInt(subway -> subway.distanceFrom(targetStation)))
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.NO_APPROACHING_SUBWAY));
    }

    public List<Subway> getSubways() {
        return Collections.unmodifiableList(subways);
    }
}
