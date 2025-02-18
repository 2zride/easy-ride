package com.easyride.subway.client.dto;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.exception.SubwayErrorCode;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

public record DataSeoulRealTimeSubwayResponse(
        DataSeoulErrorResponse errorMessage,
        List<RealTimePosition> realtimePositionList
) {

    public Subways toSubways() {
        Subways subways = new Subways();
        realtimePositionList.stream()
                .map(this::toSubway)
                .forEach(subways::add);
        return subways;
    }

    private Subway toSubway(RealTimePosition position) {
        return new Subway(position.subwayNumber,
                SubwayDirectionMapper.asDirection(position.direction, position.stationLineId.charAt(3)),
                toSubwayStation(position.stationId, position.stationName),
                toSubwayStation(position.endStationId, position.endStationName));
    }

    private SubwayStation toSubwayStation(String id, String name) {
        return SubwayStation.of(id.substring(7), name, id.charAt(3));
    }

    private record RealTimePosition(
            Integer totalCount,

            @JsonAlias("rowNum")
            Integer rowNumber,

            @JsonAlias("subwayId")
            String stationLineId, // ex. 1002

            @JsonAlias("statnId")
            String stationId, // ex. 1002000209 TODO 엣지케이스 고려 (특히 2호선)

            @JsonAlias("statnNm")
            String stationName, // ex. 한양대

            @JsonAlias("statnTid")
            String endStationId,

            @JsonAlias("statnTnm")
            String endStationName,

            @JsonAlias("updnLine")
            String direction, // 0 상행/내선 1 하행/외선

            @JsonAlias("trainNo")
            String subwayNumber,

            @JsonAlias("recptnDt")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime requestTime
    ) {
    }

    @RequiredArgsConstructor
    private enum SubwayDirectionMapper {

        UP_MAPPER("0", SubwayDirection.UP),
        DOWN_MAPPER("1", SubwayDirection.DOWN),
        INNER_MAPPER("0", SubwayDirection.INNER),
        OUTER_MAPPER("1", SubwayDirection.OUTER),
        ;

        private final String value;
        private final SubwayDirection direction;

        private static SubwayDirection asDirection(String value, char stationLine) {
            if (stationLine == '2') {
                return asDirectionWhenSeoulMetro2Line(value);
            }
            return Arrays.stream(values())
                    .filter(mapper -> mapper.value.equals(value))
                    .findAny()
                    .orElseThrow(() -> new EasyRideException(SubwayErrorCode.INVALID_DIRECTION))
                    .direction;
        }

        private static SubwayDirection asDirectionWhenSeoulMetro2Line(String value) {
            if (value.equals(INNER_MAPPER.value)) {
                return INNER_MAPPER.direction;
            }
            if (value.equals(OUTER_MAPPER.value)) {
                return OUTER_MAPPER.direction;
            }
            throw new EasyRideException(SubwayErrorCode.INVALID_DIRECTION);
        }
    }
}
