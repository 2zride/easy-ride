package com.easyride.subway.client.dataseoul;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.StationLine;
import com.easyride.subway.exception.SubwayErrorCode;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataSeoulStationLineName {

    SEOUL_METRO_1(StationLine.SEOUL_METRO_1, "1호선"),
    SEOUL_METRO_2(StationLine.SEOUL_METRO_2, "2호선"),
    SEOUL_METRO_3(StationLine.SEOUL_METRO_3, "3호선"),
    SEOUL_METRO_4(StationLine.SEOUL_METRO_4, "4호선"),
    SEOUL_METRO_5(StationLine.SEOUL_METRO_5, "5호선"),
    SEOUL_METRO_6(StationLine.SEOUL_METRO_6, "6호선"),
    SEOUL_METRO_7(StationLine.SEOUL_METRO_7, "7호선"),
    SEOUL_METRO_8(StationLine.SEOUL_METRO_8, "8호선"),
    SEOUL_METRO_9(StationLine.SEOUL_METRO_9, "9호선"),
    ;

    private final StationLine stationLine;
    private final String stationLineName;

    public static String asStationLineName(StationLine stationLine) {
        return Arrays.stream(values())
                .filter(value -> value.stationLine == stationLine)
                .findAny()
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.DATA_SEOUL_INVALID_STATION_LINE_NAME))
                .stationLineName;
    }
}
