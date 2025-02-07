package com.easyride.subway.client.dto;

import com.easyride.subway.domain.SubwayStation;
import java.util.List;

public class OdsaySearchStationResponse extends OdsayResponse {

    private final SuccessDetail result;

    public OdsaySearchStationResponse(List<ErrorDetail> error, SuccessDetail result) {
        super(error);
        this.result = result;
    }

    public List<SubwayStation> toDomains() {
        List<StationDetail> stations = result.station();
        return stations.stream()
                .map(station -> new SubwayStation(station.stationId, station.stationName, station.type))
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
