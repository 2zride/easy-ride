package com.easyride.subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SubwayCongestion {

    private static final int FIRST_CAR_NUMBER = 1;

    private final Subway subway;
    private final int subwayCongestion;
    private final List<SubwayCarCongestion> carCongestions;

    public static SubwayCongestion of(Subway subway, int subwayCongestion, List<Integer> carCongestions) {
        List<SubwayCarCongestion> subwayCarCongestions = IntStream.rangeClosed(FIRST_CAR_NUMBER, carCongestions.size())
                .mapToObj(carNumber -> new SubwayCarCongestion(carNumber, carCongestions.get(carNumber - 1)))
                .toList();
        return new SubwayCongestion(subway, subwayCongestion, subwayCarCongestions);
    }

    public List<SubwayCarCongestion> getCarCongestions() {
        return Collections.unmodifiableList(carCongestions);
    }
}
