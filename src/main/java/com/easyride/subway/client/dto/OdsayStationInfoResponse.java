package com.easyride.subway.client.dto;

import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayStation;
import java.util.List;

public class OdsayStationInfoResponse extends OdsayResponse {

    private final SuccessDetail result;

    public OdsayStationInfoResponse(List<ErrorDetail> error, SuccessDetail result) {
        super(error);
        this.result = result;
    }

    public NearSubwayStations toDomain() {
        StationDetail prev = result.prevObj().get(0);
        StationDetail next = result.nextObj().get(0);
        SubwayStation prevStation = new SubwayStation(prev.stationId, prev.stationName, prev.type);
        SubwayStation nextStation = new SubwayStation(next.stationId, next.stationName, next.type);
        return new NearSubwayStations(prevStation, nextStation);
    }

    private record SuccessDetail(List<StationDetail> prevObj,
                                 List<StationDetail> nextObj) {
    }

    private record StationDetail(String stationName,
                                 String stationId,
                                 Integer type) {
    }
}
