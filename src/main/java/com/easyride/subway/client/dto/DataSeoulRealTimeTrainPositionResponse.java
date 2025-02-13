package com.easyride.subway.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record DataSeoulRealTimeTrainPositionResponse(
        DataSeoulErrorResponse errorMessage,
        List<RealTimePosition> realtimePositionList
) {

    private record RealTimePosition(
            Integer totalCount,

            @JsonAlias("rowNum")
            Integer rowNumber,

            @JsonAlias("subwayId")
            String stationLineId,

            @JsonAlias("statnId")
            String stationId, // ex. 1002000209

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
}
