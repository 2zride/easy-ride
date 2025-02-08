package com.easyride.subway.service.dto;

import com.easyride.subway.domain.NearSubwayStations;

public record NearSubwayStationsResponse(String prevStationName,
                                         String nextStationName) {

    private static final String EMPTY = "";

    public NearSubwayStationsResponse(NearSubwayStations stations) {
        this(stations.getPrevStation() != null ? stations.getPrevStation().getName() : EMPTY,
                stations.getNextStation() != null ? stations.getNextStation().getName() : EMPTY);
    }
}
