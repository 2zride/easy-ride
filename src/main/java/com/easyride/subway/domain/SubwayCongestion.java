package com.easyride.subway.domain;

import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubwayCongestion {

    private static final int FIRST_CAR_NUMBER = 1;

    private final StationLine stationLine;
    private final String trainY;
    private final int trainCongestion;
    private final List<SubwayCarCongestion> carCongestions;

    public static SubwayCongestion of(int subwayLineNumber,
                                      String trainY,
                                      int trainCongestion,
                                      List<Integer> carCongestions) {
        StationLine stationLine = StationLine.asStationLine(subwayLineNumber);
        List<SubwayCarCongestion> subwayCarCongestions = IntStream.rangeClosed(FIRST_CAR_NUMBER, carCongestions.size())
                .mapToObj(carNumber -> new SubwayCarCongestion(carNumber, carCongestions.get(carNumber - 1)))
                .toList();
        return new SubwayCongestion(stationLine, trainY, trainCongestion, subwayCarCongestions);
    }
}
