package com.easyride.bus.dto.request;

import com.easyride.bus.domain.Coordinates;

public record StationSearchRequest(
        String stationName,
        Coordinates stationCoordinates
) {

}
