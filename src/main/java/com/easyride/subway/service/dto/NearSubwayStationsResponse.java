package com.easyride.subway.service.dto;

import com.easyride.subway.domain.NearSubwayStations;

public record NearSubwayStationsResponse(String prevStationName,
                                         String nextStationName) {

    public NearSubwayStationsResponse(NearSubwayStations stations) {
        this(stations.getPrevStation().getName(),
                stations.getNextStation().getName());
    }
}
