package com.easyride.subway.domain;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StationLine {

    SEOUL_METRO_1(1, "수도권 1호선"),
    SEOUL_METRO_2(2, "수도권 2호선"),
    SEOUL_METRO_3(3, "수도권 3호선"),
    SEOUL_METRO_4(4, "수도권 4호선"),
    SEOUL_METRO_5(5, "수도권 5호선"),
    SEOUL_METRO_6(6, "수도권 6호선"),
    SEOUL_METRO_7(7, "수도권 7호선"),
    SEOUL_METRO_8(8, "수도권 8호선"),
    SEOUL_METRO_9(9, "수도권 9호선"),
    ETC(0, "지원 예정 호선"),
    ;

    private final int number;
    private final String description;

    public static StationLine asStationLine(int number) {
        return Arrays.stream(values())
                .filter(subwayLine -> subwayLine.number == number)
                .findAny()
                .orElse(ETC);
    }
}
