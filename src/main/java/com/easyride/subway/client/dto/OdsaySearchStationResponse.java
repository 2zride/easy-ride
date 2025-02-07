package com.easyride.subway.client.dto;

import java.util.List;

public class OdsaySearchStationResponse extends OdsayResponse {

    private final SuccessDetail result;

    public OdsaySearchStationResponse(List<ErrorDetail> error, SuccessDetail result) {
        super(error);
        this.result = result;
    }

    public List<Integer> stationTypes() {
        List<StationDetail> stations = result.station();
        return stations.stream()
                .map(StationDetail::type)
                .toList();
    }

    private record SuccessDetail(Integer totalCount,
                                 List<StationDetail> station) {
    }

    private record StationDetail(String stationName,
                                 String stationId,
                                 Integer type) {
    }
}
