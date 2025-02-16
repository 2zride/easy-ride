package com.easyride.subway.client.dto;

import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Arrays;
import java.util.List;

public record SkRealTimeCongestionResponse(
        Boolean success,
        Integer code,
        @JsonAlias("msg")
        String message,
        DataDetail data
) {

    public boolean isError() {
        return !success;
    }

    public SubwayCongestion toSubwayCongestion(Subway subway) {
        int subwayCongestion = Integer.parseInt(data.congestionResult.congestionTrain);
        List<Integer> carCongestions = Arrays.stream(data.congestionResult.congestionCar.split("\\|"))
                .map(Integer::parseInt)
                .toList();
        return SubwayCongestion.of(subway, subwayCongestion, carCongestions);
    }

    private record DataDetail(
            @JsonAlias("subwayLine")
            String stationLine,
            @JsonAlias("trainY")
            String subwayId,
            CongestionDetail congestionResult
    ) {
    }

    private record CongestionDetail(
            String congestionTrain,
            String congestionCar,
            Integer congestionType // TODO 로깅 필수
    ) {
    }
}
