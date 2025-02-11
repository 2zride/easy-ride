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
        SubwayStation prevStation = toSubwayStation(prev);
        SubwayStation nextStation = toSubwayStation(next);
        return new NearSubwayStations(prevStation, nextStation);
    }

    private SubwayStation toSubwayStation(List<StationDetail> stationDetail) {
        if (stationDetail == null) {
            return null;
        }
        StationDetail station = stationDetail.get(0);
        return SubwayStation.of(station.stationId, station.stationName, station.type);
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
