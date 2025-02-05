package com.easyride.subway.client.dto;

import java.util.List;

public record OdsaySearchStationSuccessResponse(Result result) {

    public List<Station> stations() {
        return result.station();
    }

    private record Result(Integer totalCount,
                          List<Station> station) {
    }

    public record Station(String stationName,
                          String stationId,
                          Integer type) {
    }
}
