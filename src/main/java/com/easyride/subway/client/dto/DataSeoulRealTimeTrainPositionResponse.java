package com.easyride.subway.client.dto;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.SubwayPosition;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.UpDownLine;
import com.easyride.subway.exception.SubwayErrorCode;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

public record DataSeoulRealTimeTrainPositionResponse(
        DataSeoulErrorResponse errorMessage,
        List<RealTimePosition> realtimePositionList
) {

    public List<SubwayPosition> toSubwayPositions() {
        return realtimePositionList.stream()
                .map(position -> new SubwayPosition(position.trainNumber,
                        UpDownLineMapper.asUpDownLine(position.upDownLine),
                        toSubwayStation(position.stationId, position.stationName),
                        toSubwayStation(position.endStationId, position.endStationName)))
                .toList();
    }

    private SubwayStation toSubwayStation(String id, String name) {
        return SubwayStation.of(id.substring(7), name, id.charAt(3));
    }

    private record RealTimePosition(
            Integer totalCount,

            @JsonAlias("rowNum")
            Integer rowNumber,

            @JsonAlias("subwayId")
            String stationLineId,

            @JsonAlias("statnId")
            String stationId, // ex. 1002000209

            @JsonAlias("statnNm")
            String stationName, // ex. 한양대

            @JsonAlias("statnTid")
            String endStationId,

            @JsonAlias("statnTnm")
            String endStationName,

            @JsonAlias("updnLine")
            String upDownLine, // 0 상행/내선 1 하행/외선

            @JsonAlias("trainNo")
            String trainNumber,

            @JsonAlias("recptnDt")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime requestTime
    ) {
    }

    @RequiredArgsConstructor
    private enum UpDownLineMapper {

        UP("0", UpDownLine.UP),
        DOWN("1", UpDownLine.DOWN),
        ;

        private final String value;
        private final UpDownLine upDownLine;

        private static UpDownLine asUpDownLine(String value) {
            return Arrays.stream(values())
                    .filter(mapper -> mapper.value.equals(value))
                    .findAny()
                    .orElseThrow(() -> new EasyRideException(SubwayErrorCode.DATA_SEOUL_INVALID_UP_DOWN_LINE))
                    .upDownLine;
        }
    }
}
