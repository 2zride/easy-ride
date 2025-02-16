package com.easyride.subway.client.dto;

import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayStation;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public class OdsayStationInfoResponse extends OdsayResponse {

    private final SuccessDetail result;

    public OdsayStationInfoResponse(List<ErrorDetail> error, SuccessDetail result) {
        super(error);
        this.result = result;
    }

    public NearSubwayStations toNearSubwayStations() {
        List<StationDetail> prev = result.prevObj().station;
        List<StationDetail> next = result.nextObj().station;
        NearSubwayStations nearSubwayStations = new NearSubwayStations();
        nearSubwayStations.addStations(toSubwayStations(prev));
        nearSubwayStations.addStations(toSubwayStations(next));
        return nearSubwayStations;
    }

    private List<SubwayStation> toSubwayStations(List<StationDetail> stationDetails) {
        if (stationDetails == null) {
            return List.of();
        }
        return stationDetails.stream()
                .map(detail -> SubwayStation.of(detail.stationId, detail.stationName, detail.type))
                .toList();
    }

    private record SuccessDetail(
            @JsonAlias("prevOBJ")
            StationsDetail prevObj,
            @JsonAlias("nextOBJ")
            StationsDetail nextObj) {
    }

    private record StationsDetail(
            List<StationDetail> station) {
    }

    private record StationDetail(
            @JsonAlias("stationID")
            String stationId,
            String stationName,
            Integer type) {
    }
}
