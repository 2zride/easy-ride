package com.easyride.subway.client.dto;

import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public class OdsaySearchStationResponse extends OdsayResponse {

    private final SuccessDetail result;

    public OdsaySearchStationResponse(List<ErrorDetail> error, SuccessDetail result) {
        super(error);
        this.result = result;
    }

    public SubwayStations toSubwayStations() {
        List<StationDetail> stationDetails = result.station();
        List<SubwayStation> subwayStations = stationDetails.stream()
                .map(station -> new SubwayStation(station.stationId, station.stationName, station.type))
                .toList();
        return new SubwayStations(subwayStations);
    }

    private record SuccessDetail(
            Integer totalCount,
            List<StationDetail> station) {
    }

    private record StationDetail(
            @JsonAlias("stationID")
            String stationId,
            String stationName,
            Integer type) {
    }
}
