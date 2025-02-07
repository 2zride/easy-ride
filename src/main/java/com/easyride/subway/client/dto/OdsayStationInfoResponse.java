package com.easyride.subway.client.dto;

import com.easyride.subway.domain.SubwayStation;
import java.util.List;
import java.util.stream.Stream;

public class OdsayStationInfoResponse extends OdsayResponse {

    private final SuccessDetail result;

    public OdsayStationInfoResponse(List<ErrorDetail> error, SuccessDetail result) {
        super(error);
        this.result = result;
    }

    public List<SubwayStation> toDomain() {
        StationDetail prevStation = result.prevObj().get(0);
        StationDetail nextStation = result.nextObj().get(0);
        return Stream.of(prevStation, nextStation)
                .map(station -> new SubwayStation(station.stationId, station.stationName, station.type))
                .toList();
    }

    private record SuccessDetail(List<StationDetail> prevObj,
                                 List<StationDetail> nextObj) {
    }

    private record StationDetail(String stationName,
                                 String stationId,
                                 Integer type) {
    }
}
