package com.easyride.subway.service.dto;

import com.easyride.subway.domain.NearSubwayStations;
import java.util.List;

public record NearSubwayStationsResponse(
        List<SubwayStationDetail> stations
) {

    public NearSubwayStationsResponse(NearSubwayStations nearStations) {
        this(nearStations.getStations().stream()
                .map(SubwayStationDetail::new)
                .toList());
    }
}
