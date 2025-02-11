package com.easyride.subway.client.dto;

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

    public SubwayCongestion toSubwayCongestion() {
        String[] rawCarCongestions = data.congestionResult.congestionCar.split("\\|");
        List<Integer> carCongestions = Arrays.stream(rawCarCongestions)
                .map(Integer::parseInt)
                .toList();
        return SubwayCongestion.of(
                Integer.parseInt(data.subwayLine),
                data.trainY,
                Integer.parseInt(data.congestionResult.congestionTrain),
                carCongestions);
    }

    private record DataDetail(
            String subwayLine,
            String trainY,
            CongestionDetail congestionResult
    ) {
    }

    private record CongestionDetail(
            String congestionTrain,
            String congestionCar,
            Integer congestionType
    ) {
    }
}
